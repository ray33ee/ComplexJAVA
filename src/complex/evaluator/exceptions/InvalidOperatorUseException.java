/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.evaluator.exceptions;

/**
 *
 * @author Will
 */
public class InvalidOperatorUseException extends EvaluatorParseException 
{    
    public InvalidOperatorUseException(String token) { super(token); }
    
    @Override
    public String getMessage() { return "Invalid use of operator '" + super.getToken() + "' detected in formula."; }
}