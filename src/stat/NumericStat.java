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
public class NumericStat extends Stat {
    
    private float baseScore;
    private float curScore;
    private final HashSet<Stat> dependents;

    public NumericStat(float score) {
        this(null, score);
    }
    
    public NumericStat(StatDescriptor statDescriptor, float score) {
        super(statDescriptor);
        this.baseScore = score;
        this.curScore = score;
        dependents = new HashSet<>();
        mods =  new StatContainer();
    }
    
    @Override
    public float getScore() {
        return curScore;
    }

    @Override
    public void setContainer(StatContainer i) {}

    @Override
    public void refactor() {
        curScore = baseScore;
        for (Stat s : dependents) s.refactor();
    }
    
    public void modifyBase(float change) {
        baseScore += change;
        curScore += change;
        for (Stat s : dependents)
            s.refactor();
    }
 
    
    @Override
    public void modify(String name, Stat change) {
        mods.addStat(name, change);
        curScore += change.getScore();
        for (Stat s : dependents)
            s.refactor();
    }
    
    @Override
    public void removeMod(String name) {
        curScore -= mods.getScore(name);
        mods.removeStat(name);
        for (Stat s : dependents)
            s.refactor();
    }

    @Override
    public Stat copy() {
        NumericStat ret = new NumericStat(getStatDescriptor(), baseScore);
        ret.mods.addAllStats(mods.viewStats());
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
