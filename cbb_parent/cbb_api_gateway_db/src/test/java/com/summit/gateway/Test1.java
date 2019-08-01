package com.summit.gateway;

import com.summit.common.util.Cryptographic;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class Test1 {
    @Test
    public void test1() {


        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    @Test
    public void haha() throws Exception {
        String str = Cryptographic.decryptAES("0VlUiYNzE%2BSnJMcW1636jQ%3D%3D", "summitsummitsumm");
        log.debug(str);
    }
}
