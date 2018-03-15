package experimental;

import java.util.List;

public interface Stat {
    
    public List<String> getDependencies();
    
    public float update();
    
}
