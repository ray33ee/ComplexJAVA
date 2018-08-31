/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.evaluator.exceptions;

/**
 * Abstract class representing an exception thrown by the evaluator during a string conversionphysicall
 * @author Will
 */
public abstract class EvaluatorParseException extends Exception 
{
    private String _token;
    
    public EvaluatorParseException(String token) { super(); _token = token; }
    
    public String getToken() { return _token; }
}
