package ru.jader.xsdtool.gui.template.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.FIELD)
public class Template {

	@XmlElement(name = "cells") private CellsWrapper cellsWrapper;

	public Template() {}

	public Template(CellsWrapper cellsWrapper) {
		setCellsWrapper(cellsWrapper);
	}

	public CellsWrapper getCellsWrapper() {
		return cellsWrapper;
	}

	public void setCellsWrapper(CellsWrapper cellsWrapper) {
		this.cellsWrapper = cellsWrapper;
	}
}
