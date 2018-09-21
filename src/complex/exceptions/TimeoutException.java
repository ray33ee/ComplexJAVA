/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.exceptions;

import complex.Complex;

/**
 * Exception thrown when the root finder algorithm times out. The timeout value is defined in the Evaluator class.
 * @author Will
 */
public class TimeoutException extends RootFinderException 
{
    public TimeoutException(Complex val, int iteration) { super(val, iteration); }
    
    @Override public String getMessage() { return "Root finder has timed out at " + getIteration() + ", with value " + getValue() + ". Most likely cause is divergent function or bad seed."; }
}
