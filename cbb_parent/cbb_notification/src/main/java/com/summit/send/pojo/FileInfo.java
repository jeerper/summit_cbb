package com.summit.send.pojo;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayInputStream;

/**
 * @ClassName FileInfo
 * @Description TODO
 * @Author maoyuxuan
 * @Date 2020/3/4 14:43
 * @Version 1.0
 **/
@Data
public class FileInfo {

    //文件名称
    private String fileName;

    //文件存储地址
    private String url;

    //文件内容（流）
    ByteArrayResource byteArrayResource;
}
