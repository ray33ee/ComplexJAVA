/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

//import org.apache.commons.math3.complex.Complex;

import complex.evaluator.Evaluator;


/**
 * Landscape class encapsulates all members required to draw a complex landscape. This
 * includes the Evaluator class, with minimum and maximum domains.
 * @author Will
 */
public class Landscape 
{
    private Evaluator _evaltor;
    private Complex _min;
    private Complex _max;
    
    public Landscape(Evaluator eval, Complex min, Complex max)
    {
        _evaltor = eval;
        _min = min;
        _max = max;
    }
    
    public Evaluator getEvaluator() { return _evaltor; }
    
    public Complex getMinDomain() { return _min; }
    
    public Complex getMaxDomain() { return _max; }
}
