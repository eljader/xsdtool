package ru.jader.xsdtool.gui.control.features.span;

import java.util.List;

import javafx.scene.control.TablePosition;

public interface SpreadsheetSpanHelper {
	public boolean isMerged(TablePosition<?, ?> cell);
	public void merge(List<TablePosition<?, ?>> cells);
	public void split(TablePosition<?, ?> cell);
}
