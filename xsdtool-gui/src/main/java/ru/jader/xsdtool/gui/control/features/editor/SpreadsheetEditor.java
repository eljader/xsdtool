package ru.jader.xsdtool.gui.control.features.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TablePosition;
import javafx.stage.WindowEvent;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

abstract public class SpreadsheetEditor {

    private SpreadsheetView spreadsheet;
    private List<MenuItemSource> sourceRegistry = new ArrayList<MenuItemSource>();

    public SpreadsheetEditor initialize(SpreadsheetView spreadsheet) {
        this.spreadsheet = spreadsheet;
        this.initializeSources();
        this.registerSources();
        return this;
    }

    private void registerSources() {
        ContextMenu menu = this.getSpreadsheet().getContextMenu();

        for(MenuItemSource source : this.getSourceRegistry())
            this.addMenuItem(menu, source);

        this.getSpreadsheet().setContextMenu(menu);
    }

    private void addMenuItem(ContextMenu menu, MenuItemSource source) {
        MenuItem item = new MenuItem(source.getTitle());
        item.setOnAction(source.getActionHandler());

        menu.addEventHandler(WindowEvent.WINDOW_SHOWING, source.getAccsessHandler().initialize(item));
        menu.getItems().add(item);
    }

    protected final class MenuItemSource {
        private String title;
        private EventHandler<ActionEvent> actionHandler;
        private AccessMenuItemHandler accsessHandler;

        public MenuItemSource(String title,
                EventHandler<ActionEvent> actionHandler,
                AccessMenuItemHandler accsessHandler) {
            super();
            this.title = title;
            this.actionHandler = actionHandler;
            this.accsessHandler = accsessHandler;
        }

        public String getTitle() { return title; }

        public EventHandler<ActionEvent> getActionHandler() { return actionHandler; }

        public AccessMenuItemHandler getAccsessHandler() { return accsessHandler; }
    }

    protected abstract class AccessMenuItemHandler implements EventHandler<WindowEvent> {
        protected MenuItem item;

        private AccessMenuItemHandler initialize(MenuItem item) {
            this.item = item;
            return this;
        }
    }

    protected abstract void initializeSources();

    protected SpreadsheetView getSpreadsheet() { return spreadsheet; }

    protected List<MenuItemSource> getSourceRegistry() { return sourceRegistry; }

    protected TablePosition<?, ?> getSelectedCell() {
        return this.getSpreadsheet().getSelectionModel().getSelectedCells().get(0);
    }

    @SuppressWarnings("unchecked")
    protected List<TablePosition<?, ?>> getSelectedCells() {
        return
            new ArrayList<TablePosition<?, ?>>(
                (Collection<? extends TablePosition<?, ?>>) (Object)
                    this.getSpreadsheet().getSelectionModel().getSelectedCells()
            )
        ;
    }

    protected void refreshView() {
        getSpreadsheet().setGrid(getSpreadsheet().getGrid());
    }
}
