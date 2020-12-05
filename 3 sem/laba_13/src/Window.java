import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
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
    private JFileChooser fileChooser;
    private File file;



    ArrayList<Toy> toys= new ArrayList<>();


    Window(){
        setContentPane(mainPane);
        setTitle("Каталог игрушек");
        setPreferredSize(new Dimension(620,500));
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
                        readFile(file, toys);
                            textPane.setText(print(toys));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Window.this, "problems with file!");
                    }
                    catch (MyException ex){
                        JOptionPane.showMessageDialog(Window.this, ex.getMessage());
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
                    //List<Toy> buffer = new ArrayList<Toy>();

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

                    Collections.sort(buffer,Toy.ToyComparator);

               /* int firstIndex = Collections.binarySearch(buffer, t,Toy.ToyComparator);
               ListIterator<Toy> it = buffer.listIterator(firstIndex);
                for ( ; it.hasNext() ; ) {
                        it.remove();
                }
                    textPane.setText(print((ArrayList<Toy>)buffer));*/
                   textPane.setText(print(buffer,t));
                }

                    catch (MyException ex){
              JOptionPane.showMessageDialog(Window.this, ex.getMessage());
          }
            }
        }) ;

    }

    private void onClose() {
        // add your code here if necessary
        dispose();
    }

    public  void readFile(File file, ArrayList<Toy> toys) throws FileNotFoundException,
            NoSuchElementException, MyException{
        Scanner scanner= new Scanner(file);
        String name;
        int cost;
        int minAge,maxAge;
        toys.clear();
        while (scanner.hasNext()){

            name=scanner.next();
            cost=scanner.nextInt();
            String bufString=scanner.next();
            Pattern pattern = Pattern.compile("[1-9]{1,2}[0-9]{0,2}[\\-][1-9]{1,2}[0-9]{0,2}");
            Matcher matcher = pattern.matcher(bufString);
            boolean matches = matcher.matches();
            if (!matches)
                throw (new MyException("Ошибка регулярного выражения. Ваш возраст не соответсвует формату *-*"));
            StringTokenizer buf= new StringTokenizer(bufString,"-");
            minAge=Integer.parseInt(buf.nextToken());
            maxAge=Integer.parseInt(buf.nextToken());
          //  if (minAge<0 || maxAge<0)
          //      throw (new MyException("Возраст не может быть отрицательным!"));
            if (maxAge>140 || minAge>140)
                throw (new MyException("Люди так долго не живут!"));
            toys.add(new Toy(name,cost,minAge,maxAge));
        }

    }

    private String print(List<Toy> toys, Toy t){
        StringBuffer st=new StringBuffer();

        toys.stream().filter(x->(t.getMinAge()>=x.getMinAge() && t.getMaxAge()<=x.getMaxAge())&&x.getCost()<t.getCost()).forEach(x->st.append(x.toString()));
        return st.toString();
    }

    private String print(ArrayList<Toy> toys){
        StringBuffer st=new StringBuffer();

        ListIterator<Toy> it = toys.listIterator();
        for ( ; it.hasNext() ; )
            st.append(it.next().toString());
        return st.toString();
    }

    public int findFirst (List<Toy> toys, int cost){
        ListIterator<Toy> it = toys.listIterator(0);
        int i=0;
         for ( ; it.hasNext() ; ) {
             if(it.next().getCost()>=cost)
                 return i;
             i++;
         }
         return i+1;
    }
}



class MyException extends Exception{
    MyException(){}
    MyException(String str){
        super(str);
    }
}