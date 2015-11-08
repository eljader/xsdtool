package ru.jader.xsdtool.gui.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ru.jader.xsdtool.gui.control.features.cell.XSDLabelType;
import ru.jader.xsdtool.gui.template.model.Cell;
import ru.jader.xsdtool.gui.template.model.CellsWrapper;
import ru.jader.xsdtool.gui.template.model.Template;

public final class DefaultTemplateManager implements TemplateManager {

    private SpreadsheetView context;
    private File file;

    public DefaultTemplateManager(SpreadsheetView context, File file) {
        this.context = context;
        this.file = file;
    }

    @Override
    public void write() throws TemplateManagerException {
        try {
            Template template = new Template(new CellsWrapper(
                toModelCells(context.getGrid().getRows())
            ));

            JAXBContext
                .newInstance(Template.class)
                .createMarshaller()
                .marshal(template, file)
            ;
        } catch (JAXBException e) {
            throw new TemplateManagerException(e);
        }
    }

    @Override
    public void read() throws TemplateManagerException {
        try {
            Template template = (Template)
                JAXBContext
                    .newInstance(Template.class)
                    .createUnmarshaller()
                    .unmarshal(file)
            ;

            Grid grid = new GridBase(
                context.getGrid().getRowCount(),
                context.getGrid().getColumnCount()
            );

            grid.setRows(
                toSpreadsheetCells(
                    template.getCellsWrapper().getCells()
                )
            );

            context.setGrid(grid);
        } catch (JAXBException e) {
            throw new TemplateManagerException(e);
        }
    }

    private List<Cell> toModelCells(ObservableList<ObservableList<SpreadsheetCell>> rows) {
        List<Cell> cells = new ArrayList<Cell>();
        for(int row = 0; row < rows.size(); row++) {
            for(SpreadsheetCell fromCell : rows.get(row)) {
                Cell toCell = new Cell();

                toCell.setRow(fromCell.getRow());
                toCell.setCol(fromCell.getColumn());
                toCell.setRowSpan(fromCell.getRowSpan());
                toCell.setColSpan(fromCell.getColumnSpan());
                toCell.setActualRow(row);
                toCell.setType(fromCell.getCellType().getClass().getSimpleName());
                toCell.setValue(fromCell.getText());

                cells.add(toCell);
            }
        }

        return cells;
    }

    private ObservableList<ObservableList<SpreadsheetCell>> toSpreadsheetCells(List<Cell> cells)
        throws TemplateManagerException
    {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for(Cell fromCell : cells) {
            if(fromCell.getActualRow() >= rows.size())
                rows.add(FXCollections.observableArrayList());

            SpreadsheetCell toCell = new SpreadsheetCellBase(
                fromCell.getRow(),
                fromCell.getCol(),
                fromCell.getRowSpan(),
                fromCell.getColSpan(),
                getCellType(fromCell.getType())
            );

            toCell.setItem(fromCell.getValue());

            rows
                .get(fromCell.getActualRow())
                .add(fromCell.getCol(), toCell)
            ;
        }

        return rows;
    }

    private SpreadsheetCellType<?> getCellType(String typeName) throws TemplateManagerException {
        if("StringType".equals(typeName))
            return SpreadsheetCellType.STRING;

        if("XSDLabelType".equals(typeName))
            return new XSDLabelType();

        throw new TemplateManagerException(String.format("unsupported cell type %s", typeName));
    }
}
