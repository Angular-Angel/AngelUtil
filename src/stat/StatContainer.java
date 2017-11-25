/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Angle
 */
public class StatContainer {
    
    private HashMap<String, Stat> stats;
    private HashMap<String, StatContainer> references;
    private ArrayList<String> statOrder;
    private boolean active;
    
    public StatContainer() {
        this(true);
    }
    
    public StatContainer(boolean active) {
        this.stats = new HashMap<>();
        this.references = new HashMap<>();
        statOrder = new ArrayList<>();
        this.active = active;
    }
    
    public void addReference(String s, StatContainer container) {
        references.put(s, container);
    }
    
    public void clearReferences() {
        references.clear();
    }
    
    public StatContainer(StatContainer stats) {
        this(true, stats);
    }
    
    public StatContainer(boolean active, StatContainer stats) {
        this(active);
        this.stats.putAll(stats.viewStats().stats);
        for (String s : stats.statOrder) {
            statOrder.add(s);
        }
    }
    
    public Stat viewStat(String name) {
        if (stats.containsKey(name)) {return stats.get(name).copy();}
        else {throw new NoSuchStatException("Stat: " + name);}
    }
    
    public Stat getStat(String name) {
        if (name.contains("@")) {
            String[] split = name.split("@");
            if (references.containsKey(split[0])) {
                return references.get(split[0]).getStat(split[1]);
            }
            else throw new NoSuchStatException("Reference: " + split[0]);
        } else if (stats.containsKey(name)) {return stats.get(name);}
        else throw new NoSuchStatException("Stat: " + name);
    }
    
    public float getScore(String name) {
        if (name.contains("@")) {
            String[] split = name.split("@");
            if (references.containsKey(split[0])) {
                return references.get(split[0]).getScore(split[1]);
            }
            else throw new NoSuchStatException("Reference: " + split[0]);
        } else if (stats.containsKey(name)) {return stats.get(name).getScore();}
        else throw new NoSuchStatException("Stat: " + name);
    }
    
    public void addStat(String name, Stat stat) {
        if (hasStat(name)) {
            getStat(name).modifyBase(stat.getScore());
        } else {
            stats.put(name, stat);
            statOrder.add(name);
            if (active) {
                stat.setContainer(this);
                stat.refactor();
            }
        }
    }
    
    public void addStat(StatDescriptor statDescriptor) {
        Stat stat = statDescriptor.stat.copy();
        stat.statDescriptor = statDescriptor;
        addStat(statDescriptor.identifier, stat);
    }
    
    public void addStat(StatDescriptor statDescriptor, Stat stat) {
        stat.statDescriptor = statDescriptor;
        addStat(statDescriptor.identifier, stat);
    }
    
    public void modifyBaseStat(String name, float mod) {
        getStat(name).modifyBase(mod);
    }
    
    public void modifyStat(String name, float mod) {
        getStat(name).modify(mod);
    }
    
    public void removeStat(String name) {
        getStat(name).removeDependencies();
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
            addStat(s, container.viewStat(s));
            
        
    }
    
    public void modifyAllStats(StatContainer container) {
        for (String s : container.getStatList())
            if (hasStat(s)) getStat(s).modify(container.getScore(s));
            else addStat(s, container.viewStat(s));
    }
    
    public void removeAllStats(StatContainer container) {
        for (String s : container.viewStats().getStatList()) {
                if (hasStat(s)) {
                    getStat(s).modify(-container.viewStat(s).getScore());
                }
            }
    }
    
    public StatContainer viewStats() {
        StatContainer ret = new StatContainer(false);
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
        if (!active) throw new UnsupportedOperationException("Inactive StatContainer!");
        for (String s: statOrder) {
            Stat stat = stats.get(s);
            stat.setContainer(this);
            stat.refactor();
        }
    }
    
    protected void clearStats() {
        stats.clear();
        statOrder.clear();
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void init(StatContainer statContainer) {
        addReference("Source", statContainer);
        setActive(true);
    }
}
