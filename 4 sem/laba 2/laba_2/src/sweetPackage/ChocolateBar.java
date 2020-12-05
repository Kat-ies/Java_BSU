package sweetPackage;

public class ChocolateBar extends Sweet {
    private String filling;

    public ChocolateBar() {
    }
    public ChocolateBar(String name, int weight, int sugar, int shelfTimeDays, String filling) {
        super(name, weight, sugar, shelfTimeDays);
        this.filling = filling;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", filling: " + filling;
    }

    public String getFilling() {
        return filling;
    }
    public void setFilling(String filling) {
        this.filling = filling;
    }
}
