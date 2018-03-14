/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Greg
 */
public abstract class Stat extends Observable implements Observer {
    
    private final StatDescriptor statDescriptor;
    protected float score;
    
    protected StatContainer mods;
    
    public Stat(StatDescriptor statDescriptor) {
        this.statDescriptor = statDescriptor;
        score = 0;
    }
    
    public StatDescriptor getStatDescriptor() {return statDescriptor;}
    
    public float getScore() {
        return score;
    }
    
    public abstract void setContainer(StatContainer i);
    
    public void refactor() {
        float newScore = refactorBase();
        for (Stat s : mods.getStats().values()) newScore += s.getScore();
        if (score != newScore) {
            float change = newScore - score;
            score = newScore;
            notifyObservers(new StatEvent(StatEvent.Type.DEPENDENCY_CHANGED, this));
        }
    }
    
    public abstract void set(Object obj);
    
    protected abstract float refactorBase();
    
    public void modify(String name, float change) {
        if (mods.hasStat(name)) {
            mods.getStat(name).modifyBase(change);
        }
        else modify(name, new NumericStat(change));
    }
    
    public void modify(String name, Stat change) {
        change.addObserver(this);
        mods.addStat(name, change);
        score += change.getScore();
        notifyObservers(new StatEvent(StatEvent.Type.MOD_ADDED, change));
    }
    
    public abstract void modifyBase(float change);
    
    public void removeMod(String name) {
        Stat mod = mods.getStat(name);
        score -= mod.getScore();
        mods.removeStat(name);
        notifyObservers(new StatEvent(StatEvent.Type.MOD_REMOVED, mod));
    }
    
    public abstract Stat copy();
    
    @Override
    public void update(Observable o, Object arg) {
        refactor();
    }
    
}
