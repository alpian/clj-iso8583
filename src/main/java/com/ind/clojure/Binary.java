package com.ind.clojure;

public final class Binary {
    public static final byte unsignedByteCast(Object x) {
        long n = ((Number) x).longValue();
        if(n < 0 || n > 0xFF) {
            throw new IllegalArgumentException("Value out of range for unsigned byte: " + x);
        }
    
        return (byte) n;
    }
}
