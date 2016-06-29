package stat;

/**
 *
 * @author greg
 */
public class BinaryStat extends Stat{

    @Override
    public float getScore() {
        return 1;
    }

    @Override
    public void setContainer(StatContainer i) {}

    @Override
    public void refactor() throws NoSuchStatException {}

    @Override
    public void modify(float change) {}

    @Override
    public void modifyBase(float change) {}

    @Override
    public Stat copy() {
        return new BinaryStat();
    }

    @Override
    public void addDependent(Stat s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeDependent(Stat s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeDependencies() {}

    @Override
    public void set(float score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clearDependents() {}

}
