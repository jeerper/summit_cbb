package com.summit.send.util;

import java.security.SecureRandom;

public class VCodeGenerator {

    private static SecureRandom random = new SecureRandom();
    public static String getVerificationCode(){

        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + random.nextInt(10);
        }
        return vcode;
    }

//    public static void main(String[] args) {
//
//        System.out.println(getVerificationCode());
//
//    }

}
