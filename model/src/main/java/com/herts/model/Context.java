package com.herts.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;
import java.util.HashMap;

public class Context extends InputOutputParams {
    private Map<String, Object> data = new HashMap<>();

    public Context() {
        this.data = new HashMap<>();
    }

    public Context(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Map<String, Object> getAll() {
        return data;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        data.put(key, value);
    }
}
