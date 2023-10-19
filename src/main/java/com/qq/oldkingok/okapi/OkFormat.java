package com.qq.oldkingok.okapi;

public class OkFormat {
    String str;
    OkFormat(String str) {
        this.str = str;
    }
    OkFormat format(Object from, Object to) {
        str = str.replaceAll(from.toString(), to.toString());
        return this;
    }

    @Override
    public String toString() {
        return str;
    }
}
