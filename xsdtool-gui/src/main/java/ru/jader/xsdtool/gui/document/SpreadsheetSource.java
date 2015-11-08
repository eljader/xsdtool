package ru.jader.xsdtool.gui.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType.StringType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ru.jader.xsdlib.parser.handler.XLSDocumentHandler.XLSDocumentSource;
import ru.jader.xsdtool.gui.control.features.cell.XSDLabelType;
import ru.jader.xsdtool.gui.control.features.cell.XSDLabelType.XSDComponentLabel;

public final class SpreadsheetSource implements XLSDocumentSource {

    private static short STYLE_BOLD_CENTER = 0;
    private static short STYLE_TYPE = 2;
    private static short STYLE_LONG_TEXT = 3;

    private static final String SHEET_NAME = "xsdtool-info";

	private SpreadsheetView documentEditor;
	private Workbook workbook;
	private Sheet sheet;
	private Map<Short, CellStyle> styleMap;

	public SpreadsheetSource(Workbook workbook, SpreadsheetView documentEditor) {
		this.workbook = workbook;
		this.documentEditor = documentEditor;

		this.sheet = initSheet();
		this.styleMap = createStyles();
	}

	private Sheet initSheet() {
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		if(sheet == null)
			sheet = workbook.createSheet(SHEET_NAME);
		return sheet;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getSheet() {
		return this.sheet;
	}

	public List<CellRangeAddress> getListCellRangeAddress() {
		ArrayList<CellRangeAddress> result = new ArrayList<CellRangeAddress>();
		ObservableList<ObservableList<SpreadsheetCell>> rows = documentEditor.getGrid().getRows();

		for(int row = 0; row < rows.size(); row++) {
			ObservableList<SpreadsheetCell> cells = rows.get(row);
			for(int col = 0; col < cells.size(); col++) {
				SpreadsheetCell cell = cells.get(col);
				boolean isMerged = cell.getRowSpan() > 1 || cell.getColumnSpan() > 1;
				boolean isBaseCell = cell.getRow() == row && cell.getColumn() == col;

				if(isMerged && isBaseCell)
					result.add(
						new CellRangeAddress(
							cell.getRow(),
							cell.getRow() + cell.getRowSpan() - 1,
							cell.getColumn(),
							cell.getColumn() + cell.getColumnSpan() - 1
						)
					)
				;
			}
		}

		return result;
	}

	public List<SourceCell> getRegularCells() {
		List<SourceCell> result = new ArrayList<SourceCell>();
		CellStyle style = styleMap.get(STYLE_BOLD_CENTER);
		ObservableList<ObservableList<SpreadsheetCell>> rows = documentEditor.getGrid().getRows();

		for(int row = 0; row < rows.size(); row++) {
			ObservableList<SpreadsheetCell> cells = rows.get(row);
			for(int col = 0; col < cells.size(); col++) {
				SpreadsheetCell cell = cells.get(col);
				if(!cell.getText().isEmpty() && cell.getCellType() instanceof StringType) {
					result.add(
						new SourceCell(
							cell.getRow(),
							cell.getColumn(),
							cell.getText(),
							style
						)
					);

					boolean isMerged = cell.getRowSpan() > 1 || cell.getColumnSpan() > 1;
					boolean isBaseCell = cell.getRow() == row && cell.getColumn() == col;

					if(isMerged && isBaseCell)
						result.addAll(createMergedEmptyCells(cell, style));
				}
			}
		}

		return result;
	}

	private List<SourceCell> createMergedEmptyCells(SpreadsheetCell baseCell, CellStyle style) {
		List<SourceCell> result = new ArrayList<SourceCell>();
		int baseRow = baseCell.getRow();
		int baseCol = baseCell.getColumn();
		int lastRow = baseRow + baseCell.getRowSpan() - 1;
		int lastCol = baseCol + baseCell.getColumnSpan() - 1;

		for(int row = baseRow; row <= lastRow; row++) {
			int colStart = baseRow == row ? baseCol + 1 : baseCol;

			for(int col = colStart; col <= lastCol; col++) {
				result.add(new SourceCell(row, col, null, style));
			}
		}

		return result;
	}

	public List<SourceCell> getXSDLabelCells() {
		List<SourceCell> result = new ArrayList<SourceCell>();
		ObservableList<ObservableList<SpreadsheetCell>> rows = documentEditor.getGrid().getRows();

		for(List<SpreadsheetCell> row : rows) {
			for(SpreadsheetCell cell : row) {
				if(cell.getCellType() instanceof XSDLabelType) {
		        	String dataFetchMethod = null;
		        	CellStyle style = null;

					switch (XSDComponentLabel.get(cell.getText())) {
						case NAME:
							dataFetchMethod = "getPath";
							style = styleMap.get(STYLE_LONG_TEXT);
							break;
						case TYPE:
							dataFetchMethod = "getType";
							style = styleMap.get(STYLE_TYPE);
							break;
						case DESCRIPTION:
							dataFetchMethod = "getDescription";
							style = styleMap.get(STYLE_LONG_TEXT);
							break;
						default: throw new RuntimeException(String.format("%s not impemented", cell.getText()));
					}

		        	result.add(new SourceCell(cell.getRow(), cell.getColumn(), dataFetchMethod, style));
				}
			}
		}

		return result;
	}

    private Map<Short, CellStyle> createStyles() {
        Map<Short, CellStyle> map = new HashMap<Short, CellStyle>();
        CellStyle style = null;
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);

        style = addBorder(workbook.createCellStyle(),  CellStyle.BORDER_MEDIUM);
        style.setFont(bold);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        map.put(STYLE_BOLD_CENTER, style);

        style = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        map.put(STYLE_LONG_TEXT, style);

        style = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        map.put(STYLE_TYPE, style);

        return map;
    }

    private CellStyle addBorder(CellStyle style, short borderStyle) {
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        return style;
    }
}
