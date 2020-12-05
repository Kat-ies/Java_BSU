import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main extends JFrame {
    private JTable sweetsTable;
    private SweetModel sweetTableModel;
    private JCheckBoxMenuItem validateXML;
    private File ffile;


    private final int COL_COUNT = 4;
    private enum BookField {
        INDEX,NAME, FACTORY, WEIGHT;
    }

    public Main() {
        super("Sweets (Laba 13)");

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem loadFromXML = new JMenuItem("Открыть используя DOM");
        loadFromXML.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel = new SweetModel(chooser.getSelectedFile(), validateXML.getState());
                    sweetsTable.setModel(sweetTableModel);
                    ffile= chooser.getSelectedFile();
                    if (validateXML.getState())
                        JOptionPane.showMessageDialog(this,
                                "XML is valid and matches XSD schema",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(this, "Invalid XML file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        file.add(loadFromXML);

        validateXML = new JCheckBoxMenuItem("Validate XML");
        validateXML.setState(true);
        file.add(validateXML);


        JMenuItem saveToXML = new JMenuItem("Сохранить XML");
        saveToXML.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            File files = new File("output.xml");
            chooser.setSelectedFile(files);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel.saveToXML(chooser.getSelectedFile());
                } catch (FileNotFoundException err) {
                    JOptionPane.showMessageDialog(this, "Invalid file path", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        file.add(saveToXML);
        file.addSeparator();

        JMenuItem saveToBinary = new JMenuItem("Сохранить в бин.ф.");
        saveToBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            File files = new File("binOutput.xml");
            chooser.setSelectedFile(files);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel.saveToBinary(chooser.getSelectedFile());
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(this, "Invalid file path", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        file.add(saveToBinary);

        JMenuItem loadFromBinary = new JMenuItem("Открыть из бин.ф");
        loadFromBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel = SweetModel.loadFromBinary(chooser.getSelectedFile());
                    sweetsTable.setModel(sweetTableModel);
                    ffile= chooser.getSelectedFile();
                    if (validateXML.getState())
                    { JOptionPane.showMessageDialog(this,
                                "XML is valid and matches XSD schema",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(this, "Invalid binary file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        file.add(loadFromBinary);
        file.addSeparator();

        JMenuItem SAXXml = new JMenuItem("Открыть используя SAX");
        SAXXml.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML файл", "xml"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel =SweetModel.parseSAX(chooser.getSelectedFile(),validateXML.getState());
                    sweetsTable.setModel(sweetTableModel);
                    ffile= chooser.getSelectedFile();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ParserConfigurationException | SAXException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid file "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        file.add(SAXXml);



        JMenuItem calcXml = new JMenuItem("Посчитать... (SAX)");
        calcXml.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    if (validateXML.getState())
                    { JOptionPane.showMessageDialog(this,
                            "XML is valid and matches XSD schema",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    CounterSAXHandler handler = new CounterSAXHandler();
                    parser.parse(chooser.getSelectedFile(), handler);
                    JOptionPane.showMessageDialog(this,
                            "Количество конфет: " + handler.getSweetsCount() +
                                    "\nОбщий вес: " + handler.getTotalWeight() +
                                    "\nСредний вес: " + handler.getAvgW(),
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(this, "Invalid XML file "+ err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        file.add(calcXml);

        JMenuItem convertToHTML = new JMenuItem("Конвертировать в HTML");
        convertToHTML.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("HTML файл", "html"));
           // File fileOut = new File("output.html");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel.convert(ffile, chooser.getSelectedFile(), getClass().getClassLoader().getResource("html.xsl"));
                    JOptionPane.showMessageDialog(this, ffile.getName() + " converted! " , "Converted", JOptionPane.INFORMATION_MESSAGE);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(this, "Файл не открыт", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Путь не найден", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (TransformerException ex) {
                    JOptionPane.showMessageDialog(this, "Некорректный файл", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        file.add(convertToHTML);


        JMenu booksMunu = new JMenu("Конфеты");
        JMenuItem addBook = new JMenuItem("Добавить");
        addBook.addActionListener(e -> {
            sweetTableModel.getItems().add(new Sweet());
            sweetsTable.updateUI();
        });
        booksMunu.add(addBook);

        JMenuItem deleteBook = new JMenuItem("Удалить");
        deleteBook.addActionListener(e -> {
            sweetTableModel.deleteRows(sweetsTable.getSelectedRows());
            sweetsTable.updateUI();
        });
        booksMunu.add(deleteBook);

        menuBar.add(file);
        menuBar.add(booksMunu);
        add(menuBar, BorderLayout.NORTH);


        sweetTableModel = new SweetModel();
        sweetsTable = new JTable(sweetTableModel);
        add(new JScrollPane(sweetsTable), BorderLayout.CENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}
