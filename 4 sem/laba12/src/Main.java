import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main extends JFrame {
    private JTable sweetsTable;
    private SweetModel sweetTableModel;
    private JCheckBoxMenuItem validateXML;


    private final int COL_COUNT = 4;
    private enum BookField {
        INDEX,NAME, FACTORY, WEIGHT;
    }

    public Main() {
        super("Sweets (Laba 12)");

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem loadFromXML = new JMenuItem("Открыть XML...");
        loadFromXML.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    sweetTableModel = new SweetModel(chooser.getSelectedFile(), validateXML.getState());
                    sweetsTable.setModel(sweetTableModel);
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
        //file.add(validateXML);


        JMenuItem saveToXML = new JMenuItem("Сохранить XML");
        saveToXML.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            File ffile = new File("output.xml");
            chooser.setSelectedFile(ffile);

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
            File ffile = new File("binOutput.xml");
            chooser.setSelectedFile(ffile);

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
