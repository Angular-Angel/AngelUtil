package stat;

/**
 *
 * @author greg
 */
public class Trait extends StatContainer {
    
    protected String name;
    
    public Trait(String name, boolean active){
        this(name, active, new StatContainer());
    }
    
    public Trait(String name, boolean active, StatContainer stats) {
        super(active, stats);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Trait Copy() {
        Trait ret = new Trait(name, isActive());
        for (String s : viewStats().getStatList()) {
            ret.addStat(s, viewStat(s).copy());
        }
        return ret;
    }

}
