package com.summit.handle;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * OAuth2 异常格式化
 *
 * @author Administrator
 */
public class SummitAuth2ExceptionSerializer extends StdSerializer<SummitAuth2Exception> {
    private static final long serialVersionUID = 7914343101260062281L;

    public SummitAuth2ExceptionSerializer() {
        super(SummitAuth2Exception.class);
    }

    @Override
    public void serialize(SummitAuth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("code", 200);
        gen.writeStringField("message", "success");

        //消息主体开始
        gen.writeObjectFieldStart("data");
        gen.writeStringField("code", "login_error");
        gen.writeStringField("msg", "登录异常");
        gen.writeStringField("data", value.getMessage());
        gen.writeObjectField("success", false);
        gen.writeStringField("timestamp", DateUtil.now());
        gen.writeEndObject();
        //消息主体结束

        gen.writeEndObject();
    }
}
