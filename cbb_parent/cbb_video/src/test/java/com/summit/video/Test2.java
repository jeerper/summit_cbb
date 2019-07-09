package com.summit.video;

import com.sun.jna.NativeLong;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class Test2 {
    private static final ConcurrentHashMap<String, NativeLong> DEVICE_MAP = new ConcurrentHashMap<String, NativeLong>();
    @Test
    public void haha(){

        DEVICE_MAP.put("192.168.141.200",new NativeLong(13));
        DEVICE_MAP.put("192.168.141.201",new NativeLong(12));
        DEVICE_MAP.values().remove(new NativeLong(12));
        System.out.println(DEVICE_MAP);
    }
}
