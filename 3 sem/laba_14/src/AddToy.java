import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddToy extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JTextField costTextField;
    private JTextField ageTextField;

    public AddToy(ArrayList<Toy> toys) {
        setContentPane(contentPane);
       setTitle("Введите данные");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try{
               toys.add(onOK());
               onCancel();
            } catch (NumberFormatException exc){
                JOptionPane.showMessageDialog(null, "Неверный ввод стоимости");
            } catch (MyException exc){
                JOptionPane.showMessageDialog(null, exc.getMessage());
            }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private Toy onOK() throws MyException, NumberFormatException   {
        String name;
        int cost, minAge, maxAge;
        name= nameTextField.getText();
        cost= Integer.parseInt(costTextField.getText());
        if (cost<0)
            throw (new MyException("Стоимость не может быть отрицательной!"));

        Pattern pattern = Pattern.compile("[1-9]{1,2}[0-9]{0,2}[\\-][1-9]{1,2}[0-9]{0,2}");
        Matcher matcher = pattern.matcher(ageTextField.getText());
        boolean matches = matcher.matches();
         if (!matches)
             throw (new MyException("Ошибка регулярного выражения. Ваш возраст не соответсвует формату *-*"));
        StringTokenizer buf= new StringTokenizer(ageTextField.getText(),"-");
        minAge=Integer.parseInt(buf.nextToken());
        maxAge=Integer.parseInt(buf.nextToken());
       // if (minAge<0 || maxAge<0)
        //    throw (new MyException("Возраст не может быть отрицательным!"));
        if (maxAge>140 || minAge>140)
            throw (new MyException("Люди так долго не живут!"));
        return new Toy(name,cost,minAge,maxAge);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
