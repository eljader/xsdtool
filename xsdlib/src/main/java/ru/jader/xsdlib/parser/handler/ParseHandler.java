package ru.jader.xsdlib.parser.handler;

import ru.jader.xsdlib.parser.model.XsdUnit;

public interface ParseHandler {
    public void handle(XsdUnit unit) throws ParseHandlerException;
    public void finalize() throws ParseHandlerException;
}
