package com.summit.send.service.impl;

import com.summit.send.pojo.FileInfo;
import com.summit.send.service.FileService;
import com.summit.send.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FileServiceImpl
 * @Description TODO
 * @Author maoyuxuan
 * @Date 2020/3/4 12:05
 * @Version 1.0
 **/
@Service
public class FileServiceImpl implements FileService{

    @Value("${file.upload.url}")
    String uploadFilePath;

    @Value("${file.upload.path}")
    String filePath;

    public String addAttachment(List<FileInfo> fileInfos) throws IOException {
        if (fileInfos == null || fileInfos.size() == 0){
            return null;
        }
        List<String> urlList = new ArrayList<>(fileInfos.size());
        for (FileInfo fileInfo : fileInfos) {
            String url = this.saveFile(fileInfo.getByteArrayResource().getByteArray(), fileInfo.getFileName());
            urlList.add(url);
        }
        return CommonUtils.filePathListToString(urlList);
    }

    private String saveFile(byte[] data,String fileName) throws IOException {
        // 生成时间序列
        String timeDir = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String dirPath = filePath + timeDir + "/";
        File dirFile = new File(dirPath);
        if (!dirFile.isDirectory()) {
            dirFile.mkdirs();
        }
        File newFile = new File(dirPath + fileName);
        if (!newFile.isFile() && !newFile.exists()) {
            newFile.createNewFile();
        } else {
            newFile.delete();
            newFile.createNewFile();
        }

        FileOutputStream outputStream = new FileOutputStream(newFile);
        try {
            outputStream.write(data);
        } finally {
            outputStream.close();
        }

        String filePath = uploadFilePath + dirPath + fileName;
        return filePath;
    }

}
