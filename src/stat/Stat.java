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
    
    public StatDescriptor statDescriptor;
    
    public abstract float getScore();
    
    public abstract void setContainer(StatContainer i);
    
    public abstract void addDependent(Stat s);
    
    public abstract void removeDependent(Stat s);
    
    public abstract void refactor() throws NoSuchStatException;
    
    public abstract void set(float score);
    
    public abstract void modify(float change);
    
    public abstract void modifyBase(float change);
    
    public abstract void removeDependencies();
    
    public abstract void clearDependents();
    
    public abstract Stat copy();
    
}
