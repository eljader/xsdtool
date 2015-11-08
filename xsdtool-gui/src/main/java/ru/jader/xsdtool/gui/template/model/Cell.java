package ru.jader.xsdtool.gui.template.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cell")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cell {

    @XmlAttribute private Integer row;
    @XmlAttribute private Integer col;
    @XmlAttribute private String value;
    @XmlAttribute private String type;
    @XmlAttribute private Integer rowSpan;
    @XmlAttribute private Integer colSpan;
    @XmlAttribute private Integer actualRow;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getActualRow() {
        return actualRow;
    }

    public void setActualRow(Integer actualRow) {
        this.actualRow = actualRow;
    }
}
