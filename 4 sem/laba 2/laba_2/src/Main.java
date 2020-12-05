import org.xml.sax.SAXException;
import giftSupport.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, MyException, SAXException, IOException {
        try {
            File file = new File("sweets.xml");
            Scanner in = new Scanner(System.in);
            Gift firstGift = new Gift();

            System.out.println("Введите цифру команды:");
            System.out.println("1 - сформировать подарок");
            System.out.println("2 - вывести вес");
            System.out.println("3 - сортировать по содержанию сахара");
            System.out.println("4 - сортировать по времени хранения");
            System.out.println("5 - сортировать по весу");
            System.out.println("6 - поиск конфеты");
            System.out.println("7 - выход ");

            boolean work=true;
            while (work) {
                int i= in.nextInt();
                switch (i) {
                    case 1:
                        firstGift.readXMLFile(file);
                        if (checkGift(firstGift)) break;
                        System.out.println("New Year Gift:");
                        print(firstGift);
                        break;
                    case 2:
                        if (checkGift(firstGift)) break;
                        System.out.println("Weight of present:  " + firstGift.countWeight());
                        System.out.println();
                        break;
                    case 3:
                        if (checkGift(firstGift)) break;
                        System.out.println("Sorting by sugar:");
                        firstGift.sortBySugar();
                        print(firstGift);
                        break;
                    case 4:
                        if (checkGift(firstGift)) break;
                        System.out.println("Sorting by shelf time:");
                        firstGift.sortByShelfTime();
                        print(firstGift);
                        break;
                    case 5:
                        if (checkGift(firstGift)) break;
                        System.out.println("Sorting by weight:");
                        firstGift.sortByWeight();
                        print(firstGift);
                        break;
                    case 6:
                        if (checkGift(firstGift)) break;
                        System.out.println("Enter a and b:");
                        firstGift.printSweetsBySugar(in.nextInt(), in.nextInt());
                        System.out.println();
                        break;
                    case 7:
                        work = false;
                        break;
                    default:
                        System.out.println("wrong comand!");
                        break;
                }
            }
        }
        catch (IOException ex) {
            System.out.println("problems with file!");
        } catch (MyException ex) {

            System.out.println(ex.getMessage());
        }
        catch (NumberFormatException exc) {
            System.out.println("incorrect data!");
        }
        catch ( ParserConfigurationException ex) {
            System.out.println( "ParserConfigurationException caused!");
        }
        catch ( SAXException exc) {
            System.out.println("SAXException caused!");
        }
    }

    private static void print(Gift firstGift) {
        firstGift.printGift();
        System.out.println();
    }

    private static boolean checkGift(Gift firstGift) {
        if (firstGift.isEmpty()) {
            System.out.println("Empty!");
            return true;
        }
        return false;
    }
}
