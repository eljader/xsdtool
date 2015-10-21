package ru.jader.xsdlib.parser.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import ru.jader.xsdlib.parser.handler.ParseHandler;
import ru.jader.xsdlib.parser.handler.ParseHandlerException;
import ru.jader.xsdlib.parser.handler.XlsDocumentHandler.XlsDocumentTemplate.TemplateCell;
import ru.jader.xsdlib.parser.model.XsdUnit;

public final class XlsDocumentHandler implements ParseHandler {

	private OutputStream writer;
	private XlsDocumentTemplate template;
	private Sheet sheet;
	private List<TemplateCell> xsdLabelCells;
	private int rowOffset = 0;

	public XlsDocumentHandler(OutputStream writer, XlsDocumentTemplate template) {
		this.writer = writer;
		this.template = template;
		this.sheet = template.getWorkbook().createSheet();
		this.xsdLabelCells = template.xsdLabelCells();

		genereteRegularCells(template.getRegularCells());
		mergeCells(template.getListCellRangeAddress());
	}

	public void handle(XsdUnit unit) throws ParseHandlerException {
		try {
			for(TemplateCell fromCell: xsdLabelCells)
	    		createCell(
    				fromCell.getRow() + rowOffset,
    				fromCell.getCol(),
    				(String) XsdUnit.class.getDeclaredMethod(fromCell.getValue()).invoke(unit),
    				fromCell.getStyle()
				)
			;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | IllegalArgumentException
				| SecurityException e) {
			throw new ParseHandlerException(e);
		}

		rowOffset++;
	}

    public void complete() throws ParseHandlerException {
        try {
        	template.getWorkbook().write(writer);
            writer.flush();
        } catch (IOException e) { throw new ParseHandlerException(e); }
    }

    private void mergeCells(List<CellRangeAddress> ranges) {
    	for(CellRangeAddress range: ranges)
    		sheet.addMergedRegion(range);
    }

    private void genereteRegularCells(List<TemplateCell> fromCells) {
    	for(TemplateCell fromCell: fromCells) {
    		Cell toCell = createCell(
				fromCell.getRow(),
				fromCell.getCol(),
				fromCell.getValue(),
				fromCell.getStyle()
			);

    		if(fromCell.getWidth() != null)
    			sheet.setColumnWidth(fromCell.getCol(), fromCell.getWidth());

    		if(fromCell.getHeight() != null)
    			toCell.getRow().setHeight(fromCell.getHeight());
    	}
    }

    private Cell createCell(int rowNum, int colNum, String value, CellStyle style) {
    	Row row = sheet.getRow(rowNum);
		if(row == null)
			row = sheet.createRow(rowNum);

    	Cell cell = row.getCell(colNum);
		if(cell == null)
			cell = row.createCell(colNum);

		cell.setCellStyle(style);
		cell.setCellValue(value);

		return cell;
    }

    public interface XlsDocumentTemplate {
    	public Workbook getWorkbook();
    	public List<CellRangeAddress> getListCellRangeAddress();
    	public List<TemplateCell> getRegularCells();
    	public List<TemplateCell> xsdLabelCells();

        public class TemplateCell {
        	private int row;
        	private int col;
        	private String value;
        	private CellStyle style;
        	private Integer width = null;
        	private Short height = null;

    		public TemplateCell(int row, int col, String value, CellStyle style) {
    			this.row = row;
    			this.col = col;
    			this.value = value;
    			this.style = style;
    		}

    		public TemplateCell(int row, int col, String value, CellStyle style, Integer width, Short height) {
    			this.row = row;
    			this.col = col;
    			this.value = value;
    			this.style = style;
    			this.width = width;
    			this.height = height;
    		}

    		public int getRow() { return row; }
    		public int getCol() { return col; }
    		public String getValue() { return value; }
    		public CellStyle getStyle() { return style; }
			public Integer getWidth() { return width; }
			public Short getHeight() { return height; }
        }
    }
}
