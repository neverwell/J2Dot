package com.nemo.net.kryo;

import java.io.IOException;

public abstract class KryoBean {
    public KryoBean() {
    }

    public abstract boolean write(KryoOutput buf);

    public abstract boolean read(KryoInput buf);

    protected void writeInt(KryoOutput output, int value, boolean positive) {
        output.writeInt(value, positive);
    }

    protected void writeInt(KryoOutput output, int value) {
        output.writeInt(value);
    }

    protected void writeString(KryoOutput output, String value) {
        output.writeString(value);
    }

    protected void writeLong(KryoOutput output, long value, boolean positive) {
        output.writeLong(value, positive);
    }

    protected void writeLong(KryoOutput output, long value) {
        output.writeLong(value);
    }

    protected void writeBean(KryoOutput output, KryoBean value) {
        if(value == null) {
            output.writeByte(0);
        } else {
            output.writeByte(1);
            value.write(output);
        }
    }

    protected void writeShort(KryoOutput output, int value) {
        output.writeShort((short)value);
    }

    protected void writeShort(KryoOutput output, short value) {
        output.writeShort(value);
    }

    protected void writeByte(KryoOutput output, byte value) {
        output.writeByte(value);
    }

    protected void writeBytes(KryoOutput output, byte[] bytes) {
        output.write(bytes);
    }

    protected void writeBoolean(KryoOutput output, boolean value) {
        output.writeBoolean(value);
    }

    protected int readInt(KryoInput input, boolean positive) {
        return input.readInt(positive);
    }

    protected int readInt(KryoInput input) {
        return input.readInt();
    }

    protected String readString(KryoInput input) {
        return input.readString();
    }

    protected long readLong(KryoInput input, boolean positive) {
        return input.readLong(positive);
    }

    protected long readLong(KryoInput input) {
        return input.readLong();
    }

    protected KryoBean readBean(KryoInput input, Class<? extends KryoBean> clazz) {
        byte isNull = input.readByte();
        if(isNull == 0) {
            return null;
        } else {
            try {
                KryoBean bean = clazz.newInstance();
                bean.read(input);
                return bean;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected short readShort(KryoInput input) {
        return input.readShort();
    }

    protected byte readByte(KryoInput input) {
        return input.readByte();
    }

    protected boolean readBoolean(KryoInput input) {
        return input.readBoolean();
    }

    protected byte[] readBytes(KryoInput input) {
        try {
            return input.readBytes(input.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
