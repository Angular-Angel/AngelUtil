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
public class DerivedStat implements Stat{
    private StatContainer i;
    private String s1, s2;
    private float score, addition;
    private char type;
    private HashSet<Stat> dependents;
    
    public DerivedStat(StatContainer i, String s1, String s2) {
        this(i, s1, s2, '*');
    }
    
    public DerivedStat(StatContainer i, String s1, String s2, char type) {
        this.s1 = s1;
        this.s2 = s2;
        this.type = type;
        this.i = i;
        dependents = new HashSet<>();
        addition = 0;
    }
    
    public DerivedStat(String s1, String s2) {
        this(s1, s2, '*');
    }
    
    public DerivedStat(String s1, String s2, char type) {
        this.s1 = s1;
        this.s2 = s2;
        this.type = type;
        dependents = new HashSet<>();
        addition = 0;
    }
    
    @Override
    public float getScore() {
        return score;
    }

    @Override
    public void setContainer(StatContainer i) {
        if (this.i != null) removeDependencies();
        try {
            this.i = i;
            i.getStat(s1).addDependent(this);
            i.getStat(s2).addDependent(this);
        } catch (NoSuchStatException ex) {
            Logger.getLogger(DerivedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refactor() {
        
        try {
            switch(type) {
                case '*': score = i.getScore(s1) * i.getScore(s2);
                    break;
                case '/': score = i.getScore(s1) / i.getScore(s2);
                    break;
                case '+': score = i.getScore(s1) + i.getScore(s2);
                    break;
                case '-': score = i.getScore(s1) - i.getScore(s2);
                    break;
                default: System.out.println("Derived Stat: operator not recognized");
            }
            modify(addition);
            for (Stat s : dependents) s.refactor();
        } catch(NoSuchStatException ex) {
            Logger.getLogger(DerivedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modify(float change) {
        score += change;
        for (Stat s : dependents) try {
            s.refactor();
            } catch (NoSuchStatException ex) {
                Logger.getLogger(DerivedStat.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    public void modifyBase(float change) {
        addition += change;
        modify(change);
    }

    @Override
    public Stat copy() {
        DerivedStat ret = new DerivedStat(s1, s2, type);
        ret.modifyBase(score);
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
    public void removeDependencies() {
        try {
            i.getStat(s1).removeDependent(this);
            i.getStat(s2).removeDependent(this);
        } catch (NoSuchStatException ex) {
            Logger.getLogger(DerivedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
