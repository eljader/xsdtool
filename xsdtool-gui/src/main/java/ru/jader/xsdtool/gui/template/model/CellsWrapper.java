package ru.jader.xsdtool.gui.template.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cells")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellsWrapper {

    @XmlElement(name = "cell") private List<Cell> cells;

    public CellsWrapper() {
        cells = new ArrayList<Cell>();
    }

    public CellsWrapper(List<Cell> cells) {
        setCells(cells);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
