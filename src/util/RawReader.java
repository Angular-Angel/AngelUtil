package util;

import stat.StatContainer;
import stat.EquationStat;
import stat.Stat;
import stat.BinaryStat;
import stat.NumericStat;
import groovy.lang.GroovyClassLoader;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import stat.StatDescriptor;

/**
 *
 * @author Greg
 */
public class RawReader {
    
    public RawReader() {
    }
    
    public StatDescriptor readStatDescriptor(JSONObject obj) {
        String identifier = (String) obj.get("Identifier");
        String name = (String) obj.get("Name");
        Stat stat = readJSONStat((JSONArray) obj.get("Stat"));
        float base = ((Double) obj.get("Base")).floatValue();
        float increase = ((Double) obj.get("Increase")).floatValue();
        
        Stat.Type type = Stat.Type.valueOf((String) obj.get("Stat Type"));
        
        StatDescriptor ret = new StatDescriptor(identifier, name, stat, type, base, increase);
        return ret;
    }
    
    protected Color readJSONColor(JSONArray ja) {
        return new Color(((Long) ja.get(0)).intValue(), 
                ((Long) ja.get(1)).intValue(), 
                ((Long) ja.get(2)).intValue());
    }
    
    protected Stat readJSONStat(JSONArray statArray) {
        Stat stat = null; //initialize return variable
        if (statArray.size() == 1) //is it a binarty stat?
            stat = new BinaryStat(); //if so, return that.
        else {
            Object o = statArray.get(1);
            if (o instanceof Long) { //is it a numeric stat? 
                stat = new NumericStat(((Long) o).intValue()); //if so, return that.
            } else if (o instanceof Double) { //Also checking for numeric stat.
                stat = new NumericStat(((Double) o).floatValue());
            } else if (o instanceof String) {
                stat = new EquationStat((String) o);
            }
        }
        return stat;
    }
    
    protected StatContainer readJSONStats(JSONArray stats) {
        StatContainer ret = new StatContainer() {};
        for (int i = 0; i < stats.size(); i++) {
            Stat stat = null;
            JSONArray statArray = (JSONArray) stats.get(i);
            stat = readJSONStat(statArray);
            ret.addStat((String) ((JSONArray) stats.get(i)).get(0), stat);

        }
        return ret;
    }
    
    public Object readGroovyScript(File file) {
        try {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Object reactionScript = gcl.parseClass(file).newInstance();
            return reactionScript;
        } catch (CompilationFailedException | IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RawReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Object readGroovyScript(String text) {
        try {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Object reactionScript = gcl.parseClass(text).getConstructor().newInstance();
            return reactionScript;
        } catch (CompilationFailedException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RawReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
