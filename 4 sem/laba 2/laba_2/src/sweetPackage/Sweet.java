package sweetPackage;

public class Sweet {
    private String name;
    private int weight, sugar, shelfTimeDays;

    public Sweet() {
    }

    public Sweet(String name, int weight, int sugar, int shelfTimeDays) {
        this.name = name;
        this.weight = weight;
        this.sugar = sugar;
        this.shelfTimeDays = shelfTimeDays;
    }

    @Override
    public String toString() {
        return "Sweet name: " + name +
                ", weight: " + weight +
                ", sugar: " + sugar + ", shelf time: " + shelfTimeDays + " days";
    }

    public int getShelfTimeDays() {
        return shelfTimeDays;
    }
    public void setShelfTimeDays(int shelfTimeDays) {
        this.shelfTimeDays = shelfTimeDays;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getSugar() {
        return sugar;
    }
    public void setSugar(int sugar) {
        this.sugar = sugar;
    }
}
