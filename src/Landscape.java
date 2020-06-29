import java.io.Serializable;

public class Landscape implements Serializable {

    protected String name;
    protected int time;
    public boolean isVisited;

    public Landscape(){
        this.isVisited = false;
    }
}
