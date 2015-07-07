package ru.jader.xsdtool.parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import ru.jader.xsdtool.parser.handler.Handler;
import ru.jader.xsdtool.parser.model.XsdUnit;

public class SimpleXsdParser extends AbstractXsdParser {
	
	private static String DELIMITER = "/";
	private static String ATTR_DELIMITER = DELIMITER + "@";
	
	private Handler handler;
	
	public SimpleXsdParser(File file, Handler handler) throws XmlException, IOException {
		super(file);
		this.handler = handler;
	}
	
	@Override
	protected String rebuildPath(String path, SchemaLocalElement element) {
		return path + DELIMITER + element.getName().getLocalPart();  	
	}
	
	@Override
	protected void processElement(String path, SchemaLocalElement element) {
		handler.handle(createXsdUnit(path, element));
	}
	
	@Override
	protected void processAttribute(String path, SchemaProperty attribute) {
		handler.handle(createXsdUnit(path, attribute, ATTR_DELIMITER));
	}

	@Override
	protected boolean isRecursive(String path, SchemaLocalElement element) {
		//TODO make namespace sensitive
		return (path.indexOf(String.format("/%s/", element.getName().getLocalPart())) == -1) ? false : true;	
	}
	
	private XsdUnit createXsdUnit(String path, SchemaLocalElement element) {
		return 
			createXsdUnit(
				path,
				assembleType(element.getType()),
				assembleDescription(element)
			)
		;
	}

	private XsdUnit createXsdUnit(String path, SchemaProperty property ,String delimiter) {
		return 
			createXsdUnit(
				path + delimiter + property.getName().getLocalPart(),
				assembleType(property.getType()),
				assembleDescription(property)
			)
		;
	}

	private XsdUnit createXsdUnit(String path, String type, String description) {
		return 
			new XsdUnit(path, type, description)
		;
	}

	private String assembleType(SchemaType type) {  	
		if(type.isPrimitiveType())
			return type.getName().getLocalPart();
		else if(type.getContentModel() != null)
			return type.getName().toString();
		else
			return type.getBaseType().getName().getLocalPart();
	}

	private String assembleDescription(SchemaProperty property) {
		StringBuilder description = new StringBuilder();
		assembleDescription(property.getType(), description);

		return description.toString();
	}

	private String assembleDescription(SchemaLocalElement element) {
		StringBuilder description = new StringBuilder();
		SchemaAnnotation annotation = element.getAnnotation();

		if(annotation != null)
			assembleDescription(annotation, description)
		;

		assembleDescription(element.getType(), description);

		return description.toString();
	}

	private void assembleDescription(SchemaType type, StringBuilder description) {
		String[] enums = getEnum(type);
		SchemaAnnotation annotation = type.getAnnotation();

		if(annotation != null)
			assembleDescription(annotation, description)
		;

		if(!type.isPrimitiveType()) {
			for (Entry<String, Integer> facetEntry : getFacetMap().entrySet()) {
				XmlAnySimpleType facet = type.getFacet(facetEntry.getValue());

				if(facet != null)
					addLineSeparator(description)
						.append(facetEntry.getKey() + "=" + facet.getStringValue())
				;
			}
		}

		if(enums != null)
			addLineSeparator(description).append(Arrays.toString(enums))
		;
	}

	private void assembleDescription(SchemaAnnotation annotation, StringBuilder description) {
		XmlObject[] infos = annotation.getUserInformation();
		for (int i = 0; i < infos.length; i++) {
			XmlObject info = infos[i];
			addLineSeparator(description).append(info.newCursor().getTextValue().trim());
		}	
	}

	private String[] getEnum(SchemaType type) {
		SchemaType enumType = type.getBaseEnumType();

		if(enumType != null) {
			SchemaStringEnumEntry[] enumEntries = enumType.getStringEnumEntries();
			String[] enums = new String[enumEntries.length];

			for(int i = 0; i < enumEntries.length; i++)
				enums[i] = enumEntries[i].getEnumName()
			;

			return enums;
		}

		return null;
	}

	private Map<String, Integer> getFacetMap() {
		Map<String, Integer>  map = new HashMap<String, Integer> ();

		map.put("LENGTH"          ,SchemaType.FACET_LENGTH);
		map.put("MIN_LENGTH"      ,SchemaType.FACET_MIN_LENGTH);
		map.put("MAX_LENGTH"      ,SchemaType.FACET_MAX_LENGTH);
		map.put("MIN_EXCLUSIVE"   ,SchemaType.FACET_MIN_EXCLUSIVE);
		map.put("MIN_INCLUSIVE"   ,SchemaType.FACET_MIN_INCLUSIVE);
		map.put("MAX_INCLUSIVE"   ,SchemaType.FACET_MAX_INCLUSIVE);
		map.put("MAX_EXCLUSIVE"   ,SchemaType.FACET_MAX_EXCLUSIVE);
		map.put("TOTAL_DIGITS"    ,SchemaType.FACET_TOTAL_DIGITS);
		map.put("FRACTION_DIGITS" ,SchemaType.FACET_FRACTION_DIGITS);
		map.put("PATTERN"         ,SchemaType.FACET_PATTERN);

		return map;
	}

	private StringBuilder addLineSeparator(StringBuilder builder) {
		if(builder.length() > 0)
			builder.append(System.getProperty("line.separator"))
		;

		return builder;
	}
}
