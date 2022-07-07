package it.matlice.test.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputToOutputStream extends InputStream {

    InputStream in;
    OutputStream out;

    public InputToOutputStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public int read() throws IOException {
        int r = this.in.read();
        this.out.write(r);

        return r;
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        int r = this.in.read(b);

        for (int i = 0; i < r; i++) {
            this.out.write(b[i]);
        }

        return r;
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        int r = this.in.read(b, off, len);

        for (int i = 0; i < r; i++) {
            this.out.write(b[i]);
        }

        return r;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        byte[] r = this.in.readAllBytes();
        this.out.write(r);

        return r;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        byte[] r = this.in.readNBytes(len);
        this.out.write(r);

        return r;
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        int r = this.in.readNBytes(b, off, len);
        this.out.write(r);

        return r;
    }

    @Override
    public long skip(long n) throws IOException {
        return this.in.skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        this.in.skipNBytes(n);
    }

    @Override
    public int available() throws IOException {
        return this.in.available();
    }

    @Override
    public void close() throws IOException {
        this.in.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        this.in.reset();
    }

    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return this.in.transferTo(out);
    }

    public void flush() throws IOException {
        this.out.flush();
    }
}
