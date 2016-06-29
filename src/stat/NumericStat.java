/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Greg
 */
public class NumericStat extends Stat {
    
    private float baseScore;
    private float curScore;
    private HashSet<Stat> dependents;

    public NumericStat(float score) {
        this.baseScore = score;
        this.curScore = score;
        dependents = new HashSet<>();
    }
    
    @Override
    public float getScore() {
        return curScore;
    }

    @Override
    public void setContainer(StatContainer i) {}

    @Override
    public void refactor() throws NoSuchStatException {
        curScore = baseScore;
        for (Stat s : dependents) s.refactor();
    }
    
    public void modify(float change) {
        curScore += change;
        for (Stat s : dependents) try {
            s.refactor();
            } catch (NoSuchStatException ex) {
                Logger.getLogger(NumericStat.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    public void modifyBase(float change) {
        baseScore += change;
        curScore += change;
        for (Stat s : dependents) try {
            s.refactor();
            } catch (NoSuchStatException ex) {
                Logger.getLogger(NumericStat.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    public Stat copy() {
        NumericStat ret = new NumericStat(baseScore);
        ret.modify(curScore - baseScore);
        return ret;
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
    public void removeDependencies() {}

    @Override
    public void set(float score) {
        baseScore = score;
        curScore = score;
    }

    @Override
    public void clearDependents() {
        dependents.clear();
    }
    
}
