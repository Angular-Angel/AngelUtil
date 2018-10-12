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
public final class StatContainer {
    
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
    
    public StatContainer(StatContainer stats) {
        this(true, stats);
    }
    
    public StatContainer(Stat... stats) {
    	this(true);
    	for (Stat stat : stats)
    		addStat(stat);
    }
    
    public StatContainer(boolean active, StatContainer stats) {
        this(active);
        this.stats.putAll(stats.viewStats().stats);
        for (String s : stats.statOrder) {
            statOrder.add(s);
        }
        if (active) refactor();
    }
    
    public void addReference(String s, StatContainer container) {
        references.put(s, container);
    }
    
    public void clearReferences() {
        references.clear();
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
    
    public Stat getStat(StatDescriptor statDescriptor) {
        return getStat(statDescriptor.identifier);
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
            throw new RuntimeException("Stat already present: " + name);
        } else {
            stats.put(name, stat);
            statOrder.add(name);
            if (active) {
                stat.setContainer(this);
                stat.refactor();
            }
        }
    }
    
    public void addStat(StatDescriptor statDescriptor, Stat stat) {
        addStat(statDescriptor.identifier, stat);
    }
    
    public void addStat(StatDescriptor statDescriptor) {
        Stat stat = statDescriptor.stat.copy();
        addStat(statDescriptor.identifier, stat);
    }
    
    public void addStat(String name, float f) {
        try {
            Stat stat = getStat(name);
            if (stat instanceof NumericStat) {
                ((NumericStat) stat).modifyBase(f);
                return;
            }
        } catch (NoSuchStatException e) {}
        addStat(name, new NumericStat(f));
    }
    
    public void addStat(StatDescriptor statDescriptor, float f) {
        try {
            Stat stat = getStat(statDescriptor.identifier);
            if (stat instanceof NumericStat) {
                ((NumericStat) stat).modifyBase(f);
                return;
            }
        } catch (NoSuchStatException e) {}
        addStat(statDescriptor.identifier, new NumericStat(statDescriptor, f));
    }
    
    public void addStat(Stat stat) {
        addStat(stat.getStatDescriptor().identifier, stat);
    }
    
    public void removeStat(String name) {
        stats.remove(name);
        statOrder.remove(name);
    }
    
    public void addAllStats(HashMap<String, Stat> newStats) {
        for (String s : newStats.keySet()) {
            addStat(s, newStats.get(s));
        }
    }
    
    public void addAllStats(StatContainer statContainer) {
        for (String s : statContainer.getStatList())
            addStat(s, statContainer.viewStat(s));
    }
    
    public void increaseAllStats(StatContainer stats) {
        for (String s : stats.statOrder) {
            getStat(s).modify("", stats.getScore(s));
        }
    }
    
    public void increaseAllStats(String name, StatContainer stats) {
        for (String s : stats.statOrder) {
            getStat(s).modify(name, stats.getStat(s));
        }
    }
    
    public void mergeStats(StatContainer stats) {
        for (String s : stats.statOrder) {
            if (hasStat(s))
                getStat(s).modify("", stats.getScore(s));
            else addStat(s, stats.viewStat(s));
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
    
    public boolean hasStat(StatDescriptor s) {
        return hasStat(s.identifier);
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
    
    public void initValues(StatContainer statContainer) {
        addReference("Source", statContainer);
        setActive(true);
    }
}
