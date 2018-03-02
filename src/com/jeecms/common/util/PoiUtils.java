package com.jeecms.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PoiUtils {
	public static void writeToResponse(Workbook wb,String filename,HttpServletResponse response) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((filename).getBytes(), "iso-8859-1"));

        ServletOutputStream out = response.getOutputStream();

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);

            byte[] buff = new byte[2048];
            int bytesRead;

            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
	}
	
	public static Workbook createSheet(Workbook wb,String sheetName,int columnWidth,List<List<String>>list,List<String>header){
		// 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(sheetName);

        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<header.size();i++){
        	sheet.setColumnWidth((short) i, columnWidth);
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        // DataFormat df = wb.createDataFormat();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.RED.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        f2.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 设置第一种单元格的样式
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        // cs.setDataFormat(df.getFormat("#,##0.0"));

        // 设置第二种单元格的样式
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        // cs2.setDataFormat(df.getFormat("text"));

        // 创建列（每行里的单元格）
        Cell cell;
        for(int i=0;i<header.size();i++){
        	cell= row.createCell(i);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(cs);
        }
        
        for (short rowNo = 0; rowNo < list.size(); rowNo++) {

            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            row = sheet.createRow((short) rowNo + 1);
            // 在row行上创建一个方格
            List<String>rowResult=list.get(rowNo);
            for(int cellNo=0;cellNo<rowResult.size();cellNo++){
            	cell = row.createCell(cellNo);
                cell.setCellValue(rowResult.get(cellNo));
                cell.setCellStyle(cs2);
            }
        }
        return wb;
	}
}
