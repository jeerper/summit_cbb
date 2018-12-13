package com.summit.gateway;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test1 {
    @Test
    public void test1() {


            System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
