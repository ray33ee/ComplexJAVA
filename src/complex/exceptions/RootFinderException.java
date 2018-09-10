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
public abstract class RootFinderException extends Exception
{
    Complex _val;
    int _iteration;
    
    public RootFinderException(Complex val, int iteration) { _val = val; _iteration = iteration; }
    
    Complex getValue() { return _val; }
    int getIteration() { return _iteration; }
}
