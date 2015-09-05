
package util;

import org.jbox2d.common.Vec2;

/**
 *
 * @author angle
 */


public class VecMath {
    
    public static float squareDist(Vec2 v1, Vec2 v2)
    {
        if (v1 == null || v2 == null) throw new IllegalArgumentException();
        
        final float dx = v1.x - v2.x,
                    dy = v1.y - v2.y;
        
        return dx*dx + dy*dy;
    }

    public static float dist(Vec2 v1, Vec2 v2)
    {
        return (float) Math.sqrt(squareDist(v1, v2));
    }
    
    public static Vec2 direction(Vec2 v1, Vec2 v2) {
        Vec2 ret = new Vec2();
        
        ret.x = v2.x - v1.x;
        ret.y = v2.y - v1.y;
        
        return ret;
    }
}
