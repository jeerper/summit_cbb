package com.summit.cbb.utils;

import com.google.common.collect.Lists;
import com.summit.cbb.utils.constants.ExportSheetVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * 导出数据到EXCEL工具类
 * 
 * @author xjtuhgd
 * @date 2020/03/10
 */
public class ExportExcelUtil {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private static Logger log = LoggerFactory.getLogger(ExportExcelUtil.class);

    private static final String CHAR_ENTER = "\n";

    /**
     * 工作薄对象
     */
    private SXSSFWorkbook wb;

    public ExportExcelUtil() {
        this.wb = new SXSSFWorkbook(500);
    }

    public <E> void addSheet(ExportSheetVo<E> es) {
        addSheet(es.getTitle(), es.getNote(), es.getSheetName(), es.getC(), es.getDataList(), 1);
    }

    public <E> void addSheet(List<E> dataList, Class<E> c, String title, String note, String sheetName) {
        addSheet(title, note, sheetName, c, dataList, 1);
    }

    public <E> void addSheet(String title, String note, String sheetName, Class<?> cls, List<E> list, int type,
        int... groups) {

        SXSSFSheet sheet =
            wb.createSheet(StringUtils.isNotEmpty(sheetName) ? sheetName : "Export" + wb.getNumberOfSheets());
        Map<String, CellStyle> styles = createStyles(wb);
        // 注解列表（Object[]{ ExcelField, Field/Method }）
        List<Object[]> annotationList = Lists.newArrayList();

        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null) {
                if (0 == ef.type() || type == ef.type()) {
                    if (groups != null && groups.length > 0) {
                        boolean inGroup = false;
                        for (int g : groups) {
                            if (inGroup) {
                                break;
                            }
                            for (int efg : ef.groups()) {
                                if (g == efg) {
                                    inGroup = true;
                                    annotationList.add(new Object[] {ef, f});
                                    break;
                                }
                            }
                        }
                    } else {
                        annotationList.add(new Object[] {ef, f});
                    }
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null) {
                if (0 == ef.type() || type == ef.type()) {
                    if (groups != null && groups.length > 0) {
                        boolean inGroup = false;
                        for (int g : groups) {
                            if (inGroup) {
                                break;
                            }
                            for (int efg : ef.groups()) {
                                if (g == efg) {
                                    inGroup = true;
                                    annotationList.add(new Object[] {ef, m});
                                    break;
                                }
                            }
                        }
                    } else {
                        annotationList.add(new Object[] {ef, m});
                    }
                }
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField)o1[0]).sort()).compareTo(new Integer(((ExcelField)o2[0]).sort()));
            }

            ;
        });
        // Initialize
        List<String> headerList = Lists.newArrayList();
        for (Object[] os : annotationList) {
            String t = ((ExcelField)os[0]).title();
            // 如果是导出，则去掉注释
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        buildSheet(sheet, title, note, sheetName, headerList, annotationList, list, styles);
    }

    public <E> void buildSheet(SXSSFSheet sheet, String title, String note, String sheetName, List<String> headerList,
        List<Object[]> annotationList, List<E> list, Map<String, CellStyle> styles) {

        int rownum = 0;

        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(),
                headerList.size() - 1));
        }
        // Create note
        if (StringUtils.isNotBlank(note)) {
            Row noteRow = sheet.createRow(rownum++);
            noteRow.setHeightInPoints(30);
            if (note.contains(CHAR_ENTER)) {
                noteRow.setHeightInPoints(70);
            }
            Cell noteCell = noteRow.createCell(0);
            noteCell.setCellStyle(styles.get("note"));
            noteCell.setCellValue(note);
            sheet.addMergedRegion(
                new CellRangeAddress(noteRow.getRowNum(), noteRow.getRowNum(), 0, headerList.size() - 1));
        }
        // Create header
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(16);
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);

            CellStyle cellStyle2 = styles.get("header");
            cell.setCellStyle(cellStyle2);

            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = sheet.createDrawingPatriarch()
                    .createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)3, 3, (short)5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 4500 ? 4500 : colWidth);
        }
        log.debug("Initialize success.");
        setDataList(sheet, list, annotationList, styles, rownum);
    }

    /**
     * 添加数据（通过annotation.ExportField添加数据）
     *
     * @return list 数据列表
     */
    public <E> void setDataList(SXSSFSheet sheet, List<E> list, List<Object[]> annotationList,
        Map<String, CellStyle> styles, int rownum) {
        for (E e : list) {
            int colunm = 0;
            Row row = addRow(sheet, rownum++);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField)os[0];
                Object val = null;
                // Get entity value
                try {
                    if (StringUtils.isNotBlank(ef.value())) {
                        val = Reflections.invokeGetter(e, ef.value());
                    } else {
                        if (os[1] instanceof Field) {
                            val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
                        } else if (os[1] instanceof Method) {
                            val =
                                Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
                        }
                    }
                } catch (Exception ex) {
                    // Failure to ignore
                    log.info(ex.toString());
                    val = "";
                }
                this.addCell(row, colunm++, val, ef.align(), ef.fieldType(), styles);
                sb.append(val + ", ");
            }
            log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb
     *            工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>(1024);

        CellStyle style = wb.createCellStyle();
        // style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        // style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short)16);
        // titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        // style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setAlignment(HorizontalAlignment.LEFT);
        // style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        Font noteFont = wb.createFont();
        noteFont.setFontName("Arial");
        // noteFont.setColor(HSSFColor.RED.index);
        noteFont.setColor(IndexedColors.RED.getIndex());
        // 字体高度
        noteFont.setFontHeightInPoints((short)10);
        style.setFont(noteFont);
        styles.put("note", style);

        style = wb.createCellStyle();
        // style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short)12);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setWrapText(true);
        // style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short)12);
        // headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一行
     *
     * @return 行对象
     */
    private Row addRow(SXSSFSheet sheet, int rownum) {
        return sheet.createRow(rownum);
    }


    /**
     * 添加一个单元格
     *
     * @param row 添加的行
     * @param column 添加列号
     * @param val 添加值
     * @param align 对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    private Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType,
        Map<String, CellStyle> styles) {
        Cell cell = row.createCell(column);
        CellStyle style = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
        try {
            if (val == null) {
                cell.setCellValue("");
            } else if (val instanceof String) {
                cell.setCellValue((String)val);
            } else if (val instanceof Integer) {
                cell.setCellValue((Integer)val);
            } else if (val instanceof Long) {
                cell.setCellValue((Long)val);
            } else if (val instanceof Double) {
                cell.setCellValue((Double)val);
            } else if (val instanceof Float) {
                cell.setCellValue((Float)val);
            } else if (val instanceof Date) {
                DataFormat format = wb.createDataFormat();
                style.setDataFormat(format.getFormat("yyyy-MM-dd"));
                cell.setCellValue((Date)val);
            } else {
                if (fieldType != Class.class) {
                    cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
                } else {
                    cell.setCellValue((String)Class
                        .forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                            "fieldtype." + val.getClass().getSimpleName() + "Type"))
                        .getMethod("setValue", Object.class).invoke(null, val));
                }
            }
        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 输出数据流
     *
     * @param os
     *            输出数据流
     */
    public ExportExcelUtil write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName
     *            输出文件名
     */
    public ExportExcelUtil write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition",
            "attachment; filename=" + URLEncoder.encode(fileName, DEFAULT_URL_ENCODING));
        write(response.getOutputStream());
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName
     *            输出文件名
     */
    public ExportExcelUtil writeExcle(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        //配置返回response参数
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String exportName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xlsx");
        write(response.getOutputStream());
        return this;
    }
    
    /**
     * 输出到文件
     *
     * @param name
     *            输出文件名
     */
    public ExportExcelUtil writeFile(String name) throws FileNotFoundException, IOException {
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExportExcelUtil dispose() {
        wb.dispose();
        return this;
    }
}
