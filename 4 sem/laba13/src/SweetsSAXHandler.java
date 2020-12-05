import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SweetsSAXHandler extends DefaultHandler {

    private int weight;
    private String name;
    private String factory;
    private String index;
    private boolean isFactory;
    private boolean isWeight;
    private List<Sweet> sweets = new ArrayList<>();

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
        if (qName.equals("sweet")) {
            index = atts.getValue("index");
            name = atts.getValue("name");

        }
        if (qName.equals("factory")) {
            isFactory = true;
        }
        if (qName.equals("weight")) {
            isWeight = true;
        }

    }

    @Override
    public void endElement(String s, String s1, String qName) throws SAXException {
        super.endElement(s, s1, qName);
        if (qName.equals("sweet")) {
            sweets.add(new Sweet(index, name,factory,weight));
        }
    }

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {
        super.characters(chars, i, i1);
        if (isFactory) {
            factory = new String(chars, i, i1);
            isFactory = false;
        }
        if (isWeight) {
            String s = new String(chars, i, i1);
            weight = Integer.parseInt(s);
            isWeight = false;
        }
    }

    public List<Sweet> getSweets() {
        return sweets;
    }

}
