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
    
    private Stat stat;
    public final String name;
    public float base;
    public float increase;
    
    public StatDescriptor(String name, Stat stat, float base, float increase) {
        this.name = name;
        this.stat = stat;
        this.base = base;
        this.increase = increase;
    }
    
}
