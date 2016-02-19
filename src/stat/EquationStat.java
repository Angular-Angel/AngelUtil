
package stat;

import com.udojava.evalex.Expression;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angle
 */


public class EquationStat implements Stat {

    public String equation;
    protected float score, addition;
    protected StatContainer container;
    protected HashSet<Stat> dependents;
    
    public EquationStat(String string) {
        equation = string;
        addition = 0;
        score = 0;
        dependents = new HashSet<>();
    }
    
    @Override
    public float getScore() {
        return score;
    }

    @Override
    public void setContainer(StatContainer container) {
        if (this.container != null) removeDependencies();
        this.container = container;
        this.dependents = new HashSet<>();
        for (int i = 0; i < equation.length(); i++)
            if (equation.charAt(i) == '[') {
                int j = i; 
                while (equation.charAt(i) != ']') {
                    i++;
                }
                String statName = equation.substring(j+1, i);
                try {
                    container.getStat(statName).addDependent(this);
                } catch (NoSuchStatException ex) {
//                    Logger.getLogger(EquationStat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    @Override
    public void addDependent(Stat s) {
        dependents.add(s);
    }

    @Override
    public void removeDependent(Stat s) {
        dependents.remove(s);
    }

    @Override
    public void refactor() throws NoSuchStatException {
        score = parse(equation);
        modify(addition);
    }
    
    public float parse(String string) throws NoSuchStatException {
        
        String ret = string;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '[') {
                int j = i; 
                while (equation.charAt(i) != ']') {
                    i++;
                }
                String statName = string.substring(j+1, i);
                CharSequence target = '[' + statName + ']';
                CharSequence replace = "" + container.getScore(statName);
                ret = ret.replace(target, replace);
            }
        }
        
        Expression e = new Expression(ret);
        return e.eval().floatValue();
    }

    @Override
    public void modify(float change) {
        score += change;
        for (Stat s : dependents) try {
            s.refactor();
        } catch (NoSuchStatException ex) {
            Logger.getLogger(ModifiedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modifyBase(float change) {
        addition += change;
        modify(change);
    }

    @Override
    public void removeDependencies() {
        for (int i = 0; i < equation.length(); i++)
            if (equation.charAt(i) == '[') {
                int j = i; 
                while (equation.charAt(i) != ']') {
                    i++;
                }
                String statName = equation.substring(j+1, i);
                try {
                    container.getStat(statName).removeDependent(this);
                } catch (NoSuchStatException ex) {
                    Logger.getLogger(EquationStat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    @Override
    public Stat copy() {
        EquationStat ret = new EquationStat(equation);
        ret.modifyBase(addition);
        return ret;
    }

    @Override
    public void set(float score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clearDependents() {
        dependents.clear();
    }
    
}
