import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SweetModel extends AbstractTableModel implements Serializable {
    private List<Sweet> sweets;
    private final String COL_NAMES[] = {"Индекс", "Название", "Фабрика", "Вес"};


    public SweetModel(File file, boolean validate) throws IOException, org.xml.sax.SAXException {
        sweets = new ArrayList<>();
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);

        if (validate) {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(SweetModel.class.getClassLoader().getResource("schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        }

        try {
            DocumentBuilder builder = f.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node root = doc.getFirstChild();

            NodeList items = root.getChildNodes();
            for (int i = 0; i < items.getLength(); ++i) {
                Node node = items.item(i);
                if (!node.getNodeName().equals("sweet"))
                    continue;
                Sweet curSweet = new Sweet();
                curSweet.setIndex(node.getAttributes().getNamedItem("index").getTextContent());
                curSweet.setName(node.getAttributes().getNamedItem("name").getTextContent());

                NodeList fields = node.getChildNodes();
                for(int j = 0; j < fields.getLength(); ++j) {
                    switch (fields.item(j).getNodeName()) {
                        case "factory":
                            curSweet.setFactory(fields.item(j).getTextContent());
                            break;
                        case "weight":
                            curSweet.setWeight(Integer.parseInt(fields.item(j).getTextContent()));
                            break;
                    }
                }
                sweets.add(curSweet);
            }
        } catch (ParserConfigurationException ignored) {}
    }


    public SweetModel() {
        sweets = new ArrayList<>();
    }

    public List<Sweet> getItems() {
        return sweets;
    }
    public void setItems(List<Sweet> sweets) {
        this.sweets = sweets;
    }

    @Override
    public int getRowCount() {
        return sweets.size();
    }
    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return sweets.get(rowIndex).getIndex();
            case 1:
                return sweets.get(rowIndex).getName();
            case 2:
                return sweets.get(rowIndex).getFactory();
            case 3:
                return sweets.get(rowIndex).getWeight();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column < 3)
            return String.class;
        else
            return Integer.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                sweets.get(rowIndex).setIndex((String) aValue);
                break;
            case 1:
                sweets.get(rowIndex).setName((String) aValue);
                break;
            case 2:
                sweets.get(rowIndex).setFactory((String) aValue);
                break;
            case 3:
                sweets.get(rowIndex).setWeight((Integer) aValue);
                break;
        }
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<sweets>\n");
        for (Sweet sweet : sweets){
            sb.append("\t<sweet index=\"").append(sweet.getIndex()).append("\" name=\"").append(sweet.getName()).append("\">\n");
            sb.append("\t\t<factory>").append(sweet.getFactory()).append("</factory>\n").append("\t\t<weight>").append(sweet.getWeight()).append("</weight>\n");
            sb.append("\t</sweet>\n");
        }
        sb.append("</sweets>\n");
        return sb.toString();
    }

    public void saveToXML(File file) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(file);
        pw.println("<?xml version=\"1.0\" encoding=\"windows-1251\"?>");
        pw.println(toXML());
        pw.close();
    }

    public void deleteRows(int[] rows) {
        for (int i = rows.length - 1; i >= 0; --i)
            if (rows[i] < sweets.size())
                sweets.remove(rows[i]);
    }

    public static SweetModel loadFromBinary(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
        SweetModel model = new SweetModel();
        model.setItems((List<Sweet>) stream.readObject());
        stream.close();
        return model;
    }
    public void saveToBinary(File file) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
        stream.writeObject(sweets);
        stream.close();
    }

    public static SweetModel parseSAX(File file,boolean validate) throws IOException,SAXException, ParserConfigurationException  {
        if (validate) {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(SweetModel.class.getClassLoader().getResource("schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        }

        SweetModel model = new SweetModel();
        try {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SweetsSAXHandler swHandler= new SweetsSAXHandler();
        parser.parse(file, swHandler);
        model.setItems((List<Sweet>) swHandler.getSweets());

        } catch (ParserConfigurationException ignored) {}
        return model;
    }

    public static void convert(File source, File target, URL xsltURL) throws IOException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source styleSource = new StreamSource(xsltURL.openStream(), xsltURL.toExternalForm());
        Transformer transformer = factory.newTransformer(styleSource);
        transformer.transform(new StreamSource(source), new StreamResult(target));

    }
}
