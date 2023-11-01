package com.qq.oldkingok.okapi;

public class OkFormat {
    private String str;
    public OkFormat(String str) {
        this.str = str;
    }
    public OkFormat format(Object from, Object to) {
        if (from == null) throw new IllegalArgumentException("Object from can't be null!");
        if (to == null) {
            str = str.replaceAll(from.toString(), "Unknown");
        } else {
            str = str.replaceAll(from.toString(), to.toString());
        }
        return this;
    }

    @Override
    public String toString() {
        return str;
    }
}
