package com.summit.cbb.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelUtils {

    // 测试
    public static void main(String[] args) {

        Map<String, Object> item = new HashMap<>();
        item.put("sttp", "河道站");
        item.put("stcd", "60225896");
        item.put("stnm", "浮子");
        item.put("org", "渝北水文监测中心");
        item.put("img1", "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\5a5820ac9304f.jpg");
        item.put("img2", "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\5a5820ac9304f.jpg");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateStr = dateFormat.format(new Date());
        String path = "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\维修表和巡检表excel版.xls";
        String path2 = "E:\\workspace_cbb\\cbb_parent\\cbb_utils\\template-file\\维修表和巡检表excel版-" + dateStr + ".xls";
        replaceExcel(item, path, path2);

    }

    /**
     * 替换Excel模板文件内容
     *
     * @param item           文档数据
     * @param sourceFilePath Excel模板文件路径
     * @param targetFilePath Excel生成文件路径
     */
    public static boolean replaceExcel(Map<String, Object> item, String sourceFilePath, String targetFilePath) {
        boolean bool = true;
        BufferedImage bufferImg = null;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(sourceFilePath));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            Iterator<?> rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                if (row != null) {
                    int num = row.getLastCellNum();
                    for (int i = 0; i < num; i++) {
                        HSSFCell cell = row.getCell(i);
                        if (cell != null) {
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        }
                        if (cell == null || cell.getStringCellValue() == null) {
                            continue;
                        }
                        String value = cell.getStringCellValue();
                        if (!"".equals(value)) {
                            if (value.startsWith("${img")) {
                                try {
                                    String filePath = replaceText(item, value);
                                    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                                    bufferImg = ImageIO.read(new File(filePath));
                                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                                    // anchor主要用于设置图片的属性
                                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
                                            (short) cell.getCellNum(), cell.getRow().getRowNum(),
                                            (short) (cell.getColumnIndex() + 2), cell.getRow().getRowNum());
                                    anchor.setAnchorType(3);
                                    // 插入图片
                                    patriarch.createPicture(anchor,
                                            wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                                    cell.setCellValue("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String target = replaceText(item, value);
                                cell.setCellValue(target);
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }

            // 输出文件
            FileOutputStream fileOut = new FileOutputStream(targetFilePath);
            wb.write(fileOut);
            fileOut.close();
            System.out.println("excel参数替换成功！");
        } catch (Exception e) {
            System.out.println("excel参数替换失败！");
            bool = false;
            e.printStackTrace();
        }
        return bool;
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
