package com.summit.cbb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordUtils {

    // 创建一个新的word文件
    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lake_x", 108.33333);
        params.put("lake_y", 36.333333);
        params.put("lake_watershed", 36.22);
        params.put("lake_upwatershed", 40);
        params.put("lake_area", 32);
        params.put("space", "        ");
        params.put("img1", "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\5a5820ac9304f.jpg");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateStr = dateFormat.format(new Date());
        String path = "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\重庆堰塞湖应急方案.docx";
        String targetPath = "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\重庆堰塞湖应急方案-" + dateStr + ".docx";
        relaceWord(path, targetPath, params);
    }

    /**
     * 根据word模板，替换word中的参数
     *
     * @param resourceFile 模板文件
     * @param targetFile   目标文件
     * @param params       替换参数
     */
    public static void relaceWord(String resourceFile, String targetFile, Map<String, Object> params) {

        // 创建Word文件
        XWPFDocument targetDoc = new XWPFDocument();

        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(targetFile);
            if (file.isFile()) {
                file.delete();
            }
            file.createNewFile();
            // 模板读入流中
            FileInputStream is = new FileInputStream(resourceFile);
            // 新建一个word文档
            XWPFDocument xdf = new XWPFDocument(is);
            // 替换word中的变量。
            replaceText(targetDoc, xdf, params);
            // 写入
            fileOutputStream = new FileOutputStream(targetFile);
            targetDoc.write(fileOutputStream);
            System.out.println("word参数替换成功！");
        } catch (Exception e) {
            System.out.println("word参数替换失败！");
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换word参数
     *
     * @param targetDoc 目标word文件
     * @param sourceDoc 源word文件
     * @param params    参数
     */
    public static void replaceText(XWPFDocument targetDoc, XWPFDocument sourceDoc, Map<String, Object> params) {
        try {
            Iterator<XWPFParagraph> iterator = sourceDoc.getParagraphsIterator();
            XWPFParagraph para;
            Boolean first = true;
            while (iterator.hasNext()) {
                XWPFParagraph paragraph = targetDoc.createParagraph();// 创建段落文本
                para = iterator.next();
                ParagraphAlignment alignment = para.getAlignment();
                String xWPFParagraphText = para.getText();
                // 对其方式
                paragraph.setAlignment(alignment);
                List<XWPFRun> runsLists = para.getRuns();// 获取段楼中的句列表
                if (xWPFParagraphText.contains("${img")) {
                    String filePath = replaceText(params, xWPFParagraphText);
                    File file = new File(filePath);
                    InputStream pictureData = new FileInputStream(file);
                    int pictureType = 6;
                    XWPFRun xwpfRun = paragraph.createRun();
                    xwpfRun.addPicture(pictureData, pictureType, file.getName(), 5000000, 5000000);
                } else {
                    for (XWPFRun runsList : runsLists) {
                        XWPFRun xwpfRun = paragraph.createRun();
                        // 字体样式
                        String fontFamily = runsList.getFontFamily();
                        // 字体大小
                        Integer fontSize = runsList.getFontSize();
                        // 字体颜色
                        String fontColor = runsList.getColor();
                        // 内容
                        String text = runsList.getText(0);
                        // 第一行设置成粗体
                        if (first) {
                            xwpfRun.setBold(first);
                            first = false;
                        } else {
                            xwpfRun.setBold(false);
                        }
                        xwpfRun.setColor(fontColor);
                        xwpfRun.setFontFamily(fontFamily);
                        xwpfRun.setFontSize(fontSize);
                        xwpfRun.setText(replaceText(params, xWPFParagraphText));
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String replaceText(Map<String, Object> params, String sourceText) {
        for (Entry<String, Object> entry : params.entrySet()) {
            if (sourceText.contains("${" + entry.getKey() + "}")) {
                sourceText = sourceText.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return sourceText;
    }

}
