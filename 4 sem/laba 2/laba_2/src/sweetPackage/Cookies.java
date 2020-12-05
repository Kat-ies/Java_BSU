package sweetPackage;

public class Cookies extends Sweet {
    private boolean cacaoAdded;

    public Cookies(String name, int weight, int sugar, int shelfTimeDays, boolean cacaoAdded) {
        super(name, weight, sugar, shelfTimeDays);
        this.cacaoAdded = cacaoAdded;
    }

    @Override
    public String toString() {
        return super.toString() + ((cacaoAdded) ? ", with cacao" : ", without cacao");
    }

    public boolean isCacaoAdded() {
        return cacaoAdded;
    }
    public void setCacaoAdded(boolean cacaoAdded) {
        this.cacaoAdded = cacaoAdded;
    }
}
