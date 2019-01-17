package sample;

public class Composition {

    private String compName;
    private String compId;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Composition(String compId, String compName) {
        this.compName = compName;
        this.compId = compId;
    }
}
