package io.github.amings.mingle.svc.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * response payload wrapper
 *
 * @author Ming
 */

public class SvcResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private HttpServletResponse response;

    /**
     * @param response HttpServletResponse
     */
    public SvcResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    public byte[] getBody() {
        return byteArrayOutputStream.toByteArray();
    }

    public String getBody4String() {
        try {
            return byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(
                new OutputStreamWriter(this.byteArrayOutputStream, this.response.getCharacterEncoding()));
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {

        private ByteArrayOutputStream outputStream;
        private HttpServletResponse response;

        public ServletOutputStreamWrapper(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response) {
            this.outputStream = byteArrayOutputStream;
            this.response = response;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (!this.response.isCommitted()) {
                byte[] body = this.outputStream.toByteArray();
                ServletOutputStream outputStream = this.response.getOutputStream();
                IOUtils.write(body, outputStream);
                outputStream.flush();
            }
        }
    }

}
