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
public class MissingRightBracketException extends EvaluatorParseException 
{    
    public MissingRightBracketException() { super(""); }
    
    @Override
    public String getMessage() { return "Missing right bracket in formula"; }
}
