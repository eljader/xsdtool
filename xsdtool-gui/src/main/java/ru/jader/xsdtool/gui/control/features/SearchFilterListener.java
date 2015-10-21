package ru.jader.xsdtool.gui.control.features;

import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class SearchFilterListener<T> implements ChangeListener<String> {

	private static final short EVENT_NOT_SELECTED  = 0;
	private static final short EVENT_SET_SELECTED  = 1;
	private static final short EVENT_DROP_SELECTED = 2;

	private T filterSelected;
	private ItemFilter filter = new ItemFilter();
	private short event = EVENT_NOT_SELECTED;
	private String promptText = "Start typing to see the result";

	private ComboBox<T> instance;
	private ObservableList<T> allItems;

	@SuppressWarnings("unchecked")
	public SearchFilterListener(ComboBox<String> instance, ObservableList<String> items) {
		initialize((ComboBox<T>) instance, (ObservableList<T>) items, (StringConverter<T>) new DefaultStringConverter());
	}

	public SearchFilterListener(ComboBox<T> instance, ObservableList<T> items, StringConverter<T> converter) {
		initialize(instance, items, converter);
	}

	private void initialize(ComboBox<T> instance, ObservableList<T> items, StringConverter<T> converter) {
		this.instance = instance;
		this.allItems = FXCollections.observableArrayList(items);

		instance.setEditable(true);
		instance.setConverter(converter);
		instance.setItems(items);
		instance.getEditor().textProperty().addListener(this);
		instance.setPromptText(getPromptText());
	}

	public String getPromptText() {
		return this.promptText;
	}

	public String setPromptText(String text) {
		return this.promptText = text;
	}

	public void changed(
		ObservableValue<? extends String> observable,
		String oldValue,
		String newValue
	) {
		determineEvent();

		if(event == EVENT_DROP_SELECTED) {
			instance.valueProperty().set(null);
			instance.getEditor().setText(oldValue);
		}

		if(event == EVENT_NOT_SELECTED) {
			instance.valueProperty().set(null);
			instance.getEditor().setText(newValue);
			instance.setItems(FXCollections.observableArrayList(allItems));
			instance.getItems().removeIf(filter.init(newValue));
		}

		if(event == EVENT_SET_SELECTED) {
			filterSelected = instance.getValue();
			instance.getEditor().setText(newValue);
		}

		instance.show();
	}

	private void determineEvent() {
		T instanceSelected = instance.getValue();
		boolean isInstanceSelected = instanceSelected != null;
		boolean isInstanceReSelect = isInstanceSelected && !instanceSelected.equals(filterSelected);

		event =
			event == EVENT_NOT_SELECTED && isInstanceSelected ? EVENT_SET_SELECTED  :
			event == EVENT_SET_SELECTED && isInstanceReSelect ? EVENT_SET_SELECTED  :
			event == EVENT_SET_SELECTED                       ? EVENT_DROP_SELECTED :
																EVENT_NOT_SELECTED
		;
	}

	private class ItemFilter implements Predicate<T> {

		private String needle;

		public Predicate<T> init(String needle) {
			this.needle = needle;
			return this;
		}

		public boolean test(T item) {
			boolean needleNotEmpty = (needle != null && !needle.isEmpty());
			String text = instance.getConverter().toString(item).toLowerCase();

			return needleNotEmpty && !text.contains(needle.toLowerCase());
		}
	}
}