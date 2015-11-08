package ru.jader.xsdtool.gui.control.features.span;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.control.spreadsheet.SpreadsheetView.SpanType;

public final class ComputationSpanHelper implements SpreadsheetSpanHelper {

    private SpreadsheetView spreadsheet;

    public ComputationSpanHelper(SpreadsheetView spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    @Override
    public boolean isMerged(TablePosition<?, ?> cell) {
        int row = cell.getRow();
        int col = cell.getColumn();
        Grid grid = getGrid();
        SpanType type = grid.getSpanType(spreadsheet, row, col);

        return
            type == SpanType.BOTH_INVISIBLE        ? true:
            type == SpanType.COLUMN_SPAN_INVISIBLE ? true:
            type == SpanType.ROW_SPAN_INVISIBLE    ? true:
            type == SpanType.ROW_VISIBLE           ? true:
            (
                type == SpanType.NORMAL_CELL &&
                SpanType.COLUMN_SPAN_INVISIBLE == grid.getSpanType(spreadsheet, row, col + 1)
            )									   ? true:
            false
       ;
    }

    @Override
    public void merge(List<TablePosition<?, ?>> cells) {
        TablePosition<?, ?> baseCell = cells.get(0);
        Grid grid = getGrid();

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
    }

    @Override
    public void split(TablePosition<?, ?> cell) {
        Grid grid = getGrid();
        resetSpan(grid, cell);
        restoreCells(grid, cell);
    }

    private Grid getGrid() {
        return spreadsheet.getGrid();
    }

    private void resetSpan(Grid grid, TablePosition<?, ?> cell) {
        int row = cell.getRow();
        int col = cell.getColumn();

        grid.spanRow(1, row, col);
        grid.spanColumn(1, row, col);
    }

    private void restoreCells(Grid grid, TablePosition<?, ?> baseCell) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
        int baseRow = baseCell.getRow();
        int baseCol = baseCell.getColumn();

        for(int row = baseRow, cellsFound = 0; row < rows.size(); row++, cellsFound = 0) {
            for(int col = 0; col < rows.get(row).size(); col++) {
                SpreadsheetCell cell = rows.get(row).get(col);
                if(baseRow == cell.getRow() && baseCol == cell.getColumn()) {
                    rows.get(row).remove(col);
                    rows.get(row).add(col, SpreadsheetCellType.STRING.createCell(row, col, 1, 1, new String()));
                    cellsFound++;
                }
            }

            if(cellsFound == 0) break;
        }
    }
}
