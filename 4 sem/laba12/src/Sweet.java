import java.io.Serializable;

public class Sweet implements Serializable {
    private String name;
    private String factory;
    private String index;
    private int weight;

    public Sweet() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFactory() {
        return factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String toXML() {
        return "\t<sweet index=\""+ index +"\" name=\""+name+"\">\n" +
                "\t\t<factory>"+ factory +"</factory>\n" +
                "\t\t<weight>"+ weight +"</weight>\n" +
                "\t</sweet>";
    }
}
