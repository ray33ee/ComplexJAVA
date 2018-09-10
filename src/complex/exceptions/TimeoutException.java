/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.exceptions;

import complex.Complex;

/**
 *
 * @author Will
 */
public class TimeoutException extends RootFinderException 
{
    public TimeoutException(Complex val, int iteration) { super(val, iteration); }
    
    @Override public String getMessage() { return "Root finder has timed out at " + getIteration() + ", with value " + getValue().toString() + ". Most likely cause is divergent function or bad seed."; }
}