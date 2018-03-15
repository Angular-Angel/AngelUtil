
package stat;

import com.udojava.evalex.Expression;

/**
 *
 * @author angle
 */


public class EquationStat extends Stat {

    public String equation;
    protected StatContainer container;
    
    public EquationStat(String equation) {
        this(null, equation);
    }
    
    public EquationStat(StatDescriptor statDescriptor, String string) {
        super(statDescriptor);
        equation = string;
        mods = new StatContainer();
    }
   

    @Override
    public void setContainer(StatContainer container) {
        this.container = container;
        for (int i = 0; i < equation.length(); i++)
            if (equation.charAt(i) == '[') {
                int j = i; 
                while (equation.charAt(i) != ']') {
                    i++;
                }
                String statName = equation.substring(j+1, i);
                container.getStat(statName).addObserver(this);
            }
    }

    @Override
    protected float refactorBase() {
        return parse(equation);  
    }
    
    public final float parse(String string){
        
        String ret = string;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '[') {
                int j = i; 
                while (equation.charAt(i) != ']') {
                    i++;
                }
                String statName = string.substring(j+1, i);
                CharSequence target = '[' + statName + ']';
                CharSequence replace = "" + container.getScore(statName);
                ret = ret.replace(target, replace);
            }
        }
        
        Expression e = new Expression(ret);
        return e.eval().floatValue();
    }

    @Override
    public Stat copy() {
        EquationStat ret = new EquationStat(getStatDescriptor(), equation);
        ret.mods.addAllStats(mods.viewStats());
        return ret;
    }


    @Override
    public void set(Object obj) {
        if (!equation.equals(obj)) {
            equation = (String) obj;
            refactor();
        }
    }

    @Override
    public void modifyBase(float change) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
