package ru.jader.xsdtool.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsResultHandler implements Handler {
	
	private static short STYLE_HEADER = 0;
	private static short STYLE_TYPE = 2;
	private static short STYLE_LONG_TEXT = 3;
	
	private FileOutputStream writer; 
	private Workbook workbook;
	private Sheet sheet;
	private Map<Short, CellStyle> style;
	private int rowNum;
		
	public XlsResultHandler(String filename) throws FileNotFoundException {
		writer = new FileOutputStream(filename);
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet();
		style = getStyles();
		rowNum = 0;
		createHeader();
	}
	
	public void handle(XsdUnit unit) {
		Row row = sheet.createRow(getRowNum());
		
		addValue(row, 0, "", STYLE_LONG_TEXT);
		addValue(row, 1, "", STYLE_TYPE);
		addValue(row, 2, "", STYLE_LONG_TEXT);
		
		addValue(row, 3, unit.getXpath(),       STYLE_LONG_TEXT);
		addValue(row, 4, unit.getType(),        STYLE_TYPE);
		addValue(row, 5, unit.getDescription(), STYLE_LONG_TEXT);
	}
	
	public void close() throws Exception {
		workbook.write(writer);
		writer.close();
	}
	
	private Map<Short, CellStyle> getStyles() {
		Map<Short, CellStyle> map = new HashMap<Short, CellStyle>();
		
		Font bold = workbook.createFont();
		bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle header = addBorder(workbook.createCellStyle(),  CellStyle.BORDER_MEDIUM);
		header.setFont(bold);
		header.setAlignment(CellStyle.ALIGN_CENTER);
		header.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		map.put(STYLE_HEADER, header);
		
		CellStyle longText = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
		longText.setAlignment(CellStyle.ALIGN_LEFT);
		longText.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		longText.setWrapText(true);
		map.put(STYLE_LONG_TEXT, longText);
		
		CellStyle type = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
		type.setAlignment(CellStyle.ALIGN_CENTER);
		type.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		map.put(STYLE_TYPE, type);
		
		return map;
	}
	
	private int getRowNum() {
		int rowNum = this.rowNum;
		this.rowNum++;
		return rowNum;
	}
	
	private CellStyle addBorder(CellStyle style, short borderStyle) {
		style.setBorderBottom(borderStyle);
		style.setBorderTop(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
		
		return style;
	}
	
	private void createHeader() {
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,2));
		sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));
		
		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 15000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 10000);
		
		Row baseRow = sheet.createRow(getRowNum());
		Row headRow = sheet.createRow(getRowNum());
		
		addValue(baseRow, 0, "From",        STYLE_HEADER);
		addValue(baseRow, 1, "",            STYLE_HEADER);
		addValue(baseRow, 2, "",            STYLE_HEADER);
		addValue(baseRow, 3, "To",          STYLE_HEADER);
		addValue(baseRow, 4, "",            STYLE_HEADER);
		addValue(baseRow, 5, "",            STYLE_HEADER);
		
		addValue(headRow, 0, "element",     STYLE_HEADER);
		addValue(headRow, 1, "type",        STYLE_HEADER);
		addValue(headRow, 2, "description", STYLE_HEADER);
		
		addValue(headRow, 3, "element",     STYLE_HEADER);
		addValue(headRow, 4, "type",        STYLE_HEADER);
		addValue(headRow, 5, "description", STYLE_HEADER);
	}
	
	private void addValue(Row row, int cellNum, String value, short style) {
		Cell cell = row.createCell(cellNum);
		cell.setCellStyle(this.style.get(style));
		cell.setCellValue(value);
	}
}
