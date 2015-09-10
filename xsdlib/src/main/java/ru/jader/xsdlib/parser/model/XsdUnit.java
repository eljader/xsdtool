package ru.jader.xsdlib.parser.model;

public class XsdUnit {
	
	private String path;
	private String type;
	private String description;
	
	public XsdUnit(String xpath, String type, String description) {
		this.path = xpath;
		this.type = type;
		this.description = description;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
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
