/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import complex.evaluator.Evaluator;

/**
 * Landscape class encapsulates all members required to draw a complex landscape. This
 * includes the Evaluator class, with minimum and maximum domains.
 * @author Will
 */
public class Landscape 
{
    /** The evaluator class containing the token list and other information */
    private final Evaluator _evaltor;
    
    /** The minimum value in the domain, that is the bottom left of the landscape */
    private final Complex _min;
    
    /** The maximum value in the domain, that is the top right of the landscape */
    private final Complex _max;
    
    /**
     * Constructs a new landscape, with supplied values
     * @param eval the evaluator (equation) for this class
     * @param min the minimum domain
     * @param max the maximum domain
     */
    public Landscape(Evaluator eval, Complex min, Complex max)
    {
        _evaltor = eval;
        _min = min;
        _max = max;
    }
    
    public Evaluator getEvaluator() { return _evaltor; }
    
    public Complex getMinDomain() { return _min; }
    
    public Complex getMaxDomain() { return _max; }
    
    @Override public String toString() { return "f(z) = " + _evaltor.getEquation() + "    " + _min.toString() + "    " + _max.toString() + ""; }
}
