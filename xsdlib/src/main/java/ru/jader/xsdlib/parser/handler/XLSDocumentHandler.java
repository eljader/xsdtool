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
import ru.jader.xsdlib.parser.handler.XLSDocumentHandler.XLSDocumentSource.SourceCell;
import ru.jader.xsdlib.parser.model.XSDUnit;

public final class XLSDocumentHandler implements ParseHandler {

    private OutputStream writer;
    private XLSDocumentSource source;

    private List<SourceCell> xsdLabelCells;
    private int rowOffset = 0;

    public XLSDocumentHandler(OutputStream writer, XLSDocumentSource source) {
        this.writer = writer;
        this.source = source;
        this.xsdLabelCells = source.getXSDLabelCells();

        genereteRegularCells(source.getRegularCells());
        mergeCells(source.getListCellRangeAddress());
    }

    public void handle(XSDUnit unit) throws ParseHandlerException {
        try {
            for(SourceCell fromCell: xsdLabelCells)
                createCell(
                    fromCell.getRow() + rowOffset,
                    fromCell.getCol(),
                    (String) XSDUnit.class.getDeclaredMethod(fromCell.getValue()).invoke(unit),
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
            source.getWorkbook().write(writer);
            writer.flush();
        } catch (IOException e) { throw new ParseHandlerException(e); }
    }

    private void mergeCells(List<CellRangeAddress> ranges) {
        for(CellRangeAddress range: ranges)
            source.getSheet().addMergedRegion(range);
    }

    private void genereteRegularCells(List<SourceCell> fromCells) {
        for(SourceCell fromCell: fromCells) {
            Cell toCell = createCell(
                fromCell.getRow(),
                fromCell.getCol(),
                fromCell.getValue(),
                fromCell.getStyle()
            );

            if(fromCell.getWidth() != null)
                source.getSheet().setColumnWidth(fromCell.getCol(), fromCell.getWidth());

            if(fromCell.getHeight() != null)
                toCell.getRow().setHeight(fromCell.getHeight());
        }
    }

    private Cell createCell(int rowNum, int colNum, String value, CellStyle style) {
        Row row = source.getSheet().getRow(rowNum);
        if(row == null)
            row = source.getSheet().createRow(rowNum);

        Cell cell = row.getCell(colNum);
        if(cell == null)
            cell = row.createCell(colNum);

        cell.setCellStyle(style);
        cell.setCellValue(value);

        return cell;
    }

    public interface XLSDocumentSource {
        public Workbook getWorkbook();
        public Sheet getSheet();
        public List<CellRangeAddress> getListCellRangeAddress();
        public List<SourceCell> getRegularCells();
        public List<SourceCell> getXSDLabelCells();

        public class SourceCell {
            private int row;
            private int col;
            private String value;
            private CellStyle style;
            private Integer width = null;
            private Short height = null;

            public SourceCell(int row, int col, String value, CellStyle style) {
                this.row = row;
                this.col = col;
                this.value = value;
                this.style = style;
            }

            public SourceCell(int row, int col, String value, CellStyle style, Integer width, Short height) {
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

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + col;
                result = prime * result + row;
                result = prime * result
                        + ((value == null) ? 0 : value.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                SourceCell other = (SourceCell) obj;
                if (col != other.col)
                    return false;
                if (row != other.row)
                    return false;
                if (value == null) {
                    if (other.value != null)
                        return false;
                } else if (!value.equals(other.value))
                    return false;
                return true;
            }
        }
    }
}
