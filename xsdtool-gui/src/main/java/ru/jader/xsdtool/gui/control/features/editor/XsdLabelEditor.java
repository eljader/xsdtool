package ru.jader.xsdtool.gui.control.features.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.stage.WindowEvent;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType.ListType;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType.StringType;

public final class XsdLabelEditor extends SpreadsheetEditor {

	private final SpreadsheetCellHelper cellHelper = new SpreadsheetCellHelper();

	@Override
	public Object getTarget() {
		return new ArrayList<TablePosition<?, ?>>(cellHelper.storedCells.values());
	}

    public enum XsdComponentLabel {
    	NAME {
    		public String value() { return "#NAME"; }
    	},
    	TYPE {
    		public String value() { return "#TYPE"; }
    	},
    	DESCRIPTION {
    		public String value() { return "#DESCR"; }
    	};

    	public static XsdComponentLabel get(String value) {
    		for(XsdComponentLabel label : XsdComponentLabel.values())
    			if(label.value().equals(value))
    				return label;
    		return null;
    	}

    	public abstract String value();
    }

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

    	private List<String> labelList = new ArrayList<String>(XsdComponentLabel.values().length);

    	public CellToXsdComponentLabelHandler() {
    		for(XsdComponentLabel label : XsdComponentLabel.values())
    			labelList.add(label.value());
    	}

		public void handle(ActionEvent event) {
			getSpreadsheet().setGrid(
				cellHelper.replaceCell(
					getSpreadsheet().getGrid(),
					getSelectedCell(),
					SpreadsheetCellType.LIST(labelList),
					labelList.get(0)
				)
			);
		}
    }

    private class CellToStringHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			getSpreadsheet().setGrid(
				cellHelper.replaceCell(
					getSpreadsheet().getGrid(),
					getSelectedCell(),
					SpreadsheetCellType.STRING,
					new String()
				)
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

			return cellHelper.isXsdLabel(getSelectedCell()) ? false : true;
		}
	}

    private class CellToXsdLabelAccessMenuItemHandler extends AccessMenuItemHandler {

    	public void handle(WindowEvent event) {
    		item.setDisable(isNotAvailable());
		}

		private boolean isNotAvailable() {
			if(getSelectedCells().size() > 1)
				return true;

			if(cellHelper.isXsdLabel(getSelectedCell()))
				return true;

			if(cellHelper.isNotSameRow(getSelectedCell()))
				return true;

			return false;
		}
	}

    private final class SpreadsheetCellHelper {

    	private Map<String, TablePosition<?, ?>> storedCells = new HashMap<String, TablePosition<?, ?>>();

    	public boolean isXsdLabel(TablePosition<?, ?> positionCell) {
    		return storedCells.containsKey(positionCell.toString());
    	}

    	public boolean isNotSameRow(TablePosition<?, ?> positionCell) {
    		for(TablePosition<?, ?> storedPosition: storedCells.values())
    			if(storedPosition.getRow() != positionCell.getRow())
    				return true
    		;

    		return false;
    	}

    	private Grid replaceCell(Grid grid, TablePosition<?, ?> positionCell, SpreadsheetCellType<?> type, Object value) {
			ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
			int row = positionCell.getRow();
			int col = positionCell.getColumn();

			SpreadsheetCell newCell = new SpreadsheetCellBase(row, col, 1, 1, type);
			newCell.setItem(value);

			rows.get(row).remove(col);
			rows.get(row).add(col, newCell);

			if(type instanceof ListType)
				storedCells.put(positionCell.toString(), positionCell);

			if(type instanceof StringType)
				storedCells.remove(positionCell.toString());

			return grid;
    	}
    }
}
