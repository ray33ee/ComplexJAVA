/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex.evaluator.exceptions;

/**
 * Exception thrown when an operator is not used correctly. The exact cause of this exception
 * is when there are too many items left on the stack after the initial formula validation.
 * @author Will
 */
public class InvalidOperatorUseException extends EvaluatorParseException 
{    
    public InvalidOperatorUseException() { super(""); }
    
    @Override
    public String getMessage() { return "Invalid use of operator detected in formula."; }
}