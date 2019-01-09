package com.nemo.common.persist;

public interface Persistable extends Cacheable{
    boolean isDirty();

    void setDirty(boolean var1);

    long getTouchTime();
}
