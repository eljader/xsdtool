package ru.jader.xsdtool.parser.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.jader.xsdtool.parser.model.XsdUnit;

public final class XlsResultHandler implements ParseHandler {

    private static short STYLE_HEADER = 0;
    private static short STYLE_TYPE = 2;
    private static short STYLE_LONG_TEXT = 3;

    private OutputStream writer;
    private Workbook workbook;
    private Sheet sheet;
    private Map<Short, CellStyle> styles;
    private int rowNum;

    public XlsResultHandler(OutputStream writer) throws FileNotFoundException {
        this.writer = writer;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        styles = createStyles();
        rowNum = 0;
        createHeader();
    }

    public void handle(XsdUnit unit) {
        Row row = sheet.createRow(getRowNum());

        addValue(row, 0, null, STYLE_LONG_TEXT);
        addValue(row, 1, null, STYLE_TYPE);
        addValue(row, 2, null, STYLE_LONG_TEXT);
        addValue(row, 3, unit.getPath(),        STYLE_LONG_TEXT);
        addValue(row, 4, unit.getType(),        STYLE_TYPE);
        addValue(row, 5, unit.getDescription(), STYLE_LONG_TEXT);
    }

    public void finalize() throws ParseHandlerException {
        try {
            workbook.write(writer);
            writer.flush();
        } catch (IOException e) { throw new ParseHandlerException(e); }
    }

    private int getRowNum() {
        int rowNum = this.rowNum;
        this.rowNum++;
        return rowNum;
    }

    private void addValue(Row row, int cellNum, String value, short style) {
        Cell cell = row.createCell(cellNum);
        cell.setCellStyle(this.styles.get(style));
        cell.setCellValue(value);
    }

    private Map<Short, CellStyle> createStyles() {
        Map<Short, CellStyle> map = new HashMap<Short, CellStyle>();

        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle header = addBorder(workbook.createCellStyle(),  CellStyle.BORDER_MEDIUM);
        header.setFont(bold);
        header.setAlignment(CellStyle.ALIGN_CENTER);
        header.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        map.put(STYLE_HEADER, header);

        CellStyle longText = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
        longText.setAlignment(CellStyle.ALIGN_LEFT);
        longText.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        longText.setWrapText(true);
        map.put(STYLE_LONG_TEXT, longText);

        CellStyle type = addBorder(workbook.createCellStyle(), CellStyle.BORDER_THIN);
        type.setAlignment(CellStyle.ALIGN_CENTER);
        type.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        map.put(STYLE_TYPE, type);

        return map;
    }

    private CellStyle addBorder(CellStyle style, short borderStyle) {
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        return style;
    }

    private void createHeader() {
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,2));
        sheet.addMergedRegion(new CellRangeAddress(0,0,3,5));

        getHeader().forEach(headerRow -> {
            Row row = sheet.createRow(getRowNum());
            headerRow.forEach(cell -> {
                addValue(row, cell.getColNum(), cell.getTitle(), cell.getStyle());
                if(cell.getWidth() != null)
                    sheet.setColumnWidth(cell.getColNum(), cell.getWidth());
            });
        });
    }

    private List<List<HeaderCell>> getHeader() {
        List<List<HeaderCell>> header = new ArrayList<List<HeaderCell>>();
        List<HeaderCell> baseRow = new ArrayList<HeaderCell>();
        List<HeaderCell> headRow = new ArrayList<HeaderCell>();

        baseRow.add(new HeaderCell("from", 0, STYLE_HEADER, null));
        baseRow.add(new HeaderCell("",     1, STYLE_HEADER, null));
        baseRow.add(new HeaderCell("",        2, STYLE_HEADER, null));
        baseRow.add(new HeaderCell("to",   3, STYLE_HEADER, null));
        baseRow.add(new HeaderCell("",     4, STYLE_HEADER, null));
        baseRow.add(new HeaderCell("",     5, STYLE_HEADER, null));

        headRow.add(new HeaderCell("element",     0, STYLE_HEADER, 15000));
        headRow.add(new HeaderCell("type",        1, STYLE_HEADER, 6000));
        headRow.add(new HeaderCell("description", 2, STYLE_HEADER, 10000));
        headRow.add(new HeaderCell("element",     3, STYLE_HEADER, 15000));
        headRow.add(new HeaderCell("type",        4, STYLE_HEADER, 6000));
        headRow.add(new HeaderCell("description", 5, STYLE_HEADER, 10000));

        header.add(baseRow);
        header.add(headRow);

        return header;
    }

    private class HeaderCell {
        private String title;
        private int colNum;
        private short style;
        private Integer width;

        public HeaderCell(String title, int colNum, short style, Integer width) {
            this.title = title;
            this.colNum = colNum;
            this.style = style;
            this.width = width;
        }

        public String getTitle() { return title; }

        public int getColNum() { return colNum; }

        public short getStyle() { return style; }

        public Integer getWidth() { return width; }
    }
}
