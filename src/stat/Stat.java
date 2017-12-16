/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

/**
 *
 * @author Greg
 */
public abstract class Stat {
    
    private StatDescriptor statDescriptor;
    
    protected StatContainer mods;
    
    public Stat(StatDescriptor statDescriptor) {
        this.statDescriptor = statDescriptor;
    }
    
    public StatDescriptor getStatDescriptor() {return statDescriptor;}
    
    public abstract float getScore();
    
    public abstract void setContainer(StatContainer i);
    
    public abstract void addDependent(Stat s);
    
    public abstract void removeDependent(Stat s);
    
    public abstract void refactor() throws NoSuchStatException;
    
    public abstract void set(float score);
    
    public void modify(String name, float change) {
        if (mods.hasStat(name))
            ((NumericStat) mods.getStat(name)).modifyBase(change);
        else modify(name, new NumericStat(change));
    }
    
    public abstract void modify(String name, Stat change);
    
    public abstract void removeMod(String name);
    
    public abstract void removeDependencies();
    
    public abstract void clearDependents();
    
    public abstract Stat copy();
    
}
