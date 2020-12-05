import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;


public class Main extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonClose;
    private JTextField firstElem;
    private JLabel NameOfField1;
    private JLabel Name;
    private JTextField koef;
    private JTextField amount;
    private JButton buttonInput;
    private JTextField outputTxtTextField;
    private JComboBox comboBox;
    private JTextArea out;
    private JButton ButtonProgression;
    private JButton ButtonSum;



    Series series;
    String fileName;

    public Main() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        try{enterSeries();}
        catch (MyException r)
        { out.setText(r.getMessage().toString());}
        getFilename();


        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        ButtonProgression.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    enterSeries();
                    FileWriter file =new FileWriter(getFilename());
                    writeSeries();
                    writeSeries(file);
                }
                catch (IOException ex){
                    out.setText("С файлом что-то не так!");
                }
                catch (MyException e1){
                    out.setText(e1.getMessage().toString());
                }
                catch (NumberFormatException c) {
                    out.setText("Вы вводите не числа!");
                }
                }
        });

        ButtonSum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    enterSeries();
                    FileWriter file =new FileWriter(getFilename());
                    writeSum();
                    writeSum(file);
                }
                catch (IOException ex){
                    out.setText("С файлом что-то не так!");
                }
                catch (MyException e1){
                    out.setText(e1.getMessage().toString());
                }
                catch (NumberFormatException c) {
                    out.setText("Вы вводите не числа!");
                }

            }
        });
    }

    private String getFilename(){
        String buf;
        buf = outputTxtTextField.getText();
        if (buf != "")
            fileName = buf;
        else
            out.setText("Неверный ввод имени файла!");
        return buf;
    }

    private void enterSeries() throws MyException, NumberFormatException {

            double d=0,a=0;
        int n=0;

            String buf = firstElem.getText();
            if (buf.length() != 0) {
                a = Double.valueOf(buf);
            }
            else
                throw (new MyException(("Вы ввели пустое поле")));

            buf = koef.getText();
            if (buf.length() != 0) {
                d = Double.valueOf(buf);
            }
            else
                throw (new MyException(("Вы ввели пустое поле")));
            buf = amount.getText();
            if (buf.length() != 0) {
                n = Integer.valueOf(buf);
            }
            else
                throw (new MyException(("Вы ввели пустое поле")));

        if (comboBox.getSelectedIndex()==0)
            series = new Liner(a,d,n);
        else{
            if (Math.abs(d)<=1)
                throw (new MyException(("Данная реализация не поддерживает |q|<=1 для геом.пр.!")));
            else
            series= new Explonential(a,d,n);
        }

    }

    private void writeSeries(FileWriter writer) throws IOException {
        writer.write(series.toString());
        writer.close();
    }

    private void writeSeries() {
        out.setText(series.toString());

    }

    private void writeSum(FileWriter writer) throws IOException {
        writer.write("Ваша сумма: " + series.sum());
        writer.close();
    }

    private void writeSum() {
        out.setText("Ваша сумма: " + series.sum());
    }

    private void onClose() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Main dialog = new Main();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}


abstract class Series{
    protected double a0;
    protected double d;
    protected int n;

     Series( double a0, double d,int n){
        this.a0=a0;
        this.d=d;
        this.n=n;
    }

    abstract protected double getElem (int index);

    protected double sum(){
        double sum=0;
        for (int i=0; i<n; i++){
            sum+=getElem(i);
        }
        return sum;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Ваша прогрессия:");
        for (int i = 1; i <= n; i++) {
            str.append(" " + getElem(i));
        }
        return str.toString();
    }

}

 class MyException extends Exception{
    MyException(){}
    MyException(String str){
        super(str);
    }
}

class Liner extends Series {
    public  Liner( double a0, double d,int n){
        super(a0,d,n);
    }
    protected double getElem(int i){
        return a0+(i-1)*d;
    }
    protected double sum(){ return (a0+getElem(n))*n/2;}
}

class Explonential extends Series {
    public  Explonential( double a0, double d,int n){
        super(a0,d,n);
    }
    protected double getElem(int i){
        return a0*(double) Math.pow((double) d, (double) i - 1);
    }
    protected double sum(){
        return a0*((double) Math.pow((double) d, (double) n )-1)/(d-1);
    }
}
