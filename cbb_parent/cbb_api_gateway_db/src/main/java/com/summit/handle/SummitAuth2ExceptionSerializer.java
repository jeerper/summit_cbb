package com.summit.handle;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.summit.common.entity.ResponseCodeEnum;

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

        ResponseCodeEnum responseCodeEnum = ResponseCodeEnum.valueOf(value.getMessage());
        gen.writeStartObject();
        gen.writeStringField("code", responseCodeEnum.getCode());
        gen.writeStringField("msg", responseCodeEnum.getMessage());
        //消息主体开始
        gen.writeObjectFieldStart("data");
        gen.writeStringField("timestamp", DateUtil.now());
        gen.writeEndObject();
        //消息主体结束
        gen.writeEndObject();
    }
}
