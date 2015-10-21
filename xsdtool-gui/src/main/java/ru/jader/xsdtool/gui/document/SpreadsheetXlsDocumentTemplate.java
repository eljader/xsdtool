package ru.jader.xsdtool.gui.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ru.jader.xsdlib.parser.handler.XlsDocumentHandler.XlsDocumentTemplate;
import ru.jader.xsdtool.gui.control.features.editor.SpreadsheetEditor;
import ru.jader.xsdtool.gui.control.features.editor.XsdLabelEditor.XsdComponentLabel;

public final class SpreadsheetXlsDocumentTemplate implements XlsDocumentTemplate {

    private static short STYLE_BOLD_CENTER = 0;
    private static short STYLE_TYPE = 2;
    private static short STYLE_LONG_TEXT = 3;

    private List<List<TablePosition<?, ?>>> mergedCells;
	private List<TablePosition<?, ?>> xsdLables;
	private SpreadsheetView documentEditor;

	private Workbook workbook = new XSSFWorkbook();
	private Map<Short, CellStyle> styleMap = createStyles();

	public SpreadsheetXlsDocumentTemplate(
		SpreadsheetView documentEditor,
		SpreadsheetEditor mergeCellEditor,
		SpreadsheetEditor xsdLabelEditor
	) {
		this.mergedCells = getMergedCells(mergeCellEditor);
		this.xsdLables = getXsdLables(xsdLabelEditor);
		this.documentEditor = documentEditor;
	}

	@SuppressWarnings("unchecked")
	private List<List<TablePosition<?, ?>>> getMergedCells(SpreadsheetEditor mergeCellEditor) {
		return (List<List<TablePosition<?, ?>>>) mergeCellEditor.getTarget();
	}

	@SuppressWarnings("unchecked")
	private List<TablePosition<?, ?>> getXsdLables(SpreadsheetEditor xsdLabelEditor) {
		return (List<TablePosition<?, ?>>) xsdLabelEditor.getTarget();
	}

	private Grid getGrid() {
		return documentEditor.getGrid();
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public List<CellRangeAddress> getListCellRangeAddress() {
		List<CellRangeAddress> result = new ArrayList<CellRangeAddress>(mergedCells.size());

		for(List<TablePosition<?, ?>> mergedCell : mergedCells) {
			TablePosition<?, ?> baseCell = mergedCell.get(0);

			int firstRow, lastRow, firstCol, lastCol;
			firstRow = lastRow = baseCell.getRow();
			firstCol = lastCol = baseCell.getColumn();

			for(TablePosition<?, ?> position : mergedCell) {
				if(lastRow < position.getRow()) lastRow = position.getRow();
				if(lastCol < position.getColumn()) lastCol = position.getColumn();
			}

			result.add(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
		}

		return result;
	}

	public List<TemplateCell> getRegularCells() {
		List<TemplateCell> result = new ArrayList<TemplateCell>();
		CellStyle style = styleMap.get(STYLE_BOLD_CENTER);
		ObservableList<SpreadsheetColumn> columns = documentEditor.getColumns();

		for(List<SpreadsheetCell> row : getGrid().getRows())
			for(SpreadsheetCell cell : row)
				if(!cell.getText().isEmpty()) {
					System.out.println(new Double(getGrid().getRowHeight(cell.getRow())).shortValue());
					result.add(
						new TemplateCell(
							cell.getRow(),
							cell.getColumn(),
							cell.getText(),
							style,
							new Double(columns.get(cell.getColumn()).getWidth()).intValue() * 30,
							new Double(getGrid().getRowHeight(cell.getRow())).shortValue()
						)
					);

					result.addAll(
						getMergedEmptyCells(
							cell.getRow(),
							cell.getColumn(),
							style
						)
					);
				}
		;

		return result;
	}

	private List<TemplateCell> getMergedEmptyCells(int row, int col, CellStyle style) {
		List<TemplateCell> result = new ArrayList<TemplateCell>();
		int baseCellPosition = 0;

		for(List<TablePosition<?, ?>> mergedCell : mergedCells) {
			TablePosition<?, ?> baseCell = mergedCell.get(baseCellPosition);

			if(baseCell.getRow() == row && baseCell.getColumn() == col) {

				for(int position = baseCellPosition + 1; position < mergedCell.size(); position++) {
					result.add(
						new TemplateCell(
							mergedCell.get(position).getRow(),
							mergedCell.get(position).getColumn(),
							null,
							style
						)
					);
				}
			}
		}

		return result;
	}

	public List<TemplateCell> xsdLabelCells() {
		List<TemplateCell> result = new ArrayList<TemplateCell>();

        for(TablePosition<?, ?> position : xsdLables) {
        	SpreadsheetCell cell = getGrid().getRows().get(position.getRow()).get(position.getColumn());
        	String dataFetchMethod = null;
        	CellStyle style = null;

			switch (XsdComponentLabel.get(cell.getText())) {
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

        	result.add(new TemplateCell(cell.getRow(), cell.getColumn(), dataFetchMethod, style));
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
