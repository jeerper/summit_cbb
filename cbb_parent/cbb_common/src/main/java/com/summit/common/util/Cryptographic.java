package com.summit.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Cryptographic {

    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES密码解密.
     *
     * @param data 加密过的密码
     * @param pass 秘钥
     * @return
     * @throws Exception
     */
    public static String decryptAES(String data, String pass) throws Exception {
        data = HttpUtil.decode(data, StandardCharsets.UTF_8);
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);

    }


}
