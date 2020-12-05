package sweetPackage;

public class Meringue extends Sweet {
    private String supplements;

    public Meringue(String name, int weight, int sugar, int shelfTimeDays, String supplements) {
        super(name, weight, sugar, shelfTimeDays);
        this.supplements = supplements;
    }

    @Override
    public String toString() {
        return super.toString() + ", with supplements: " + supplements;
    }

    public String getSupplements() {
        return supplements;
    }
    public void setSupplements(String supplements) {
        this.supplements = supplements;
    }
}
