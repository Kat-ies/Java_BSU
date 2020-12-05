package MyPackage;

import java.util.Objects;


public class Day {
    private int day;

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public Day(int day) throws MyException {
        if (day>0 && day<32)
        this.day = day;
        else throw new MyException("Wrong day!");
    }

    public  Day(){
        this.day=1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Day)) return false;
        Day day1 = (Day) o;
        return day == day1.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day);
    }

    @Override
    public String toString() {
        return "Day: " + day;
    }
}

class MyException extends Exception {
    MyException() {
    }
    MyException(String str) {
        super(str);
    }
}

