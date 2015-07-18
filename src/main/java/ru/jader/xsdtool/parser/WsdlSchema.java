package ru.jader.xsdtool.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WsdlSchema extends Schema {

    private static String OBJECTIVE_TAG_NAME = "schema";
    private static String OBJECTIVE_TAG_NS = "http://www.w3.org/2001/XMLSchema";
    private static String ATTRIBUTE_DELIMITER = ":";
    private static List<String> ALLOWED_ATTRIBUTIES = Arrays.asList(new String[]{"targetNamespace", "xmlns"});

    @Override
    public void load(File file) throws XmlException, IOException {
        this.sts = XmlBeans.compileXsd(getSchemas(file), XmlBeans.getBuiltinTypeSystem(), null);
        this.stl = XmlBeans
            .typeLoaderUnion(
                new SchemaTypeLoader[]{
                    this.sts,
                    XmlBeans.getBuiltinTypeSystem()
                }
            )
        ;
    }

    private XmlObject[] getSchemas(File file) throws XmlException, IOException {
        Document dom = (Document) XmlObject.Factory.parse(file).getDomNode();
        List<XmlObject> schemas = new ArrayList<XmlObject>();
        NodeList list = dom.getElementsByTagNameNS(OBJECTIVE_TAG_NS, OBJECTIVE_TAG_NAME);

        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
                schemas.add(
                    XmlObject
                        .Factory
                        .parse(file)
                        .set(assembleSchema(node))
                )
            ;
        }

        return schemas.toArray(new XmlObject[schemas.size()]);
    }

    private XmlObject assembleSchema(Node node) throws XmlException {
        Map<String, String> attributies = new HashMap<String, String>();
        getAttributies(node.getParentNode(), attributies);
        addAttributies(node, attributies);

        return XmlObject.Factory.parse(node);
    }

    private Map<String, String> getAttributies(Node node, Map<String, String> map) {
        if(node != null) {
            NamedNodeMap attributes = node.getAttributes();
            if(attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attribute = attributes.item(i);
                    if(!map.containsKey(attribute.getNodeName()))
                        map.put(attribute.getNodeName(), attribute.getNodeValue())
                    ;
                }
            }
            return getAttributies(node.getParentNode(), map);
        }
        return map;
    }

    private void addAttributies(Node node, Map<String, String> attributes) {
        Element element = (Element) node;
        for(Entry<String, String> attr : attributes.entrySet())
            if(isAllowedAttributie(attr.getKey()) && !element.hasAttribute(attr.getKey()))
                element.setAttribute(attr.getKey(), attr.getValue())
        ;
    }

    private boolean isAllowedAttributie(String name) {
        if(ALLOWED_ATTRIBUTIES.contains(name))
            return true;
        if(ALLOWED_ATTRIBUTIES.contains(name.split(ATTRIBUTE_DELIMITER)[0]))
            return true;

        return false;
    }
}
