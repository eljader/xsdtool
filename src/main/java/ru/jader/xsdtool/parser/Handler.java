package ru.jader.xsdtool.parser;

public interface Handler {
	
	public void handle(XsdUnit unit);
	
	public void close() throws Exception;
}
