package util;

import stat.StatContainer;
import stat.EquationStat;
import stat.Stat;
import stat.NumericStat;
import groovy.lang.GroovyClassLoader;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stat.StatDescriptor;

/**
 *
 * @author Greg
 */
public class RawReader {
    
    
    public HashMap<String, StatDescriptor> statDescriptors;
    
    public RawReader() {
        statDescriptors = new HashMap<>();
    }
    
    public StatDescriptor readStatDescriptor(JSONObject obj) {
        String identifier = (String) obj.get("Identifier");
        String name = (String) obj.get("Name");
        float base = ((Double) obj.get("Base")).floatValue();
        float increase = ((Double) obj.get("Increase")).floatValue();
        
        StatDescriptor.StatType type = StatDescriptor.StatType.valueOf((String) obj.get("Stat Type"));
        
        StatDescriptor ret = new StatDescriptor(identifier, name, null, type, base, increase);
        ret.stat = readJSONStat(ret, obj.get("Stat"));
        return ret;
    }
    
    protected Color readJSONColor(JSONArray ja) {
        return new Color(((Long) ja.get(0)).intValue(), 
                ((Long) ja.get(1)).intValue(), 
                ((Long) ja.get(2)).intValue());
    }
    
    protected Stat readJSONStat(JSONArray statArray) {
        if (statArray.size() < 2)
            System.out.println(statArray);
        Object o = statArray.get(1);
        return readJSONStat(o);
    }
    
    protected StatContainer readJSONStats(JSONArray stats) {
        StatContainer ret = new StatContainer();
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i) instanceof JSONArray) {
                JSONArray statArray = (JSONArray) stats.get(i);
                String statName = (String) ((JSONArray) stats.get(i)).get(0);
                StatDescriptor statDescriptor = statDescriptors.get(statName);
                Stat stat;
                if (statArray.size() > 1)
                    stat = readJSONStat(statDescriptor, statArray.get(1));
                else 
                    stat = statDescriptor.stat.copy();
                ret.addStat((String) ((JSONArray) stats.get(i)).get(0), stat);
            } else if (stats.get(i) instanceof String) {
                StatDescriptor statDescriptor = statDescriptors.get((String) stats.get(i));
                if (statDescriptor == null) System.out.println((String) stats.get(i));
                ret.addStat((String) statDescriptor.identifier, statDescriptor.stat.copy());
            }
        }
        return ret;
    }
    
    
    protected Stat readJSONStat(StatDescriptor statDescriptor, Object o) {
        Stat stat = null; //initialize return variable
        if (o instanceof Long) { //is it a numeric stat? 
            stat = new NumericStat(statDescriptor, ((Long) o).intValue()); //if so, return that.
        } else if (o instanceof Double) { //Also checking for numeric stat.
            stat = new NumericStat(statDescriptor, ((Double) o).floatValue());
        } else if (o instanceof String) {
            stat = new EquationStat(statDescriptor, (String) o);
        }
        return stat;
    }
    
    protected Stat readJSONStat(Object o) {
        Stat stat = null; //initialize return variable
        if (o instanceof Long) { //is it a numeric stat? 
            stat = new NumericStat(((Long) o).intValue()); //if so, return that.
        } else if (o instanceof Double) { //Also checking for numeric stat.
            stat = new NumericStat(((Double) o).floatValue());
        } else if (o instanceof String) {
            stat = new EquationStat((String) o);
        }
        return stat;
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
            Object groovyScript = gcl.parseClass(text).getConstructor().newInstance();
            return groovyScript;
        } catch (CompilationFailedException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RawReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void readJSONStatDescriptions(File file) {
        JSONParser parser = new JSONParser();
	try {
            JSONArray statDescriptions = (JSONArray) parser.parse(new FileReader(file));
            for (Object e : statDescriptions) {
                StatDescriptor statDescriptor = readStatDescriptor((JSONObject) e);
                statDescriptors.put(statDescriptor.identifier, statDescriptor);
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(RawReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
