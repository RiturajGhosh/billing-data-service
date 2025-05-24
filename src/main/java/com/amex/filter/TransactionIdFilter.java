package com.amex.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionIdFilter implements Filter {

    private static final String TRANSACTION_ID = "txId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String txId = UUID.randomUUID().toString(); // or extract from headers if needed
        MDC.put(TRANSACTION_ID, txId);              // add to MDC for logging
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("X-Transaction-ID", txId);    // optional: include in response

        try {
            chain.doFilter(request, response);      // continue request
        } finally {
            MDC.remove(TRANSACTION_ID);             // clean up
        }
    }
}
