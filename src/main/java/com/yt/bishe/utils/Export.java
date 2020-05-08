package com.yt.bishe.utils;

import com.yt.bishe.entity.ChidOrder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class Export  {


    public void exportOrders(List<ChidOrder> chidOrders, HttpServletResponse response)throws IOException{
        File file = new File("订单导出表.xlsx");
        Workbook workbook =new XSSFWorkbook();

        Sheet sheet=workbook.createSheet("订单表");
        int index=0;
        Row row0=sheet.createRow(index++);
        //设置第一行
        row0.createCell(0).setCellValue("所属主订单编号");
        row0.createCell(1).setCellValue("编号");
        row0.createCell(2).setCellValue("图书名称");
        row0.createCell(3).setCellValue("书本ID");
        row0.createCell(4).setCellValue("图书单价");
        row0.createCell(5).setCellValue("购买数量");
        //把查询结果放入到对应的列
        for (ChidOrder ChidOrder:chidOrders) {
            Row row=sheet.createRow(index++);
            row.createCell(0).setCellValue(ChidOrder.getpOrderId());
            row.createCell(1).setCellValue(ChidOrder.getChidOrderId());
            row.createCell(2).setCellValue(ChidOrder.getBookName());
            row.createCell(3).setCellValue(ChidOrder.getBookId());
            row.createCell(4).setCellValue(ChidOrder.getPrice());
            row.createCell(5).setCellValue(ChidOrder.getCount());
        }


        response.reset();
        response.setHeader("Content-Disposition","attachment;filename=\""+new String(file.getName().getBytes("utf-8"),"ISO8859-1")+"\"");
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream os=new BufferedOutputStream(response.getOutputStream());
        workbook.write(os);
        os.flush();
        os.close();


    }


}
