package com.practice;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    public static Object[][] readExcel(String filePath, String sheetName)
            throws IOException {

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheet(sheetName);

        int lastRow = sheet.getLastRowNum();

        Object[][] data = new Object[lastRow][3];

        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= lastRow; i++) {

            Row row = sheet.getRow(i);

            data[i - 1][0] = formatter.formatCellValue(row.getCell(0));
            data[i - 1][1] = formatter.formatCellValue(row.getCell(1));

            data[i - 1][2] = Boolean.parseBoolean(
                    formatter.formatCellValue(row.getCell(2))
            );
        }

        workbook.close();
        fis.close();

        return data;
    }
}
