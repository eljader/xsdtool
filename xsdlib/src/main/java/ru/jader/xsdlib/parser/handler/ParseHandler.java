package ru.jader.xsdlib.parser.handler;

import ru.jader.xsdlib.parser.model.XSDUnit;

public interface ParseHandler {
    public void handle(XSDUnit unit) throws ParseHandlerException;
    public void complete() throws ParseHandlerException;
}
