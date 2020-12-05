import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.stream.Stream;

import static javax.imageio.ImageIO.read;

 class MainWindow extends JFrame{
    JTabbedPane contentPane;
    JPanel tab1;
    JPanel tab2;
    JPanel tab3;

    MainWindow(){
        contentPane = new JTabbedPane();
        /*___________________Task 1____________________*/

        tab1 = new JPanel();

        JList listR, listL;
        DefaultListModel rListModel = new DefaultListModel();
        DefaultListModel lListModel = new DefaultListModel();

        listR=new JList(rListModel);
        rListModel.addElement("Это правая колонка!");
        rListModel.addElement("08.12.19");


        listL=new JList(lListModel);
        lListModel.addElement("Это левая колонка!");
        lListModel.addElement(1111);
        lListModel.addElement(12);

        JButton leftList, rightList;
        JPanel panelButtons = new JPanel(new BorderLayout());
        leftList = new JButton("<--");
        rightList = new JButton("-->");

        tab1.setLayout(new BorderLayout());
        tab1.add(listL, BorderLayout.WEST);
        tab1.add(listR, BorderLayout.EAST);
        panelButtons.add(leftList, BorderLayout.SOUTH);
        panelButtons.add(rightList, BorderLayout.NORTH);
        tab1.add(panelButtons, BorderLayout.CENTER);
        tab1.setPreferredSize(new Dimension(500,400));

        //leftList.setSize(new Dimension(50,50));
        //rightList.setSize(new Dimension(50,50));
        listL.setPreferredSize(new Dimension(200,50));
        listR.setPreferredSize(new Dimension(200,50));


        rightList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indices[] =listL.getSelectedIndices();
                moveElements(indices, lListModel, rListModel);
            }
        });
        leftList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indices[] =listR.getSelectedIndices();
                moveElements(indices, rListModel, lListModel);
            }
        });

        contentPane.add(tab1, "Задание 1");

        /*___________________Task 2____________________*/

        tab2 = new JPanel() ;
        tab2.setLayout(new GridLayout(3,5));
        tab2.setPreferredSize(new Dimension(500,400));

        MouseListener listener=new MouseListener() {
            String oldText;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                JButton tempButton;
                tempButton = (JButton) e.getSource();
                oldText =tempButton.getText();
                tempButton.setText("Clicked!");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JButton tempButton;
                tempButton = (JButton) e.getSource();
                tempButton.setText(oldText);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JButton tempButton;
                tempButton = (JButton) e.getSource();
                tempButton.setBackground(Color.blue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton tempButton;
                tempButton = (JButton) e.getSource();
                tempButton.setBackground(Color.orange);
            }
        };

        for (int i = 0 ; i < 15 ; i++){
            JButton button = new JButton();
            button.addMouseListener(listener);
            button.setBackground(Color.ORANGE);
            button.setText("Monday"+i);
            tab2.add(button);
        }
        contentPane.addTab("Задание 2", tab2);

        /*___________________Task 3____________________*/
        tab3 = new JPanel();
        tab3.setLayout(new BoxLayout(tab3, BoxLayout.Y_AXIS));
        final String[] cities = {"Минск", "Гомель", "Брест", "Гродно", "Витебск", "Могилев"};

        ButtonGroup radioGroup=new ButtonGroup();
        for (String city: cities) {

            JRadioButton b = new JRadioButton(city);
            b.setIcon(new ImageIcon( "src\\"+ city+".png"));
            b.setSelectedIcon(new ImageIcon("src\\" +city+"_s.png"));
            b.setRolloverIcon(new ImageIcon("src\\" +city+"_r.png"));
            radioGroup.add(b);
            tab3.add(b);
        }

        contentPane.addTab("Задание 3",tab3);

        add(contentPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                onClose();
            }
        });
    }

     private void moveElements(int[] indices, DefaultListModel from,  DefaultListModel to) {
         for (int index : indices)
             to.addElement(from.get(index));
         for (int i = indices.length-1; i >= 0; i--)
             from.remove(indices[i]);
     }

    private void onClose(){
        dispose();
    }
}


public class Main {

    public static void main(String[] args) {
        MainWindow paint = new MainWindow();
        paint.setVisible(true);
        paint.pack();
    }
}