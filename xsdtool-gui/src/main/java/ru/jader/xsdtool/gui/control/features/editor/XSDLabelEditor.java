package ru.jader.xsdtool.gui.control.features.editor;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.stage.WindowEvent;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import ru.jader.xsdtool.gui.control.features.cell.XSDLabelType;
import ru.jader.xsdtool.gui.control.features.cell.XSDLabelType.XSDComponentLabel;

public final class XSDLabelEditor extends SpreadsheetEditor {

	private final SpreadsheetCellHelper helper = new SpreadsheetCellHelper();

	@Override
	protected void initializeSources() {
    	this.getSourceRegistry().add(new MenuItemSource(
			"Cast Cell to String",
			new CellToStringHandler(),
			new CellToStringAccessMenuItemHandler()
		));
    	this.getSourceRegistry().add(new MenuItemSource(
			"Cast Cell to XSD Component Label",
			new CellToXsdComponentLabelHandler(),
			new CellToXsdLabelAccessMenuItemHandler()
		));
    }

    private class CellToXsdComponentLabelHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			helper.replaceCell(
				getSelectedCell(),
				new XSDLabelType(),
				XSDComponentLabel.stringValues().get(0)
			);
		}
    }

    private class CellToStringHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			helper.replaceCell(
				getSelectedCell(),
				SpreadsheetCellType.STRING,
				new String()
			);
		}
    }

    private class CellToStringAccessMenuItemHandler extends AccessMenuItemHandler {

    	public void handle(WindowEvent event) {
			item.setDisable(isNotAvailable());
		}

		private boolean isNotAvailable() {
			if(getSelectedCells().size() > 1)
				return true;

			return
				helper
					.isXsdLabel(
						getSelectedCell().getRow(),
						getSelectedCell().getColumn()
					) ?
						false : true
			;
		}
	}

    private class CellToXsdLabelAccessMenuItemHandler extends AccessMenuItemHandler {

    	public void handle(WindowEvent event) {
    		item.setDisable(isNotAvailable());
		}

		private boolean isNotAvailable() {
			TablePosition<?, ?> position = getSelectedCell();

			if(getSelectedCells().size() > 1)
				return true;

			if(helper.isXsdLabel(position.getRow(), position.getColumn()))
				return true;

			if(helper.isNotSameRow(position.getRow()))
				return true;

			return false;
		}
	}

    private final class SpreadsheetCellHelper {

    	public boolean isXsdLabel(int row, int col) {
    		return
				getSpreadsheet()
					.getGrid()
					.getRows()
					.get(row)
					.get(col)
					.getCellType()
						instanceof XSDLabelType
    		;
    	}

    	public boolean isNotSameRow(int cellRowNum) {
    		ObservableList<SpreadsheetCell> cellRow = getSpreadsheet().getGrid().getRows().get(cellRowNum);
			for(SpreadsheetCell cell : cellRow)
				if(isXsdLabel(cell.getRow(), cell.getColumn()))
					return false;

			ObservableList<ObservableList<SpreadsheetCell>> rows = getSpreadsheet().getGrid().getRows();
			for(ObservableList<SpreadsheetCell> row : rows)
    			for(SpreadsheetCell cell : row)
    				if(isXsdLabel(cell.getRow(), cell.getColumn()))
    					return true;

    		return false;
    	}

    	private void replaceCell(TablePosition<?, ?> position, SpreadsheetCellType<?> type, Object value) {
    		ObservableList<ObservableList<SpreadsheetCell>> rows = getSpreadsheet().getGrid().getRows();
			int row = position.getRow();
			int col = position.getColumn();

			SpreadsheetCell newCell = new SpreadsheetCellBase(row, col, 1, 1, type);
			newCell.setItem(value);

			rows.get(row).remove(col);
			rows.get(row).add(col, newCell);

			refreshView();
    	}
    }
}
