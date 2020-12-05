package giftSupport;

import sweetComparators.ShelfTimeComparator;
import sweetComparators.SugarComparator;
import sweetComparators.WeightComparator;
import sweetPackage.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Gift {
    private ArrayList<Sweet> giftSweets = new ArrayList<Sweet>();

    public Gift() { }

    public void addSweet(Sweet sweet) {
        giftSweets.add(sweet);
    }

    public void printGift() {
        for (Sweet i : giftSweets) {
            System.out.println(i.toString());
        }
    }

    public int countWeight() {
        int summary = 0;
        for (Sweet i : giftSweets)
            summary += i.getWeight();
        return summary;
    }

    public void sortByShelfTime() { giftSweets.sort(new ShelfTimeComparator());}
    public void sortBySugar() { giftSweets.sort(new SugarComparator());}
    public void sortByWeight() { giftSweets.sort(new WeightComparator());}

    public ArrayList<Sweet> findSweetsBySugar(int a, int b) throws MyException {
        ArrayList<Sweet> appropriateSweets = new ArrayList<Sweet>();
        if (a<0 || b<0 || a>b)
            throw new MyException("неверный диапазон!");
        for (Sweet i : giftSweets) {
            if (i.getSugar() >= a && i.getSugar() <= b)
                appropriateSweets.add(i);
        }
        return appropriateSweets;
    }

    public void printSweetsBySugar  (int a, int b) throws MyException {
        System.out.println("Sweets with sugar content (" + a + ", " + b + "):");
        boolean flag=false;
        for (Sweet i : findSweetsBySugar(a, b))
        { System.out.println(i.toString());
         flag=true;}
        if (!flag)
            System.out.println("0");
    }

    public ArrayList<Sweet> getGiftSweets() {
        return giftSweets;
    }
    public void setGiftSweets(ArrayList<Sweet> giftSweets) {
        this.giftSweets = giftSweets;
    }

    public boolean isEmpty(){
        if (giftSweets.isEmpty())
            return true;
        else return false;
    }

    public void readXMLFile(File file) throws NoSuchElementException, NumberFormatException,
            ParserConfigurationException, IOException, SAXException, MyException {

        giftSweets.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        String name;
        int weight, sugar, shelfTimeDays;
        String filling;

        NodeList nodeList = doc.getElementsByTagName("chocolateBar");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                name = element.getAttribute("name");
                filling = element.getElementsByTagName("filling").item(0).getTextContent();
                weight = Integer.parseInt(element.getElementsByTagName("weight").item(0).getTextContent());
                sugar = Integer.parseInt(element.getElementsByTagName("sugar").item(0).getTextContent());
                shelfTimeDays = Integer.parseInt(element.getElementsByTagName("shelfTimeDays").item(0).getTextContent());

                if (weight < 0 || sugar < 0 || shelfTimeDays < 0)
                    throw new MyException("У поля " + name + " отрицательные числа!!");
                giftSweets.add(new ChocolateBar(name, weight, sugar, shelfTimeDays, filling));
            }
        }

        boolean cacaoAdded;
        nodeList = doc.getElementsByTagName("cookie");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                name = element.getAttribute("name");
                weight = Integer.parseInt(element.getElementsByTagName("weight").item(0).getTextContent());
                sugar = Integer.parseInt(element.getElementsByTagName("sugar").item(0).getTextContent());
                shelfTimeDays = Integer.parseInt(element.getElementsByTagName("shelfTimeDays").item(0).getTextContent());

                String buf = element.getElementsByTagName("cacaoAdded").item(0).getTextContent();
                if (buf.equals("true"))
                    cacaoAdded = true;
                else if (buf.equals("false"))
                    cacaoAdded = false;
                else throw new MyException("У поля " + name + " неверно заполенено поле какао!!");

                if (weight < 0 || sugar < 0 || shelfTimeDays < 0)
                    throw new MyException("У поля " + name + " отрицательные числа!!");
                giftSweets.add(new Cookies(name, weight, sugar, shelfTimeDays, cacaoAdded));
            }
        }

        nodeList = doc.getElementsByTagName("meringue");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                name = element.getAttribute("name");
                filling = element.getElementsByTagName("suppliments").item(0).getTextContent();
                weight = Integer.parseInt(element.getElementsByTagName("weight").item(0).getTextContent());
                sugar = Integer.parseInt(element.getElementsByTagName("sugar").item(0).getTextContent());
                shelfTimeDays = Integer.parseInt(element.getElementsByTagName("shelfTimeDays").item(0).getTextContent());

                if (weight < 0 || sugar < 0 || shelfTimeDays < 0)
                    throw new MyException("У поля " + name + " отрицательные числа!!");
                giftSweets.add(new Meringue(name, weight, sugar, shelfTimeDays, filling));
            }
        }

    }

}


