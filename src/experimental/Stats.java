package experimental;

import java.util.HashMap;
import java.util.Map;

public final class Stats {
    //private final DAG<Stat> dependencyGraph = new DAG<>();
    private final Map<String, Stat> names = new HashMap<>();
    
    public Stats() {
    }
    
    public void addStat(String name, Stat newStat) {
        names.put(name, newStat);
        //dependencyGraph.add(newStat);
        //for (String dependency : newStat.getDependencies()) {
            
        //}
    }
    
    public Stat getStat(String name) {
        return names.get(name);
    }
}
