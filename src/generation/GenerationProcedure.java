
package generation;

public interface GenerationProcedure<T> {
    
    public T generate();
    
    public T generate(Object o);
    
    public T modify(T t);
    
    public boolean isApplicable(T t);
    
}
