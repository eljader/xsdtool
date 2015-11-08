package ru.jader.xsdtool.gui.control.features.editor;

import java.util.List;

import ru.jader.xsdtool.gui.control.features.span.SpreadsheetSpanHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.stage.WindowEvent;

public final class MergeCellEditor extends SpreadsheetEditor {

    private SpreadsheetSpanHelper helper;

    public MergeCellEditor(SpreadsheetSpanHelper helper) {
        this.helper = helper;
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
            helper.merge(getSelectedCells());
            refreshView();
        }
    }

    private final class SplitCellsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            helper.split(getSelectedCell());
            refreshView();
        }
    }

    private final class MergeCellsAccessMenuItemHandler extends AccessMenuItemHandler {

        public void handle(WindowEvent event) {
            item.setDisable(
                canMerge(getSelectedCells()) ? false : true
            );
        }

        private boolean canMerge(List<TablePosition<?, ?>> selectedCells) {
            if (selectedCells.size() == 1)
                return false;

            for(int i = 0; i < selectedCells.size() - 1; i++) {
                TablePosition<?, ?> currentPosition = selectedCells.get(i);
                TablePosition<?, ?> nextPosition = selectedCells.get(i+1);

                if(helper.isMerged(currentPosition))
                    return false;

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
                helper.isMerged(getSelectedCell()) ? false : true
            );
        }
    }
}
