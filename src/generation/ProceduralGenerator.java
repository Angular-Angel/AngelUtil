
package generation;

/**
 *
 * @author angle
 */


public interface ProceduralGenerator<T> {
    
    public T generate();
    
    public T generate(Object o);
    
}
