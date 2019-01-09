package com.nemo.net.kryo;

public class KryoException extends RuntimeException{
    private static final long serialVersionUID = 6666173322042239859L;
    private StringBuffer trace;

    public KryoException(){
    }

    public KryoException(String message, Throwable cause) {
        super(message, cause);
    }

    public KryoException(String message) {
        super(message);
    }

    public KryoException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        if(this.trace == null) {
            return super.getMessage();
        } else {
            StringBuffer buffer = new StringBuffer(512);
            buffer.append(super.getMessage());
            if(buffer.length() > 0) {
                buffer.append('\n');
            }
            buffer.append("Serialization trace:");
            buffer.append(this.trace);
            return buffer.toString();
        }
    }

    public void addTrace(String info) {
        if(info == null) {
            throw new IllegalArgumentException("info cannot be null.");
        } else {
            if(this.trace == null) {
                this.trace = new StringBuffer(512);
            }
            this.trace.append('\n');
            this.trace.append(info);
        }
    }
}
