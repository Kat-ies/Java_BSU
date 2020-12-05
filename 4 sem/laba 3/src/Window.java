import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Window extends JFrame {

    private JTabbedPane tabbedPane;

    private JPanel countriesTab, countriesInfo;
    private JList<Country> countryList;
    private DefaultListModel<Country> countryListModel;
    private JLabel countryFlag, countryName, countryCapital;


    private JMenu tourMenu;
    private JPanel toursTab;
    private JTable tourTable, tourSummary;
    private DefaultTableModel tourTableModel;



    public Window() {

        super("laba 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        countriesTab = new JPanel(new BorderLayout());
        tabbedPane.add(countriesTab, "Countries");
        initCountriesTab(countriesTab);

        toursTab = new JPanel(new BorderLayout());
        tabbedPane.add(toursTab, "Tours");
        initToursTab(toursTab);


        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);

        tourMenu = new JMenu("Tours");
        JMenuItem addTour = new JMenuItem("Add tour");
        addTour.addActionListener(e -> tourTableModel.setRowCount(tourTableModel.getRowCount()+1));
        tourMenu.add(addTour);
        menuBar.add(tourMenu);
    }

    private void initCountriesTab(JPanel tab) {
        try{
        countryListModel = new DefaultListModel<>();
        countryList = new JList<>(countryListModel);
        countryList.setCellRenderer(new CountryJLabel());
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tab.add(new JScrollPane(countryList), BorderLayout.CENTER);
        File input = new File("src\\flags_list.txt");
        Scanner in= new Scanner (input);
        int n=Integer.parseInt(in.nextLine());

        Map<String, String> capitals = new TreeMap<String, String>() ;
                for (int i=0; i<n; i++){
                    capitals.put( in.nextLine(),in.nextLine());
                }
                in.close();
            //put("vatican city", "Vatican City");
        capitals.forEach(
                (country, capital) -> countryListModel.addElement(
                        new Country(
                                country,
                                capital,
                                new ImageIcon("flags/flag_"+country.replace(' ', '_')+".png")
                        )
                )
        );

        countriesInfo = new JPanel();
        countriesInfo.setLayout(new BoxLayout(countriesInfo, BoxLayout.Y_AXIS));
        Dimension d=new Dimension(200,300);
        countriesInfo.setPreferredSize(d);
        tab.add(countriesInfo, BorderLayout.EAST);

        countryFlag = new JLabel();
        countryName = new JLabel();
        countryCapital = new JLabel();

        countriesInfo.add(countryFlag);
        countriesInfo.add(countryName);
        countriesInfo.add(countryCapital);

        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Country c = countryList.getSelectedValue();
                countryFlag.setIcon(c.getFlag());
                countryName.setText(c.getName().toUpperCase());
                countryCapital.setText("Capital: " + c.getCapital());
            }
        });

        countryList.setSelectedIndex(0);}
        catch (InputMismatchException | FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void initToursTab(JPanel tab) {
        String colNames[] = {"Flag", "Description", "Price", "Selected"};
        Object[][] data = {
                {new ImageIcon("flags/flag_egypt.png"), "Egypt pyramids ", 1000, false},
                {new ImageIcon("flags/flag_belarus.png"), "Minsk sights",100, false},
                {new ImageIcon("flags/flag_great_britain.png"), "The Tower of London", 1999, false},
                {new ImageIcon("flags/flag_australia.png"), "Week in kangaroo land", 5000, false},
        };

        tourTableModel = new DefaultTableModel(data, colNames);
        tourTable = new JTable(tourTableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return ImageIcon.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return Boolean.class;
                    default:
                        return super.getColumnClass(column);
                }
            }

        };
        tab.add(new JScrollPane(tourTable), BorderLayout.CENTER);
        tourTable.setRowHeight(40);
        tourTable.getTableHeader().setReorderingAllowed(false);
        tourTable.getModel().addTableModelListener(this::tableChanged);


        tourSummary = new JTable(1,4) {
            DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
            {
                renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
            }

            /*@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }*/

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return renderRight;
            }
        };
        tourSummary.setValueAt("Total:", 0, 2);
        tourSummary.setValueAt(0, 0, 3);
        tab.add(tourSummary, BorderLayout.SOUTH);
    }

    private void tableChanged(TableModelEvent e) {
        if (e.getColumn() == 3 || e.getColumn()==2) {
            int row = e.getFirstRow();

            int number = (int) tourTable.getValueAt(row, 2);

                int sum=0;
                for (int i=0; i<tourTable.getRowCount();i++){
                    if ((Boolean) tourTable.getValueAt(i, 3))
                        sum+=(int) tourTable.getValueAt(i,2);

                }
                tourSummary.setValueAt(sum,0,3);
        }
    }
}

class MyException extends Exception {
    MyException() {
    }

    MyException(String str) {
        super(str);
    }
}