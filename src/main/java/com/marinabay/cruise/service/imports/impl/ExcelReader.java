package com.marinabay.cruise.service.imports.impl;

import com.marinabay.cruise.model.Schedules;
import com.marinabay.cruise.service.imports.AbstractReader;
import com.marinabay.cruise.service.imports.Reader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader extends AbstractReader<Schedules> {

	@Override
	public List<String> getExtension() {
		List<String> extension = new ArrayList<String>(3);
		extension.add("xls");
		extension.add("xlsx");
		return extension;
	}

    public Workbook getWorkBook(InputStream is) throws Exception {
        if ("xlsx".equals(this.extension)) {
            return new XSSFWorkbook(is);
        }
        return new HSSFWorkbook(is);
    }

	@Override
	public List<Schedules> getData(InputStream is) throws Exception {
		List<Schedules> data = new ArrayList<Schedules>();
        Workbook workBook = getWorkBook(is);

		Sheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();

		while (rows.hasNext()) {
			Row row = rows.next();
            Schedules c = new Schedules();
			//System.out.println("Row No.: " + row.getRowNum());
            if (row.getRowNum() < 1) {
                //currently ignore header row
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();

				//System.out.println("Cell No.: " + cell.getColumnIndex());
				switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC: {

                        // cell type numeric.
                        System.out.println("Numeric value: " + cell.getNumericCellValue());
                        c.setPassengers(Double.valueOf(cell.getNumericCellValue()).intValue());
                        break;
                    }

                    case HSSFCell.CELL_TYPE_STRING: {

                        // cell type string.
                        RichTextString richTextString = cell.getRichStringCellValue();
                        c.setData(cell.getColumnIndex(), richTextString.getString());
                        //System.out.println("String value: " + richTextString.getString());

                        break;
                    }

                    default: {

                        // types other than String and Numeric.
                        System.out.println("Type not supported.");

                        break;
                    }
				}
			}
            if (!StringUtils.isEmpty(c.getCruiseName())) {
                data.add(c);
            }
        }
		return data;
	}
	
	public static List<Schedules> getEndResults(InputStream is, String fileName) throws Exception {
		Reader<Schedules> rd = new ExcelReader();
		return rd.getResults(is, fileName);
	}

}
