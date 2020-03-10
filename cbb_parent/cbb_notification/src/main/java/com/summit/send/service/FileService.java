package com.summit.send.service;

import com.summit.send.pojo.FileInfo;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName FileService
 * @Description 文件服务
 * @Author maoyuxuan
 * @Date 2020/3/4 12:05
 * @Version 1.0
 **/
public interface FileService {

    public String addAttachment(List<FileInfo> fileInfos) throws IOException;

}
