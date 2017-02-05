package com.construction.pm.networks.webapi;

import java.util.HashMap;

public class WebApiParam {
    protected HashMap<String, String> mHashMap;

    public WebApiParam() {
        mHashMap = new HashMap<String, String>();
    }

    public void put(final String key, final String value) {
        mHashMap.put(key, value);
    }

    public void put(final String key, final int value) {
        mHashMap.put(key, String.valueOf(value));
    }

    public void put(final String key, final Integer value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value));
        else
            mHashMap.put(key, null);
    }

    public void put(final String key, final long value) {
        mHashMap.put(key, String.valueOf(value));
    }

    public void put(final String key, final Long value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value));
        else
            mHashMap.put(key, null);
    }

    public void put(final String key, final boolean value) {
        mHashMap.put(key, String.valueOf(value ? 1 : 0));
    }

    public void put(final String key, final Boolean value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value ? 1 : 0));
        else
            mHashMap.put(key, null);
    }

    public void add(final String key, final String value) {
        put(key, value);
    }

    public void add(final String key, final int value) {
        put(key, value);
    }

    public void add(final String key, final Integer value) {
        put(key, value);
    }

    public void add(final String key, final long value) {
        put(key, value);
    }

    public void add(final String key, final Long value) {
        put(key, value);
    }

    public void add(final String key, final boolean value) {
        put(key, value);
    }

    public void add(final String key, final Boolean value) {
        put(key, value);
    }

    public HashMap<String, String> getParams() {
        return mHashMap;
    }

    public boolean has(final String key) {
        return mHashMap.containsKey(key);
    }

    public void remove(final String key) {
        mHashMap.remove(key);
    }

    public void clear() {
        mHashMap.clear();
    }
}
