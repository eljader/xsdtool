package ru.jader.xsdtool.parser.handler;

import ru.jader.xsdtool.parser.model.XsdUnit;

public interface ParseHandler {
    public void handle(XsdUnit unit) throws ParseHandlerException;
    public void finalize() throws ParseHandlerException;
}
