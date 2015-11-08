package ru.jader.xsdtool.gui.control.features.cell;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.spreadsheet.SpreadsheetCellType.ListType;

public class XSDLabelType extends ListType {

    public enum XSDComponentLabel {
        NAME {
            public String value() { return "#NAME"; }
        },
        TYPE {
            public String value() { return "#TYPE"; }
        },
        DESCRIPTION {
            public String value() { return "#DESCR"; }
        };

        public static XSDComponentLabel get(String value) {
            for(XSDComponentLabel label : XSDComponentLabel.values())
                if(label.value().equals(value))
                    return label;
            return null;
        }

        public static List<String> stringValues() {
            List<String> values = new ArrayList<String>(values().length);
            for(XSDComponentLabel label : XSDComponentLabel.values())
                values.add(label.value());

            return values;
        }

        public abstract String value();
    }

    public XSDLabelType() {
        super(XSDComponentLabel.stringValues());
    }
}
