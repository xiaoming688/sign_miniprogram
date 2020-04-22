package com.sign.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheUtil {
    public static final Map<String, String> cacheSessionMap;
    static {
        cacheSessionMap = new ConcurrentHashMap<>();
    }
}
