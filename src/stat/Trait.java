package stat;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import stat.NoSuchStatException;
import stat.Stat;
import stat.StatContainer;

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
        Trait ret = new Trait(name, active);
        for (String s : viewStats().getStatList()) {
            try {
                ret.addStat(s, viewStat(s).copy());
            } catch (NoSuchStatException ex) {
                Logger.getLogger(Trait.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

}
