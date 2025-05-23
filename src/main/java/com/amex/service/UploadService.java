package com.amex.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public interface UploadService {
    void upload(MultipartFile file) throws IOException;
}
