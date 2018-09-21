/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.exceptions;

import complex.Complex;

/**
 * Exception thrown when the root finder fails (failure detected by NaN or infinite result)
 * @author Will
 */
public class DivergentSeedException extends RootFinderException
{
    public DivergentSeedException(Complex val, int iteration) { super(val, iteration); }
    
    @Override public String getMessage() { return "Root-finding failed. Iteration " + super.getIteration() + " has the value " + super.getValue() + ". Most likely cause is divergent function or bad seed."; }
}
