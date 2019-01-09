package com.nemo.net.kryo;

import java.io.IOException;
import java.io.InputStream;

public class KryoInput extends InputStream{
    private byte[] buffer;
    private int capacity;
    private int position;
    private int limit;
    private int total;
    private char[] chars;
    private InputStream inputStream;

    public KryoInput() {
        this.chars = new char[32];
    }

    public KryoInput(int bufferSize) {
        this.chars = new char[32];
        this.capacity = bufferSize;
        this.buffer = new byte[bufferSize];
    }

    public KryoInput(byte[] buffer) {
        this.chars = new char[32];
        this.setBuffer(buffer, 0, buffer.length);
    }

    public KryoInput(byte[] buffer, int offset, int count) {
        this.chars = new char[32];
        this.setBuffer(buffer, offset, count);
    }

    public KryoInput(InputStream inputStream) {
        this(4096);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        } else {
            this.inputStream = inputStream;
        }
    }

    public KryoInput(InputStream inputStream, int bufferSize) {
        this(bufferSize);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        } else {
            this.inputStream = inputStream;
        }
    }

    public void setBuffer(byte[] bytes) {
        this.setBuffer(bytes, 0, bytes.length);
    }

    public void setBuffer(byte[] bytes, int offset, int count) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        } else {
            this.buffer = bytes;
            this.position = offset;
            this.limit = count;
            this.capacity = bytes.length;
            this.total = 0;
            this.inputStream = null;
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.limit = 0;
        this.rewind();
    }

    public int total() {
        return this.total + this.position;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void rewind() {
        this.position = 0;
        this.total = 0;
    }

    public void skip(int count) throws KryoException {
        int skipCount = Math.min(this.limit - this.position, count);

        while(true) {
            this.position += skipCount;
            count -= skipCount;
            if (count == 0) {
                return;
            }

            skipCount = Math.min(count, this.capacity);
            this.require(skipCount);
        }
    }

    protected int fill(byte[] buffer, int offset, int count) throws KryoException {
        if (this.inputStream == null) {
            return -1;
        } else {
            try {
                return this.inputStream.read(buffer, offset, count);
            } catch (IOException var5) {
                throw new KryoException(var5);
            }
        }
    }

    private int require(int required) throws KryoException {
        int remaining = this.limit - this.position;
        if (remaining >= required) {
            return remaining;
        } else if (required > this.capacity) {
            throw new KryoException("Buffer too small: capacity: " + this.capacity + ", required: " + required);
        } else {
            int count = this.fill(this.buffer, this.limit, this.capacity - this.limit);
            if (count == -1) {
                throw new KryoException("Buffer underflow.");
            } else {
                remaining += count;
                if (remaining >= required) {
                    this.limit += count;
                    return remaining;
                } else {
                    System.arraycopy(this.buffer, this.position, this.buffer, 0, remaining);
                    this.total += this.position;
                    this.position = 0;

                    do {
                        count = this.fill(this.buffer, remaining, this.capacity - remaining);
                        if (count == -1) {
                            if (remaining < required) {
                                throw new KryoException("Buffer underflow.");
                            }
                            break;
                        }

                        remaining += count;
                    } while(remaining < required);

                    this.limit = remaining;
                    return remaining;
                }
            }
        }
    }

    private int optional(int optional) throws KryoException {
        int remaining = this.limit - this.position;
        if (remaining >= optional) {
            return optional;
        } else {
            optional = Math.min(optional, this.capacity);
            int count = this.fill(this.buffer, this.limit, this.capacity - this.limit);
            if (count == -1) {
                return remaining == 0 ? -1 : Math.min(remaining, optional);
            } else {
                remaining += count;
                if (remaining >= optional) {
                    this.limit += count;
                    return optional;
                } else {
                    System.arraycopy(this.buffer, this.position, this.buffer, 0, remaining);
                    this.total += this.position;
                    this.position = 0;

                    do {
                        count = this.fill(this.buffer, remaining, this.capacity - remaining);
                        if (count == -1) {
                            break;
                        }

                        remaining += count;
                    } while(remaining < optional);

                    this.limit = remaining;
                    return remaining == 0 ? -1 : Math.min(remaining, optional);
                }
            }
        }
    }

    public boolean eof() {
        return this.optional(1) == 0;
    }

    public int available() throws IOException {
        return this.limit - this.position + (null != this.inputStream ? this.inputStream.available() : 0);
    }

    public int read() throws KryoException {
        return this.optional(1) == 0 ? -1 : this.buffer[this.position++] & 255;
    }

    public int read(byte[] bytes) throws KryoException {
        return this.read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int offset, int count) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        } else {
            int startingCount = count;
            int copyCount = Math.min(this.limit - this.position, count);

            do {
                System.arraycopy(this.buffer, this.position, bytes, offset, copyCount);
                this.position += copyCount;
                count -= copyCount;
                if (count == 0) {
                    break;
                }

                offset += copyCount;
                copyCount = this.optional(count);
                if (copyCount == -1) {
                    if (startingCount == count) {
                        return -1;
                    }
                    break;
                }
            } while(this.position != this.limit);

            return startingCount - count;
        }
    }

    public long skip(long count) throws KryoException {
        int skip;
        for(long remaining = count; remaining > 0L; remaining -= (long)skip) {
            skip = Math.max(2147483647, (int)remaining);
            this.skip(skip);
        }

        return count;
    }

    public void close() throws KryoException {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    public byte readByte() throws KryoException {
        this.require(1);
        return this.buffer[this.position++];
    }

    public int readByteUnsigned() throws KryoException {
        this.require(1);
        return this.buffer[this.position++] & 255;
    }

    public byte[] readBytes(int length) throws KryoException {
        byte[] bytes = new byte[length];
        this.readBytes(bytes, 0, length);
        return bytes;
    }

    public void readBytes(byte[] bytes) throws KryoException {
        this.readBytes(bytes, 0, bytes.length);
    }

    public void readBytes(byte[] bytes, int offset, int count) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        } else {
            int copyCount = Math.min(this.limit - this.position, count);

            while(true) {
                System.arraycopy(this.buffer, this.position, bytes, offset, copyCount);
                this.position += copyCount;
                count -= copyCount;
                if (count == 0) {
                    return;
                }

                offset += copyCount;
                copyCount = Math.min(count, this.capacity);
                this.require(copyCount);
            }
        }
    }

    public int readInt() throws KryoException {
        this.require(4);
        byte[] buffer = this.buffer;
        int position = this.position;
        this.position = position + 4;
        return (buffer[position] & 255) << 24 | (buffer[position + 1] & 255) << 16 | (buffer[position + 2] & 255) << 8 | buffer[position + 3] & 255;
    }

    public int readInt(boolean optimizePositive) throws KryoException {
        if (this.require(1) < 5) {
            return this.readInt_slow(optimizePositive);
        } else {
            int b = this.buffer[this.position++];
            int result = b & 127;
            if ((b & 128) != 0) {
                byte[] buffer = this.buffer;
                b = buffer[this.position++];
                result |= (b & 127) << 7;
                if ((b & 128) != 0) {
                    b = buffer[this.position++];
                    result |= (b & 127) << 14;
                    if ((b & 128) != 0) {
                        b = buffer[this.position++];
                        result |= (b & 127) << 21;
                        if ((b & 128) != 0) {
                            b = buffer[this.position++];
                            result |= (b & 127) << 28;
                        }
                    }
                }
            }

            return optimizePositive ? result : result >>> 1 ^ -(result & 1);
        }
    }

    private int readInt_slow(boolean optimizePositive) {
        int b = this.buffer[this.position++];
        int result = b & 127;
        if ((b & 128) != 0) {
            this.require(1);
            byte[] buffer = this.buffer;
            b = buffer[this.position++];
            result |= (b & 127) << 7;
            if ((b & 128) != 0) {
                this.require(1);
                b = buffer[this.position++];
                result |= (b & 127) << 14;
                if ((b & 128) != 0) {
                    this.require(1);
                    b = buffer[this.position++];
                    result |= (b & 127) << 21;
                    if ((b & 128) != 0) {
                        this.require(1);
                        b = buffer[this.position++];
                        result |= (b & 127) << 28;
                    }
                }
            }
        }

        return optimizePositive ? result : result >>> 1 ^ -(result & 1);
    }

    public boolean canReadInt() throws KryoException {
        if (this.limit - this.position >= 5) {
            return true;
        } else if (this.optional(5) <= 0) {
            return false;
        } else {
            int p = this.position;
            if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else {
                return p != this.limit;
            }
        }
    }

    public boolean canReadLong() throws KryoException {
        if (this.limit - this.position >= 9) {
            return true;
        } else if (this.optional(5) <= 0) {
            return false;
        } else {
            int p = this.position;
            if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else if (p == this.limit) {
                return false;
            } else if ((this.buffer[p++] & 128) == 0) {
                return true;
            } else {
                return p != this.limit;
            }
        }
    }

    public String readString() {
        int available = this.require(1);
        int b = this.buffer[this.position++];
        if ((b & 128) == 0) {
            return this.readAscii();
        } else {
            int charCount = available >= 5 ? this.readUtf8Length(b) : this.readUtf8Length_slow(b);
            switch(charCount) {
                case 0:
                    return null;
                case 1:
                    return "";
                default:
                    --charCount;
                    if (this.chars.length < charCount) {
                        this.chars = new char[charCount];
                    }

                    this.readUtf8(charCount);
                    return new String(this.chars, 0, charCount);
            }
        }
    }

    private int readUtf8Length(int b) {
        int result = b & 63;
        if ((b & 64) != 0) {
            byte[] buffer = this.buffer;
            int b1 = buffer[this.position++];
            result |= (b1 & 127) << 6;
            if ((b1 & 128) != 0) {
                b1 = buffer[this.position++];
                result |= (b1 & 127) << 13;
                if ((b1 & 128) != 0) {
                    b1 = buffer[this.position++];
                    result |= (b1 & 127) << 20;
                    if ((b1 & 128) != 0) {
                        b1 = buffer[this.position++];
                        result |= (b1 & 127) << 27;
                    }
                }
            }
        }

        return result;
    }

    private int readUtf8Length_slow(int b) {
        int result = b & 63;
        if ((b & 64) != 0) {
            this.require(1);
            byte[] buffer = this.buffer;
            int b1 = buffer[this.position++];
            result |= (b1 & 127) << 6;
            if ((b1 & 128) != 0) {
                this.require(1);
                b1 = buffer[this.position++];
                result |= (b1 & 127) << 13;
                if ((b1 & 128) != 0) {
                    this.require(1);
                    b1 = buffer[this.position++];
                    result |= (b1 & 127) << 20;
                    if ((b1 & 128) != 0) {
                        this.require(1);
                        b1 = buffer[this.position++];
                        result |= (b1 & 127) << 27;
                    }
                }
            }
        }

        return result;
    }

    private void readUtf8(int charCount) {
        byte[] buffer = this.buffer;
        char[] chars = this.chars;
        int charIndex = 0;
        int count = Math.min(this.require(1), charCount);

        int position;
        byte b;
        for(position = this.position; charIndex < count; chars[charIndex++] = (char)b) {
            b = buffer[position++];
            if (b < 0) {
                --position;
                break;
            }
        }

        this.position = position;
        if (charIndex < charCount) {
            this.readUtf8_slow(charCount, charIndex);
        }

    }

    private void readUtf8_slow(int charCount, int charIndex) {
        char[] chars = this.chars;

        for(byte[] buffer = this.buffer; charIndex < charCount; ++charIndex) {
            if (this.position == this.limit) {
                this.require(1);
            }

            int b = buffer[this.position++] & 255;
            switch(b >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    chars[charIndex] = (char)b;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    break;
                case 12:
                case 13:
                    if (this.position == this.limit) {
                        this.require(1);
                    }

                    chars[charIndex] = (char)((b & 31) << 6 | buffer[this.position++] & 63);
                    break;
                case 14:
                    this.require(2);
                    chars[charIndex] = (char)((b & 15) << 12 | (buffer[this.position++] & 63) << 6 | buffer[this.position++] & 63);
            }
        }

    }

    private String readAscii() {
        byte[] buffer = this.buffer;
        int end = this.position;
        int start = end - 1;
        int limit = this.limit;

        while(end != limit) {
            int b = buffer[end++];
            if ((b & 128) != 0) {
                buffer[end - 1] = (byte)(buffer[end - 1] & 127);
                String value = new String(buffer, 0, start, end - start);
                buffer[end - 1] = (byte)(buffer[end - 1] | 128);
                this.position = end;
                return value;
            }
        }

        return this.readAscii_slow();
    }

    private String readAscii_slow() {
        --this.position;
        int charCount = this.limit - this.position;
        if (charCount > this.chars.length) {
            this.chars = new char[charCount * 2];
        }

        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        int i = this.position;
        int ii = 0;

        for(int n = this.limit; i < n; ++ii) {
            chars[ii] = (char)buffer[i];
            ++i;
        }

        this.position = this.limit;

        while(true) {
            this.require(1);
            int b = buffer[this.position++];
            if (charCount == chars.length) {
                char[] newChars = new char[charCount * 2];
                System.arraycopy(chars, 0, newChars, 0, charCount);
                chars = newChars;
                this.chars = newChars;
            }

            if ((b & 128) == 128) {
                chars[charCount++] = (char)(b & 127);
                return new String(chars, 0, charCount);
            }

            chars[charCount++] = (char)b;
        }
    }

    public StringBuilder readStringBuilder() {
        int available = this.require(1);
        int b = this.buffer[this.position++];
        if ((b & 128) == 0) {
            return new StringBuilder(this.readAscii());
        } else {
            int charCount = available >= 5 ? this.readUtf8Length(b) : this.readUtf8Length_slow(b);
            switch(charCount) {
                case 0:
                    return null;
                case 1:
                    return new StringBuilder("");
                default:
                    --charCount;
                    if (this.chars.length < charCount) {
                        this.chars = new char[charCount];
                    }

                    this.readUtf8(charCount);
                    StringBuilder builder = new StringBuilder(charCount);
                    builder.append(this.chars, 0, charCount);
                    return builder;
            }
        }
    }

    public float readFloat() throws KryoException {
        return Float.intBitsToFloat(this.readInt());
    }

    public float readFloat(float precision, boolean optimizePositive) throws KryoException {
        return (float)this.readInt(optimizePositive) / precision;
    }

    public short readShort() throws KryoException {
        this.require(2);
        return (short)((this.buffer[this.position++] & 255) << 8 | this.buffer[this.position++] & 255);
    }

    public int readShortUnsigned() throws KryoException {
        this.require(2);
        return (this.buffer[this.position++] & 255) << 8 | this.buffer[this.position++] & 255;
    }

    public long readLong() throws KryoException {
        this.require(8);
        byte[] buffer = this.buffer;
        return (long)buffer[this.position++] << 56 | (long)(buffer[this.position++] & 255) << 48 | (long)(buffer[this.position++] & 255) << 40 | (long)(buffer[this.position++] & 255) << 32 | (long)(buffer[this.position++] & 255) << 24 | (long)((buffer[this.position++] & 255) << 16) | (long)((buffer[this.position++] & 255) << 8) | (long)(buffer[this.position++] & 255);
    }

    public long readLong(boolean optimizePositive) throws KryoException {
        if (this.require(1) < 9) {
            return this.readLong_slow(optimizePositive);
        } else {
            int b = this.buffer[this.position++];
            long result = (long)(b & 127);
            if ((b & 128) != 0) {
                byte[] buffer = this.buffer;
                b = buffer[this.position++];
                result |= (long)((b & 127) << 7);
                if ((b & 128) != 0) {
                    b = buffer[this.position++];
                    result |= (long)((b & 127) << 14);
                    if ((b & 128) != 0) {
                        b = buffer[this.position++];
                        result |= (long)((b & 127) << 21);
                        if ((b & 128) != 0) {
                            b = buffer[this.position++];
                            result |= (long)(b & 127) << 28;
                            if ((b & 128) != 0) {
                                b = buffer[this.position++];
                                result |= (long)(b & 127) << 35;
                                if ((b & 128) != 0) {
                                    b = buffer[this.position++];
                                    result |= (long)(b & 127) << 42;
                                    if ((b & 128) != 0) {
                                        b = buffer[this.position++];
                                        result |= (long)(b & 127) << 49;
                                        if ((b & 128) != 0) {
                                            b = buffer[this.position++];
                                            result |= (long)b << 56;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!optimizePositive) {
                result = result >>> 1 ^ -(result & 1L);
            }

            return result;
        }
    }

    private long readLong_slow(boolean optimizePositive) {
        int b = this.buffer[this.position++];
        long result = (long)(b & 127);
        if ((b & 128) != 0) {
            this.require(1);
            byte[] buffer = this.buffer;
            b = buffer[this.position++];
            result |= (long)((b & 127) << 7);
            if ((b & 128) != 0) {
                this.require(1);
                b = buffer[this.position++];
                result |= (long)((b & 127) << 14);
                if ((b & 128) != 0) {
                    this.require(1);
                    b = buffer[this.position++];
                    result |= (long)((b & 127) << 21);
                    if ((b & 128) != 0) {
                        this.require(1);
                        b = buffer[this.position++];
                        result |= (long)(b & 127) << 28;
                        if ((b & 128) != 0) {
                            this.require(1);
                            b = buffer[this.position++];
                            result |= (long)(b & 127) << 35;
                            if ((b & 128) != 0) {
                                this.require(1);
                                b = buffer[this.position++];
                                result |= (long)(b & 127) << 42;
                                if ((b & 128) != 0) {
                                    this.require(1);
                                    b = buffer[this.position++];
                                    result |= (long)(b & 127) << 49;
                                    if ((b & 128) != 0) {
                                        this.require(1);
                                        b = buffer[this.position++];
                                        result |= (long)b << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!optimizePositive) {
            result = result >>> 1 ^ -(result & 1L);
        }

        return result;
    }

    public boolean readBoolean() throws KryoException {
        this.require(1);
        return this.buffer[this.position++] == 1;
    }

    public char readChar() throws KryoException {
        this.require(2);
        return (char)((this.buffer[this.position++] & 255) << 8 | this.buffer[this.position++] & 255);
    }

    public double readDouble() throws KryoException {
        return Double.longBitsToDouble(this.readLong());
    }

    public double readDouble(double precision, boolean optimizePositive) throws KryoException {
        return (double)this.readLong(optimizePositive) / precision;
    }
}
