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
public class StatEvent {
    public static enum Type {
        BASE_MODIFIED, DEPENDENCY_CHANGED, MOD_ADDED, MOD_REMOVED
    }
    
    public final float quantity;
    public final Type type;
    public final Object thing;
    
    public StatEvent(Type type) {
        this(type, 0, null);
    }
    
    public StatEvent(Type type, float quantity) {
        this(type, quantity, null);
    }
    
    public StatEvent(Type type, Stat mod) {
        this(type, mod.getScore(), mod);
    }
    
    public StatEvent(Type type, Object thing) {
        this(type, 0, thing);
    }
    
    public StatEvent(Type type, float quantity, Object thing) {
        this.quantity = quantity;
        this.type = type;
        this.thing = thing;
    }
}
