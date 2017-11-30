/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import java.util.HashSet;

/**
 *
 * @author Greg
 */
public class NumericStat implements Stat {
    
    private float baseScore;
    private float curScore;
    private final HashSet<Stat> dependents;
    private StatDescriptor statDescriptor;

    public NumericStat(float score) {
        this(null, score);
    }
    
    public NumericStat(StatDescriptor statDescriptor, float score) {
        this.baseScore = score;
        this.curScore = score;
        this.statDescriptor = statDescriptor;
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
    
    @Override
    public void modify(float change) {
        curScore += change;
        for (Stat s : dependents)
            s.refactor();
    }

    @Override
    public void modifyBase(float change) {
        baseScore += change;
        curScore += change;
        for (Stat s : dependents)
            s.refactor();
    }

    @Override
    public Stat copy() {
        NumericStat ret = new NumericStat(statDescriptor, baseScore);
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

    @Override
    public StatDescriptor getStatDescriptor() {
        return statDescriptor;
    }
    
}
