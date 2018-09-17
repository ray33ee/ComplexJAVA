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
public class FloatingFormatException extends EvaluatorParseException 
{
    public FloatingFormatException(String token) { super(token); }
    
    @Override
    public String getMessage() { return "Invalid number format - " + super.getToken(); }
}
