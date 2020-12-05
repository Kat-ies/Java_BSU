import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CounterSAXHandler extends DefaultHandler {
    private int count;
    private int totalWeight;
    private boolean isParsing;

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
        if (qName.equals("sweets")) {
            count = 0;
            totalWeight = 0;
        }
        if (qName.equals("sweet")) {
            count++;
        }
        if (qName.equals("weight")) {
            isParsing = true;
        }
    }

    @Override
    public void endElement(String s, String s1, String qName) throws SAXException {
        super.endElement(s, s1, qName);
        if (qName.equals("weight")) {
            isParsing = false;
        }
    }

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {
        super.characters(chars, i, i1);
        if (isParsing)
            totalWeight += Integer.parseInt(new String(chars, i, i1));
    }

    public int getSweetsCount() {
        return count;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public double getAvgW() {
        return ((double)totalWeight)/count;
    }















}
