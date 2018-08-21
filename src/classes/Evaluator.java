/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import org.apache.commons.math3.complex.Complex;

/**
 *  This Evaluator class acts as a functor for calculating f(z). This class is initialised
 *  with a formula string, which is processed into an array of tokens. From these tokens, the
 *  minimum stack size is also computed and stored as a member variable. When the calculate
 *  method is invoked, it uses the passed value of z, the token list and the max stack size
 *  to calculate f(z).
 * @author Will
 */
public class Evaluator {
    
    private Token[] _tokenlist;
    private int     _stackmax;
    
    public Evaluator()
    {
        this("z");
    }
    
    public Evaluator(String formula)
    {
        setString(formula);
    }
    
    /**
     * Convert string formula into token list, and use the token list to get the
     * max stack size. 
     * @param formula the formula to convert
     */
    public void setString(String formula)
    {
        _tokenlist = new Token[7];
        
        _tokenlist[0] = new Token(new Complex(0,0), Token.INSTRUCTION.VARIABLE);
        _tokenlist[1] = new Token(new Complex(2,0), Token.INSTRUCTION.CONSTANT);
        _tokenlist[2] = new Token(new Complex(4,0), Token.INSTRUCTION.OPERATOR);
        
        _tokenlist[3] = new Token(new Complex(0,0), Token.INSTRUCTION.VARIABLE);
        _tokenlist[4] = new Token(new Complex(2,0), Token.INSTRUCTION.CONSTANT);
        _tokenlist[5] = new Token(new Complex(2,0), Token.INSTRUCTION.OPERATOR);
        
        _tokenlist[6] = new Token(new Complex(0,0), Token.INSTRUCTION.OPERATOR);
        
        
        _stackmax = 5;
    }
    
    /**
     * Use the formula from the token list to calculate the expression f(z)
     * @param z the independent variable, z
     * @return the result of f(z)
     */
    public Complex f(Complex z)
    {
        //Initialise stack
        //Loop through token list and do stuff
        //return top of stack
        
        Complex[] stack = new Complex[_stackmax];
        int pointer = 0; //Pointer to top of stack
        
        //for (int i = 0; i < stack.length; ++i)
        //    stack[i] = new Complex(0,0);
        
        for (int i = 0; i < _tokenlist.length; ++i)
        {
            switch (_tokenlist[i].getType())
            {
                case VARIABLE:
                    stack[pointer] = z;
                    ++pointer;
                    break;
                case OPERATOR:
                    switch ((int)_tokenlist[i].getData().getReal())
                    {
                        case 0:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].add(stack[pointer]);
                            break;
                        case 1:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].subtract(stack[pointer]);
                            break;
                        case 2:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].multiply(stack[pointer]);
                            break;
                        case 3:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].divide(stack[pointer]);
                            break;
                        case 4:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].pow(stack[pointer]);
                            break;
                        case 5:
                            --pointer;
                            stack[pointer-1] = stack[pointer-1].log().divide(stack[pointer].log());
                            break;
                        case 6:
                            
                            break;
                        case 7:
                            
                            break;
                        case 8:
                            
                            break;
                        case 9:
                            
                            break;
                        case 10:
                            
                            break;
                        case 11:
                            
                            break;
                        case 12:
                            
                            break;
                        case 13:
                            
                            break;
                        case 14:
                            
                            break;
                        case 15:
                            
                            break;
                        case 16:
                            
                            break;
                        case 17:
                            
                            break;
                        case 18:
                            
                            break;
                        case 19:
                            
                            break;
                        case 20:
                            
                            break;
                        case 21:
                            
                            break;
                        case 22:
                            
                            break;
                        case 23:
                            
                            break;
                        case 24:
                            
                            break;
                        
                    }
                    break;
                case CONSTANT:
                    stack[pointer] = _tokenlist[i].getData();
                    ++pointer;
                    break;
            }
        }
        
        return stack[pointer-1];
    }
    
}
