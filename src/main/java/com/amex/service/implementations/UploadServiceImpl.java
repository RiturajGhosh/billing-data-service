package com.amex.service.implementations;

import com.amex.dto.entity.Employee;
import com.amex.service.CustomThreadFactory;
import com.amex.service.UploadService;
import com.amex.util.MdcTaskDecorator;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class UploadServiceImpl implements UploadService {

    private static final int BATCH_SIZE = 1000;
    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final ExecutorService executorService;

    @Autowired
    public UploadServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() * 2, // corePoolSize
                Runtime.getRuntime().availableProcessors() + 10,   // maximumPoolSize
                60, TimeUnit.SECONDS,     // idle timeout
                new LinkedBlockingQueue<>(200),  // task queue size
                new CustomThreadFactory("db-batch-")
        );
    }


    @Override
    public void upload(MultipartFile file) throws IOException {
        List<Future<?>> futures = new ArrayList<>();
        List<Employee> batch = new ArrayList<>();
        try (InputStream iStream = file.getInputStream()) {
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)       // how many rows to keep in memory
                    .bufferSize(4096)        // buffer size to use when reading InputStream
                    .open(iStream);
            var sheet = workbook.getSheetAt(0);
            int count = 0;
            var startTime = System.currentTimeMillis();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                Employee emp = new Employee();
                emp.setId(UUID.randomUUID().toString());
                emp.setName(row.getCell(0).getStringCellValue());
                emp.setDepartment(row.getCell(1).getStringCellValue());
                emp.setSalary(row.getCell(2).getNumericCellValue());
                batch.add(emp);

                if (batch.size() == BATCH_SIZE) {
                    var prev = startTime;
                    startTime = System.currentTimeMillis();
                    log.info("Time taken for batch read: {}", startTime - prev);
                    List<Employee> toInsert = new ArrayList<>(batch);
                    batch.clear();
                    futures.add(executorService.submit(MdcTaskDecorator.decorate(() -> batchInsert(toInsert))));
                }
            }
            if (!batch.isEmpty()) {
                List<Employee> toInsert = new ArrayList<>(batch);
                futures.add(executorService.submit(MdcTaskDecorator.decorate(() -> batchInsert(toInsert))));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("Batch failed: " + e.getMessage());
            }
        }
    }

    private void batchInsert(List<Employee> batch) {
        log.info("Batch update started");
        var startTime = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(
                "INSERT INTO employee (id, name, department, salary) VALUES (?, ?, ?, ?)",
                batch,
                batch.size(),
                (ps, emp) -> {
                    ps.setString(1, emp.getId());
                    ps.setString(2, emp.getName());
                    ps.setString(3, emp.getDepartment());
                    ps.setDouble(4, emp.getSalary());
                });
        var endTime = System.currentTimeMillis();
        log.info("Time taken for update:{}", endTime - startTime);
    }
}