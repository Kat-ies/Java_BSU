package MyPackage;

import java.util.Objects;

public class Month {
    private int month;

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Month)) return false;
        Month month1 = (Month) o;
        return month == month1.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month);
    }

    @Override
    public String toString() {
        return "Month: " + month ;
    }

    public Month(int month) throws MyException {
        if (month>0&& month<13 )
        this.month = month;
        else throw new MyException("Wrong month!");
    }

    public Month(){this.month=1;}
}