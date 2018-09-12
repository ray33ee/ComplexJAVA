/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.exceptions;

import complex.Complex;

/**
 * All exceptions thrown by root finder algorithm inherit this class, which contains the value of the seed and the index of the iteration at the time the exception is thrown
 * @author Will
 */
public abstract class RootFinderException extends Exception
{
    /** Value of the seed when the exception is thrown */
    Complex _val;
    
    /** The number of iterations completed at the time the exception is thrown */
    int _iteration;
    
    public RootFinderException(Complex val, int iteration) { _val = val; _iteration = iteration; }
    
    Complex getValue() { return _val; }
    int getIteration() { return _iteration; }
}
