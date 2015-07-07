package ru.jader.xsdtool.parser.handler;

import ru.jader.xsdtool.parser.model.XsdUnit;

public interface Handler {
	
	public void handle(XsdUnit unit);
	public void close() throws Exception;
}
