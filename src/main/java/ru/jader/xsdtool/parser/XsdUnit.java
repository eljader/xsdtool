package ru.jader.xsdtool.parser;

public class XsdUnit {
	
	private String xpath;
	private String type;
	private String description;
	
	public XsdUnit(String xpath, String type, String description) {
		this.xpath = xpath;
		this.type = type;
		this.description = description;
	}
	
	public String getXpath() {
		return xpath;
	}
	
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
