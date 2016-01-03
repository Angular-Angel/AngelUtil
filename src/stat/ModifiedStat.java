package stat;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author greg
 */
public class ModifiedStat implements Stat {
    private StatContainer i;
    private String s1;
    private float score, modifier, addition;
    private char type;
    private HashSet<Stat> dependents;
    
    public ModifiedStat(StatContainer i, String stat, float mod) {
        this(i, stat, mod, '*');
    }
    
    public ModifiedStat(StatContainer i, String stat, float mod, char type) {
        this.i = i;
        s1 = stat;
        modifier = mod;
        this.type = type;
        dependents = new HashSet<>();
        addition = 0;
    }
    
    public ModifiedStat(String s1, float mod) {
        this(s1, mod, '*');
    }
    
    public ModifiedStat(String s1, float mod, char type) {
        this.s1 = s1;
        modifier = mod;
        this.type = type;
        dependents = new HashSet<>();
    }
    
    @Override
    public float getScore() {
        return score;
    }

    @Override
    public void setContainer(StatContainer i) {
        if (this.i != null) removeDependencies();
        this.i = i;
        try {
            i.getStat(s1).addDependent(this);
        } catch (NoSuchStatException ex) {
            Logger.getLogger(ModifiedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refactor() {
        try {
            switch(type) {
                case '*': score = i.getScore(s1) * modifier;
                    break;
                case '/': score = i.getScore(s1) / modifier;
                    break;
                case '+': score = i.getScore(s1) + modifier;
                    break;
                case '-': score = i.getScore(s1) - modifier;
                    break;
            }
            modify(addition);
            for (Stat s : dependents) s.refactor();
        } catch(NoSuchStatException ex) {
            Logger.getLogger(ModifiedStat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public Stat copy() {
        ModifiedStat ret = new ModifiedStat(s1, score, type);
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
        } catch (NoSuchStatException ex) {
            Logger.getLogger(ModifiedStat.class.getName()).log(Level.SEVERE, null, ex);
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
