import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Window extends JFrame {
    private JButton buttonOpen;
    private JButton buttonExit;
    private JButton buttonAddData;
    private JButton buttonResult;
    private JTextField costMax;
    private JTextField ageField;
    private JPanel mainPane;
    private JTextPane textPane;
    private JTextPane textPaneForRes;
    private JButton buttonSave;
    private JFileChooser fileChooser;
    private File file;


    ArrayList<Toy> toys = new ArrayList<>();
    List<Toy> toysForXML= new ArrayList<>();

    Window() {
        setContentPane(mainPane);
        setTitle("Каталог игрушек");
        setPreferredSize(new Dimension(640, 500));
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File ("D:\\Катя\\Прогр. 2 курс\\Java\\laba_14\\"));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        });
        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Choose file: ");
                int returnValue = fileChooser.showOpenDialog(Window.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    try {
                        String checkFormat = getFileExtension(file.getName());
                        if (checkFormat.equals("xml"))
                            readXMLFile(file,toys);
                        else
                            if (checkFormat.equals("txt"))
                        readTxtFile(file, toys);
                            else throw new MyException("Выбранный файл не поддерживается!");
                        textPane.setText(print(toys));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Window.this, "problems with file!");
                    } catch (MyException ex) {
                        JOptionPane.showMessageDialog(Window.this, ex.getMessage());
                    }
                    catch (NumberFormatException exc) {
                        JOptionPane.showMessageDialog(Window.this, "incorrect data!");
                    }
                    catch ( ParserConfigurationException ex) {
                        JOptionPane.showMessageDialog(Window.this, "ParserConfigurationException caused!");
                    }
                    catch ( SAXException exc) {
                        JOptionPane.showMessageDialog(Window.this, "SAXException caused!");
                    }
                }
            }
        });
        buttonAddData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var dialog = new AddToy(toys);
                dialog.pack();
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setVisible(true);
                textPane.setText(print(toys));
            }
        });
        buttonResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) throws NoSuchElementException {
                try {

                    List<Toy> buffer = (ArrayList<Toy>) ((ArrayList<Toy>) toys).clone();

                    int cost, minAge, maxAge;
                    cost = Integer.parseInt(costMax.getText());
                    if (cost < 0)
                        throw (new MyException("Стоимость не может быть отрицательной!"));
                    Pattern pattern = Pattern.compile("[1-9]{1,2}[0-9]{0,2}[\\-][1-9]{1,2}[0-9]{0,2}");
                    Matcher matcher = pattern.matcher(ageField.getText());
                    boolean matches = matcher.matches();
                    if (!matches)
                        throw (new MyException("Ошибка регулярного выражения. Ваш возраст не соответсвует формату *-*"));
                    StringTokenizer buf = new StringTokenizer(ageField.getText(), "-");
                    minAge = Integer.parseInt(buf.nextToken());
                    maxAge = Integer.parseInt(buf.nextToken());
                    //if (minAge<0 || maxAge<0)
                    // throw (new MyException("Возраст не может быть отрицательным!"));
                    if (maxAge > 140 || minAge > 140)
                        throw (new MyException("Люди так долго не живут!"));
                    Toy t = new Toy(" ", cost, minAge, maxAge);

                    Collections.sort(buffer, Toy.ToyComparator);
                    textPaneForRes.setText(print(buffer, t));

                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(Window.this, ex.getMessage());
                }
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter writer;
                File file;
                try {
                    fileChooser.setDialogTitle("Save file: ");
                    file = new File("output.xml");
                    fileChooser.setSelectedFile(file);
                    fileChooser.showSaveDialog(null);
                  file=  fileChooser.getSelectedFile();
                    writer = new FileWriter(file, false);
                   saveAsXML(writer, (ArrayList<Toy>) toysForXML);
                    writer.close();
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла!");
                }
            }
        });
    }

    private void onClose() {
        // add your code here if necessary
        dispose();
    }

    public void readTxtFile(File file, ArrayList<Toy> toys) throws FileNotFoundException,
            NoSuchElementException, MyException {
        Scanner scanner = new Scanner(file);
        String name;
        int cost;
        int minAge, maxAge;
        toys.clear();
        while (scanner.hasNext()) {

            name = scanner.next();
            cost = scanner.nextInt();
            String bufString = scanner.next();
            Pattern pattern = Pattern.compile("[1-9]{1,2}[0-9]{0,2}[\\-][1-9]{1,2}[0-9]{0,2}");
            Matcher matcher = pattern.matcher(bufString);
            boolean matches = matcher.matches();
            if (!matches)
                throw (new MyException("Ошибка регулярного выражения. Ваш возраст не соответсвует формату *-*"));
            StringTokenizer buf = new StringTokenizer(bufString, "-");
            minAge = Integer.parseInt(buf.nextToken());
            maxAge = Integer.parseInt(buf.nextToken());
            //  if (minAge<0 || maxAge<0)
            //      throw (new MyException("Возраст не может быть отрицательным!"));
            if (maxAge > 140 || minAge > 140)
                throw (new MyException("Люди так долго не живут!"));
            toys.add(new Toy(name, cost, minAge, maxAge));
        }

    }

    public void readXMLFile(File file, ArrayList<Toy> toys) throws NoSuchElementException,NumberFormatException,
            ParserConfigurationException,IOException, SAXException, MyException {

        String name;
        int cost,minAge,maxAge;
        toys.clear();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        NodeList nodeList = doc.getElementsByTagName("toy");

        for (int i = 0 ; i < nodeList.getLength() ; i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                //name = element.getElementsByTagName("name").item(0).getTextContent();
                name= element.getAttribute("name");
                cost = Integer.parseInt(element.getElementsByTagName("cost").item(0).getTextContent());
           //     cost = Integer.parseInt(element.getAttribute("cost"));
                minAge=Integer.parseInt(element.getElementsByTagName("minAge").item(0).getTextContent());
                maxAge=Integer.parseInt(element.getElementsByTagName("maxAge").item(0).getTextContent());
                if (cost < 0)
                    throw new MyException("У поля " + name + " отрицательная стоимость!");
                if (minAge<0 || maxAge<0)
                    throw (new MyException("Возраст не может быть отрицательным!"));
                if (maxAge > 140 || minAge > 140)
                    throw (new MyException("Люди так долго не живут!"));
                toys.add(new Toy(name,cost,minAge,maxAge));
            }
        }
    }

    private String print(List<Toy> toys, Toy t) {
        StringBuffer st = new StringBuffer();
        toysForXML.clear();

        toys.stream().filter(x -> (t.getMinAge() >= x.getMinAge() && t.getMaxAge() <= x.getMaxAge()) && x.getCost() < t.getCost()).forEach(x ->toysForXML.add(x));
        for(Toy i: toysForXML){
            st.append(i.toString());
        }
        buttonSave.setEnabled(true);
        if (st.length() == 0) {
            st.append("По вашему запросу ничего не найдено!");
            buttonSave.setEnabled(false);
        }
        return st.toString();
    }
    private String print(ArrayList<Toy> toys) {
        StringBuffer st = new StringBuffer();

        ListIterator<Toy> it = toys.listIterator();
        for (; it.hasNext(); )
            st.append(it.next().toString());
        return st.toString();
    }
    private void saveAsXML(FileWriter writer, ArrayList<Toy> saveToys) throws IOException{
        StringBuffer xmlStringData = new StringBuffer();
        xmlStringData.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlStringData.append("<toys>\n");


        for (Toy it: toysForXML){
            xmlStringData.append("<toy name=\"" +it.getName() + "\">\n");
            xmlStringData.append("<cost>" + it.getCost() + "</cost>\n");
            xmlStringData.append("<minAge>" + it.getMinAge() + "</minAge>\n");
            xmlStringData.append("<maxAge>" + it.getMaxAge()+ "</maxAge>\n");
            xmlStringData.append("</toy>\n");
        }
        xmlStringData.append("</toys>\n");
        writer.write(xmlStringData.toString());
    }
    private static String getFileExtension(String myStr) {
        int index = myStr.indexOf('.');
        return index == -1? null : myStr.substring(index+1);
    }
}
class MyException extends Exception {
    MyException() {
    }

    MyException(String str) {
        super(str);
    }
}