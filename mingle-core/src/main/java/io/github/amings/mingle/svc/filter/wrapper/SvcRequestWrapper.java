package io.github.amings.mingle.svc.filter.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * request payload wrapper
 *
 * @author Ming
 */

public class SvcRequestWrapper extends HttpServletRequestWrapper {

    private String body;

    /**
     * @param request HttpServletRequest
     */
    public SvcRequestWrapper(HttpServletRequest request) {
        super(request);
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            if (request.getInputStream() != null) {
                body = bufReader.lines().collect(Collectors.joining());
            }
        } catch (IOException ex) {
            body = null;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // TODO Auto-generated method stub

            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return body;
    }

}
