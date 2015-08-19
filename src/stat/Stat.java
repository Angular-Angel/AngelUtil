/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

/**
 *
 * @author Greg
 */
public interface Stat {
    
    public float getScore();
    
    public void setContainer(StatContainer i);
    
    public void addDependent(Stat s);
    
    public void removeDependent(Stat s);
    
    public void refactor() throws NoSuchStatException;
    
    public void set(float score);
    
    public void modify(float change);
    
    public void modifyBase(float change);
    
    public void removeDependencies();
    
    public Stat copy();
    
}
