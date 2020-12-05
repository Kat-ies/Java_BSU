package MyPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Year {
    private int year;
    private Month month;
    private Day day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }


    public void setDate(int year, int month, int day) throws MyException, DateTimeException {
        this.year = year;
        this.month = new Month(month);
        this.day = new Day(day);
    }

    public String dayOfWeek() {
        String variants[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        GregorianCalendar calendar = new GregorianCalendar(this.year, this.month.getMonth() - 1, this.day.getDay());
        // calendar.setFirstDayOfWeek(1);
        int number = calendar.get(calendar.DAY_OF_WEEK);
        return variants[number - 1];
    }

    public String interval(String begin, String end) throws MyException {
        if (!(isLegalDate(begin)))
            throw (new MyException("Такой начальной даты в календаре нет!"));

        if (!(isLegalDate(end)))
            throw (new MyException("Такой даты на 2 позиции в календаре нет!"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(begin, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        Period period = Period.between(startDate, endDate);

        if (period.getYears()<0 || period.getMonths() <0 || period.getDays()<0)
            throw (new MyException("Дата начала > даты конца!"));
        return ("Количество прошедших лет: " + period.getYears() + '\n'
                + "Количество прошедших месяцев: " + period.getMonths() + '\n'
                + "Количество прошедших дней: " + period.getDays());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Year)) return false;
        Year year1 = (Year) o;
        return year == year1.year &&
                Objects.equals(month, year1.month) &&
                Objects.equals(day, year1.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return "Year: " + year + '\n' + month.toString() + '\n' + day.toString();
    }

    public Year(int year, int month, int day) throws MyException, DateTimeException {
        if (year > 0) {

            GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
            calendar.getTime();

            this.year = year;
            this.month = new Month(month);
            this.day = new Day(day);

        } else throw new MyException("Wrong year!");
    }

    public Year() {
        this.year = 1;

    }


    public static boolean isLegalDate(String s) throws DateTimeParseException {
        final DateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return DATEFORMAT.format(DATEFORMAT.parse(s)).equals(s);
        } catch (ParseException ex) {
            return false;
        }
    }

    public Year (Scanner in) throws  MyException, DateTimeException{
        String test=in.next();

        Pattern pattern = Pattern.compile("[0-9]{1,2}[.][0-9]{1,2}[.][0-9]{4}");
        Matcher matcher = pattern.matcher(test);
        boolean matches = matcher.matches();
        if (!matches)
            throw (new MyException("Ошибка регулярного выражения. Ваша дата не соответсвует формату. Введите время в пределах 01.01.1000 - 31.12.9999 !"));
        if ( ! (isLegalDate(test)))
            throw (new MyException("Такой даты в календаре нет!"));

        StringTokenizer buf= new StringTokenizer(test,".");
        int day,month,year;
        day=Integer.parseInt(buf.nextToken());
        month=Integer.parseInt(buf.nextToken());
        year=Integer.parseInt(buf.nextToken());

        this.year = year;
        this.month = new Month(month);
        this.day = new Day(day);
    }
}