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
public class StatDescriptor {
    
    public final Stat stat;
    public final Stat.Type type;
    public final String identifier;
    public final String name;
    public float base;
    public float increase;
    
    public StatDescriptor(String identifier, String name, Stat stat, Stat.Type type, float base, float increase) {
        this.identifier = identifier;
        this.name = name;
        this.stat = stat;
        this.base = base;
        this.increase = increase;
        this.type = type;
    }
    
}
