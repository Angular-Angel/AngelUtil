/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Greg
 */
public class StatContainer {
    
    private HashMap<String, Stat> stats;
    private ArrayList<String> statOrder;
    
    public StatContainer() {
        this.stats = new HashMap<>();
        statOrder = new ArrayList<>();
    }
    
    public StatContainer(StatContainer stats) {
        this.stats = new HashMap<>();
        statOrder = new ArrayList<>();
        this.stats.putAll(stats.viewStats().stats);
        for (String s : stats.statOrder) {
            statOrder.add(s);
        }
    }
    
    public Stat viewStat(String name) throws NoSuchStatException{
        if (stats.containsKey(name)) {return stats.get(name).copy();}
        else {throw new NoSuchStatException("Stat: " + name);}
    }
    
    public Stat getStat(String name) throws NoSuchStatException{
        if (stats.containsKey(name)) {return stats.get(name);}
        else {throw new NoSuchStatException("Stat: " + name);}
    }
    
    public float getScore(String name) throws NoSuchStatException{
        if (stats.containsKey(name)) {return stats.get(name).getScore();}
        else {throw new NoSuchStatException("Stat: " + name);}
    }
    
    public void addStat(String name, Stat stat) {
        if (hasStat(name)) {
            try {
                getStat(name).modifyBase(stat.getScore());
            } catch (NoSuchStatException ex) {
                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            stats.put(name, stat);
            statOrder.add(name);
            stat.setContainer(this);
            try {
                stat.refactor();
            } catch (NoSuchStatException ex) {
//                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void removeStat(String name) {
        try {
            getStat(name).removeDependencies();
        } catch (NoSuchStatException ex) {
            Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        stats.remove(name);
        statOrder.remove(name);
    }
    
    public void addAllStats(HashMap<String, Stat> newStats) {
        for (String s : newStats.keySet()) {
            addStat(s, newStats.get(s));
        }
    }
    
    public void addAllStats(StatContainer container) {
        for (String s : container.getStatList())
            try {
                addStat(s, container.viewStat(s));
            } catch (NoSuchStatException ex) {
                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    public void modifyAllStats(StatContainer container) {
        for (String s : container.getStatList())
            try {
                if (hasStat(s)) getStat(s).modify(container.getScore(s));
                else addStat(s, container.viewStat(s));
            } catch (NoSuchStatException ex) {
                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void removeAllStats(StatContainer container) {
        for (String s : container.viewStats().getStatList()) {
                try {
                    if (hasStat(s)) {
                        getStat(s).modify(-container.viewStat(s).getScore());
                    }

                } catch (NoSuchStatException ex) { 
                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            } 
            }
    }
    
    public StatContainer viewStats() {
        StatContainer ret = new StatContainer();
        for (String s : statOrder) {
            ret.addStat(s, stats.get(s).copy());
        }
        return ret;
    }
    
    public ArrayList<String> getStatList() {
        return (ArrayList<String>) statOrder.clone();
    }
    
    protected HashMap<String, Stat> getStats() {
        return stats;
    }
    
    public boolean hasStat(String s) {
        return stats.containsKey(s);
    }
    
    public void refactor() {
        for (String s: statOrder) {
            Stat stat = stats.get(s);
            try {
                stat.setContainer(this);
                stat.refactor();
            } catch (NoSuchStatException ex) {
                Logger.getLogger(StatContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void clearStats() {
        stats.clear();
        statOrder.clear();
    }
}
