package ru.jader.xsdtool.gui.control.features.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.stage.WindowEvent;

public final class MergeCellEditor extends SpreadsheetEditor {

	private final SpreadsheetCellHelper cellHelper = new SpreadsheetCellHelper();

	@Override
	public Object getTarget() {
		return new ArrayList<List<TablePosition<?, ?>>>(cellHelper.storedCells.values());
	}

	@Override
	protected void initializeSources() {
		this.getSourceRegistry().add(new MenuItemSource(
			"Merge Cells",
			new MergeCellsHandler(),
			new MergeCellsAccessMenuItemHandler()
		));
		this.getSourceRegistry().add(new MenuItemSource(
			"Split Cell",
			new SplitCellsHandler(),
			new SplitCellsAccessMenuItemHandler()
		));
	}

    private final class MergeCellsHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			getSpreadsheet()
				.setGrid(
					cellHelper.merge(getSpreadsheet().getGrid(), getSelectedCells())
				)
			;
		}
    }

    private final class SplitCellsHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			getSpreadsheet()
				.setGrid(
					cellHelper.split(getSpreadsheet().getGrid(), getSelectedCell())
				)
			;
		}
    }

    private final class MergeCellsAccessMenuItemHandler extends AccessMenuItemHandler {

		public void handle(WindowEvent event) {
			item.setDisable(
				canMerge(getSelectedCells()) ? false : true
			);
		}

		private boolean canMerge(List<TablePosition<?, ?>> selectedCells) {
			TablePosition<?, ?> baseCell = selectedCells.get(0);

			if (selectedCells.size() == 1)
				return false;

			if (cellHelper.isMerged(baseCell))
				return false;

			for(int i = 0; i < selectedCells.size() - 1; i++) {
				TablePosition<?, ?> currentPosition = selectedCells.get(i);
				TablePosition<?, ?> nextPosition = selectedCells.get(i+1);

				if (nextPosition.getRow() - currentPosition.getRow() > 1)
					return false;

				if (nextPosition.getColumn() - currentPosition.getColumn() > 1)
					return false;
			}

			return true;
		}
    }

    private final class SplitCellsAccessMenuItemHandler extends AccessMenuItemHandler {
		public void handle(WindowEvent event) {
			item.setDisable(
				cellHelper.isMerged(getSelectedCell()) ? false : true
			);
		}
    }

    private final class SpreadsheetCellHelper {

    	private Map<String, List<TablePosition<?, ?>>> storedCells = new HashMap<String, List<TablePosition<?, ?>>>();

    	public boolean isMerged(TablePosition<?, ?> cell) {
    		return storedCells.containsKey(cell.toString());
    	}

    	public Grid merge(Grid grid, List<TablePosition<?, ?>> cells) {
    		TablePosition<?, ?> baseCell = cells.get(0);

    		int baseRow, baseCol, maxRow, maxCol;
			baseRow = maxRow = baseCell.getRow();
			baseCol = maxCol = baseCell.getColumn();

			for(TablePosition<?, ?> cell: cells) {
				if (maxRow < cell.getRow()) maxRow = cell.getRow();
				if (maxCol < cell.getColumn()) maxCol = cell.getColumn();
			}

			if(baseRow < maxRow)
				grid.spanRow(maxRow - baseRow + 1, baseRow, baseCol);
			if(baseCol < maxCol)
				grid.spanColumn(maxCol - baseCol + 1, baseRow, baseCol);

			storedCells.put(baseCell.toString(), cells);
	        return grid;
    	}

    	public Grid split(Grid grid, TablePosition<?, ?> cell) {
			resetSpan(grid, cell);
			restoreCells(grid, storedCells.get(cell.toString()));
			storedCells.remove(cell.toString());
			return grid;
    	}

		private void resetSpan(Grid grid, TablePosition<?, ?> cell) {
			int row = cell.getRow();
			int col = cell.getColumn();

			grid.spanRow(1, row, col);
			grid.spanColumn(1, row, col);
		}

		private void restoreCells(Grid grid, List<TablePosition<?, ?>> cells) {
			ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
			cells.remove(0);

			for(TablePosition<?, ?> cell: cells) {
				int row = cell.getRow();
				int col = cell.getColumn();

				rows.get(row).remove(col);
				rows.get(row).add(col, SpreadsheetCellType.STRING.createCell(row, col, 1, 1, new String()));
			}
		}
    }
}
