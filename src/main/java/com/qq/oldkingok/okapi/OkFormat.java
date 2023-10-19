package com.qq.oldkingok.okapi;

public class OkFormat {
    private String str;
    public OkFormat(String str) {
        this.str = str;
    }
    public OkFormat format(Object from, Object to) {
        str = str.replaceAll(from.toString(), to.toString());
        return this;
    }

    @Override
    public String toString() {
        return str;
    }
}
