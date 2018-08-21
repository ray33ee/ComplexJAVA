/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import org.apache.commons.math3.complex.Complex;

/**
 *  Token class, contains _data and _type, which explains how _data should be treated.
 *  The formula is converted from a sting to an array of tokens. 
 * @author Will
 */
public class Token {
    
    /**
     * An enum containing the 'type' of data in the token. EMPTY is the value 
     * assigned with an empty constructor. VARIABLE tells the evaluator to 
     * use the independent variable (_data will be 0). OPERATOR tells the 
     * evaluator that the data is an operator, (_data will be Complex(n, 0) where
     * n is the index of the operator. And CONSTANT tells the evaluator that the 
     * _data is a constant (_data is the constant.)
     */
    public enum INSTRUCTION { EMPTY, VARIABLE, OPERATOR, CONSTANT }
    
    private Complex     _data;
    private INSTRUCTION _type;
    
    /**
     * Constructs empty token, with data = 0 and type = EMPTY.
     */
    public Token()
    {
        this(new Complex(0,0), INSTRUCTION.EMPTY);
    }
    
    /**
     * Construct token, composed of a data element, and a type element. Type element
     * tells the evaluator to treat the data as a variable, operator or a constant.
     * @param data  
     * @param type 
     */
    public Token(Complex data, INSTRUCTION type)
    {
        _data = data;
        _type = type;
    }
    
    /**
     * Get the _data element. 
     * @return the _data element
     */
    public Complex getData()
    {
        return _data;
    }
    
    /**
     * Get the _type element
     * @return the _type element
     */
    public INSTRUCTION getType()
    {
        return _type;
    }
    
    /**
     * Set the data in the token
     * @param data the data to set
     */
    public void setData(Complex data)
    {
        _data = data;
    }
    
    /**
     * Set the type in the token
     * @param type the type to set
     */
    public void setInstruction(INSTRUCTION type)
    {
        _type = type;
    }
}
