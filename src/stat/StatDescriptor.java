/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

/**
 *
 * @author angle
 */
public final class StatDescriptor {
    
    public static enum StatType {
        NUMBER, PERCENTAGE, MULTIPLIER;
    }
    
    public Stat stat;
    public final StatType type;
    public final String identifier;
    public final String name;
    public final float base;
    public final float increase;
    
    public StatDescriptor(String identifier, String name, Stat stat, StatType type, float base, float increase) {
        this.identifier = identifier;
        this.name = name;
        this.stat = stat;
        this.base = base;
        this.increase = increase;
        this.type = type;
    }
    
}
