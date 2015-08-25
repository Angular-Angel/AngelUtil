package util;

import stat.DerivedStat;
import stat.StatContainer;
import stat.ModifiedStat;
import stat.EquationStat;
import stat.Stat;
import stat.BinaryStat;
import stat.NumericStat;
import groovy.lang.GroovyClassLoader;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.simple.JSONArray;

/**
 *
 * @author Greg
 */
public class RawReader {
    
    public RawReader() {
    }
    
    protected Color readJSONColor(JSONArray ja) {
        return new Color(((Long) ja.get(0)).intValue(), 
                ((Long) ja.get(1)).intValue(), 
                ((Long) ja.get(2)).intValue());
    }
    
    protected StatContainer readJSONStats(JSONArray stats) {
        StatContainer ret = new StatContainer() {};
        for (int i = 0; i < stats.size(); i++) {
            Stat stat = null;
            if (((JSONArray) stats.get(i)).size() == 1)
                stat = new BinaryStat();
            else {
                Object o = ((JSONArray) stats.get(i)).get(1);
                if (o instanceof Long) {
                    stat = new NumericStat(((Long) o).intValue());
                } else if (o instanceof Double) {
                    stat = new NumericStat(((Double) o).floatValue());
                } else if (o instanceof JSONArray) {
                    JSONArray oa = (JSONArray) o;
                    if (oa.get(1) instanceof String) {
                        String s1 = (String) oa.get(0);
                        String s2 = (String) oa.get(1);
                        char operand = ((String) oa.get(2)).charAt(0);
                        stat = new DerivedStat(s1, s2, operand);
                    } else if (oa.get(1) instanceof Double) {
                        String s1 = (String) oa.get(0);
                        float mod = ((Double) oa.get(1)).floatValue();
                        char operand = ((String) oa.get(2)).charAt(0);
                        stat = new ModifiedStat(s1, mod, operand);
                    } else if (oa.get(1) instanceof Long) {
                        String s1 = (String) oa.get(0);
                        float mod = ((Long) oa.get(1)).floatValue();
                        char operand = ((String) oa.get(2)).charAt(0);
                        stat = new ModifiedStat(s1, mod, operand);
                    }

                } else if (o instanceof String) {
                    stat = new EquationStat((String) o);
                }
            }
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
            Object reactionScript = gcl.parseClass(text).newInstance();
            return reactionScript;
        } catch (CompilationFailedException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RawReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
