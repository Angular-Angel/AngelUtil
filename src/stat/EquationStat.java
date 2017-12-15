
package stat;

import com.udojava.evalex.Expression;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angle
 */


public class EquationStat extends Stat {

    public String equation;
    protected float score;
    protected StatContainer container;
    protected HashSet<Stat> dependents;
    
    public EquationStat(String equation) {
        this(null, equation);
    }
    
    public EquationStat(StatDescriptor statDescriptor, String string) {
        super(statDescriptor);
        equation = string;
        score = 0;
        dependents = new HashSet<>();
        mods = new StatContainer();
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
                container.getStat(statName).addDependent(this);
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
        try {
            return e.eval().floatValue();
        } catch(Exception ex) {
            System.err.println(equation);
            Logger.getLogger(EquationStat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

     @Override
    public void modify(String name, Stat change) {
        mods.addStat(name, change);
        score += change.getScore();
        for (Stat s : dependents)
            s.refactor();
    }
    
    @Override
    public void removeMod(String name) {
        score -= mods.getScore(name);
        mods.removeStat(name);
        for (Stat s : dependents)
            s.refactor();
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
                container.getStat(statName).removeDependent(this);
            }
    }

    @Override
    public Stat copy() {
        EquationStat ret = new EquationStat(getStatDescriptor(), equation);
        ret.mods.addAllStats(mods.viewStats());
        return ret;
    }

    @Override
    public void set(float score) {
        throw new UnsupportedOperationException("Can't set EquationStat"); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clearDependents() {
        dependents.clear();
    }

}
