package com.nemo.net.kryo;

public class KryoUtil {
    public static int CACHESIZE = 1048576;

    private static final ThreadLocal<KryoOutput> cacheOutputs = new ThreadLocal<KryoOutput>() {
        @Override
        protected KryoOutput initialValue() {
            KryoOutput output = new KryoOutput(KryoUtil.CACHESIZE);
            return output;
        }
    };

    private static final ThreadLocal<KryoInput> cacheInputs = new ThreadLocal<KryoInput>() {
        @Override
        protected KryoInput initialValue() {
            KryoInput input = new KryoInput();
            return input;
        }
    };

    public KryoUtil(){
    }

    public static KryoOutput getOutput() {
        KryoOutput output = cacheOutputs.get();
        if(output.position() > 0) {
            output.setPosition(0);
            throw new RuntimeException("output出现迭代调用，或者使用之后没有清空");
        } else {
            return output;
        }
    }

    public static KryoInput getInput() {
        return cacheInputs.get();
    }

    public static void clear() {
        KryoOutput output = cacheOutputs.get();
        output.clear();
    }
}
