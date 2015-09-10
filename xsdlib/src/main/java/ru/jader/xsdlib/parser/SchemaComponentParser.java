package ru.jader.xsdlib.parser;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;

public abstract class SchemaComponentParser {

    public void parse(SchemaComponent component) throws ParseComponentException {
        SchemaType type = null;

        if(component instanceof SchemaType)
            type = (SchemaType) component;
        if(component instanceof SchemaGlobalElement)
            type = ((SchemaGlobalElement) component).getType();
        if(type == null)
            throw new RuntimeException(String.format("not implemented for %s", component.getClass()));

        parseType(type, new String());
    }

    private void parseElement(SchemaLocalElement element, String path) throws ParseComponentException {
        SchemaType type = element.getType();
        boolean isNotRecursive = !this.isRecursive(path, element);
        path = this.rebuildPath(path, element);

        if(type.getContentModel() != null && isNotRecursive)
            parseType(type, path);
        else
            this.process(path, element);
    }

    private void parseType(SchemaType type, String path) throws ParseComponentException {
        SchemaProperty[] attributes = type.getAttributeProperties();

        for (int i = 0; i < attributes.length; i++) {
            SchemaProperty attribute = attributes[i];
            this.process(path, attribute);
        }

        SchemaParticle particle = type.getContentModel();

        if(particle == null || type.getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE) {
            this.process(path, type);
        }
        else if(particle.countOfParticleChild() == 0) {
            parseElement((SchemaLocalElement) particle, path);
        }
        else {
            SchemaParticle[] childes = particle.getParticleChildren();

            for (int i = 0; i < childes.length; i++) {
                SchemaLocalElement element = (SchemaLocalElement) childes[i];
                parseElement(element, path);
            }
        }
    }

    protected abstract String rebuildPath(String path, SchemaLocalElement element);

    protected abstract void process(String path, Object parseObject) throws ParseComponentException;

    protected abstract boolean isRecursive(String path, SchemaLocalElement element);
}
