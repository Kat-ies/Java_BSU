import java.util.Comparator;

public class Toy  {
    private String name;
    private int cost;
    private int minAge;
    private int maxAge;

    public Toy(){
        name="";
        cost=0;
        minAge=0;
        maxAge=0;
    }

    public Toy(String name, int cost, int minAge, int maxAge){
        this.name=name;
        this.cost=cost;
        this.minAge=minAge;
        this.maxAge=maxAge;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public int getMinAge() {
        return minAge;
    }
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    public int getMaxAge() {
        return maxAge;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Toy toy = (Toy) o;
      if (cost>=toy.cost)
          return false;
        return true;
    }

    @Override
    public String toString() {
        return   name + ", cost=" + cost + ", minAge=" + minAge + ", maxAge=" + maxAge +'\n';
    }

    public static Comparator<Toy> ToyComparator = new Comparator<Toy>() {

        @Override
        public int compare(Toy t1,Toy t2) {
            return t1.getCost() - t2.getCost();
        }
    };
}


