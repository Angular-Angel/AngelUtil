
package util;

/**
 *
 * @author angle
 */


public enum Direction {
    
    NORTH(0, -1, 0), EAST(1, 0, 0), SOUTH(0, 1, 0), WEST(-1, 0, 0), UP(0, 0, -1), DOWN(0, 0, 1);
    
    public final int xmod;
    public final int ymod;
    public final int zmod;
    
    
    Direction(int x, int y, int z) {
        xmod = x;
        ymod = y;
        zmod = z;
    }
    
}
