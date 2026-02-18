package com.newhorizon.nhmonolithicapplication.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Component
@Slf4j
public class RequestIdHeaderWebFilter implements Filter {

    private final Tracer tracer;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(
                "request-id", currentTraceId());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String currentTraceId() {
        Span span = tracer.currentSpan();
        return nonNull(span) ? span.context().traceId() : null;
    }
}
