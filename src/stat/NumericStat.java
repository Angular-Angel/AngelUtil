/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

/**
 *
 * @author Greg
 */
public class NumericStat extends Stat {
    
    private float baseScore;

    public NumericStat(float score) {
        this(null, score);
    }
    
    public NumericStat(StatDescriptor statDescriptor, float score) {
        super(statDescriptor);
        this.baseScore = score;
        mods = new StatContainer();
    }

    @Override
    public void setContainer(StatContainer i) {}
    
    @Override
    public void modifyBase(float change) {
        baseScore += change;
        score += change;
        setChanged();
        notifyObservers(new StatEvent(StatEvent.Type.BASE_MODIFIED, change));
    }

    @Override
    public Stat copy() {
        NumericStat ret = new NumericStat(getStatDescriptor(), baseScore);
        ret.mods.addAllStats(mods.viewStats());
        return ret;
    }

    @Override
    protected float refactorBase() {
        return baseScore;
    }

    @Override
    public void set(Object obj) {
        float change = (float) obj - baseScore;
        baseScore = (float) obj;
        setChanged();
        notifyObservers(new StatEvent(StatEvent.Type.BASE_MODIFIED, change));
    }
    
}
