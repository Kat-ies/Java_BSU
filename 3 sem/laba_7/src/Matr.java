import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Matr {

    public static final double EPS = 1e-10;

    static class MyException extends Exception {
        MyException() {
        }

        MyException(String str) {
            super(str);
        }
    }

    public static void main(String[] args) {
        File input = new File("input.txt");
        try {
            Scanner scan = new Scanner(input);
            int size = scan.nextInt();
            double[][] matrix = new double[size][size];
            double[][] matrixCheck = new double[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                matrix[i][j]=matrixCheck[i][j] = scan.nextInt();

            double[] x = new double[size];
            double[] b = new double[size];
            for (int i = 0; i < size; i++) {
                x[i] = 0;
                b[i] = scan.nextInt();
            }
            int a;
            if(scan.hasNext())
                throw  new MyException("too much data");


          //  String str= scan.nextLine();
          //  if (str!="end")
            //    throw new MyException ("too much data!");

            System.out.println("Your matrix:");
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(" " + matrix[i][j] + " ");
                }
                System.out.println("|" + " " + b[i]);
            }

            //   System.out.println(Arrays.deepToString(matrix;));
            x = findSolution(matrix, b);
            System.out.println("Your Solution:");
            for (int i = 0; i < matrix.length; i++) {
                System.out.println(x[i]);
            }
            System.out.println("Checkpoint: Ax=");
            double[] bNew=aX(matrixCheck,x);
            for (int i = 0; i < matrixCheck.length; i++) {
                System.out.println(  bNew[i]);
            }

        }
        catch (MyException e) {
            System.out.println(e.getMessage());
        }
        catch (IndexOutOfBoundsException e) { //обращение к массиву по некорректному индексу
            System.out.println("Wrong index");
        }
        catch (InputMismatchException e) {
            System.out.println("Wrong data!");
        }
        catch (NoSuchElementException e) {
            System.out.println("not enough data!");
        }
        catch (Exception e) {
            System.out.println("FileNotFound!");
        }
    }

    public static double[] findSolution(double[][] matrix, double[] b) throws  Exception, MyException  {
        double[][] mas=matrix;
        double buf[] = new double[mas.length];
        for (int i = 0; i < buf.length; i++)
            buf[i] = 0;
        int len = mas.length;
        //прямой ход метода Гаусса
        double koofAlfa = 0;
        int posGlavEl = 0;
        while (posGlavEl < len - 1) {
            double maxElement = mas[posGlavEl][posGlavEl];
            int indexMaxElementCol = posGlavEl;
            //Поиск максимального элемента
            for (int j = posGlavEl; j < len; j++)
                if (Math.abs(maxElement) < Math.abs(mas[j][posGlavEl])) {
                    maxElement = mas[j][posGlavEl];
                    indexMaxElementCol = j;
                }

            if (Math.abs(maxElement)< EPS)
                throw new MyException("The determinant of the matrix is zero!");

            double a = b[posGlavEl];
            b[posGlavEl] = b[indexMaxElementCol];
            b[indexMaxElementCol]=a;

            double[][] masStr = new double[1][len];
                masStr[0] = mas[posGlavEl];
                mas[posGlavEl] = mas[  indexMaxElementCol];
                mas[  indexMaxElementCol] = masStr[0];

            // Очередной раз применяем диагонализацию
            for (int str = posGlavEl + 1; str < len; ++str) {
                koofAlfa = mas[str][posGlavEl] / mas[posGlavEl][posGlavEl];
                for (int col = 0; col < len; ++col) {
                    mas[str][col] -= koofAlfa * mas[posGlavEl][col];
                }
                b[str] -= koofAlfa * b[posGlavEl];
            }
            ++posGlavEl;

        }
        double chek=1;
        for (int i=0; i<len;i++){
            chek*=mas[i][i];
        }
        if (Math.abs(chek)< EPS)
            throw new MyException("The determinant of the matrix is zero!");
        //обратный ход метода Гаусса
        posGlavEl = len - 1;
        while (posGlavEl >= 0) {
            for (int str = len - 1; str >= 0; --str) {
                for (int col = 0; col < len; ++col) {
                    b[str] -= mas[str][col] * buf[col];
                }
                buf[posGlavEl] = b[posGlavEl] / mas[posGlavEl][posGlavEl];
                --posGlavEl;
            }
        }
     //   double bufForReturn[] = new double[mas.length];
        //int k = 0;
      //  for (int i = buf.length - 1; i >= 0; i--, k++) {
       //     bufForReturn[k] = buf[i];
      //  }

        for (int i=0; i <len; i++)
            buf[i]= roundAvoid(buf[i], 2);

        return buf;
    }
    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    public static double[] aX (double[][] mass, double[] xChek) throws  Exception, MyException{
        double[] b=new double[xChek.length];
        for (int i=0; i<b.length;i++)
            b[i]=0;
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b.length; j++)
            {
                double buf = mass[i][j];
                buf*= xChek[j];
                b[i] +=  buf;
            }
        return b;
    }
}

/*подумать:
1) ввод через функцию
2) сеттеры
3) рациональный вывод
 */