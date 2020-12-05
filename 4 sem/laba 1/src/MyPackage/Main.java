package MyPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Введите дату в формате DD.MM.YYYY");
            Year myYear =new Year(in);


            System.out.println("Ваша дата:");
            System.out.println (myYear.toString());

            System.out.println("День недели по заданной дате:");
            System.out.println (myYear.dayOfWeek());

            System.out.println("__________Временной промежуток_________");
            System.out.println("Введите промежуток в формате DD.MM.YYYY DD.MM.YYYY");

            Scanner sc = new Scanner(System.in);
            String st=sc.next();
            System.out.println( myYear.interval(st,sc.next()));
        }
        catch (NumberFormatException ex)
        {
            System.out.println ("Wrong Format!");
        }
        catch (InputMismatchException ex){
            System.out.println ("Wrong format!");
        }
        catch (MyException ex){
            System.out.println (ex.getMessage());
        }

        catch (DateTimeParseException exc){
            System.out.println ("Плохое время для задания! Введите время в пределах 01.01.1000 - 31.12.9999");
        }
        catch (DateTimeException ex) {
            System.out.println ("Нет такой даты");
        }

    }
}


