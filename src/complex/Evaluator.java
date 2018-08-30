/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import java.util.Stack;
import java.util.Vector;
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
    
    /**
     * Machine EPSILON value
     */
    private static final double EPSILON;
    
    /**
     * Square root of machine EPSILON value
     */
    private static final double SQRTEPS;
    
    static { double ep = 1; while (1.0 + 0.5*ep != 1.0) ep = 0.5 * ep; EPSILON = ep; SQRTEPS = Math.sqrt(EPSILON); }
    
    /**
     * Default timeout value supplied to the newton-raphson method
     */
    public static final int TIMEOUT = 1000;
    
    /**
     * List of all functions (excludes operators)
     */
    private static final String[] functions = { "log", "neg", "conj", "sqrt", "ln", "exp", "sinh", "cosh", "tanh", "sin", "cos", "tan", "asinh", "acosh", "atanh", "asin", "acos", "atan", "inv", "mod", "arg" };
    
    /**
     * The formula represented as a list of tokens
     */
    private Token[] _tokenlist;
    
    /**
     * The maximum size the stack can grow to when evaluating tokenlist
     */
    private int _stackmax;
    
    /**
     * Constructs an instance of Evaulator, with the formula "z".
     */
    public Evaluator() { setString("(z + i)*(z-1)"); }
    
    /**
     * Constructs an instance of Evaulator with a specific formula
     * @param formula the equation to convert
     */
    public Evaluator(String formula) { setString(formula); }
    
    /**
     * Obtains a copy (via clone) of the list of tokens
     * @return a copy of the array of tokens stored
     */
    public Token[] getTokens() { return _tokenlist.clone(); }
    
    /**
     * Get the maximum size of the stack required for a single evaluation of the formula.
     * @return the maximum size that an evaluating stack can grow to
     */
    public int getStackMax() { return _stackmax; }
    
    /**
     * Convert string formula into token list, and use the token list to get the
     * max stack size. 
     * @param formula the formula to convert
     */
    public void setString(String formula) { _tokenlist = processString(formula); calculateStackmax(); }
    
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
                            stack[pointer-1] = stack[pointer-1].negate();
                            break;
                        case 7:
                            stack[pointer-1] = stack[pointer-1].conjugate();
                            break;
                        case 8:
                            stack[pointer-1] = stack[pointer-1].sqrt();
                            break;
                        case 9:
                            stack[pointer-1] = stack[pointer-1].log();
                            break;
                        case 10:
                            stack[pointer-1] = stack[pointer-1].exp();
                            break;
                        case 11:
                            stack[pointer-1] = stack[pointer-1].sinh();
                            break;
                        case 12:
                            stack[pointer-1] = stack[pointer-1].cosh();
                            break;
                        case 13:
                            stack[pointer-1] = stack[pointer-1].tanh();
                            break;
                        case 14:
                            stack[pointer-1] = stack[pointer-1].sin();
                            break;
                        case 15:
                            stack[pointer-1] = stack[pointer-1].cos();
                            break;
                        case 16:
                            stack[pointer-1] = stack[pointer-1].tan();
                            break;
                        case 17:
                            
                            break;
                        case 18:
                            
                            break;
                        case 19:
                            
                            break;
                        case 20:
                            stack[pointer-1] = stack[pointer-1].asin();
                            break;
                        case 21:
                            stack[pointer-1] = stack[pointer-1].acos();
                            break;
                        case 22:
                            stack[pointer-1] = stack[pointer-1].atan();
                            break;
                        case 23:
                            stack[pointer-1] = new Complex(stack[pointer-1].abs());
                            break;
                        case 24:
                            stack[pointer-1] = new Complex(stack[pointer-1].getArgument());
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
    
    
    public Complex newton_raphson(Complex zn, int timeout)
    {
        //Timeout for non-convergent cases
	if (timeout-- == 0)
		return zn;

	Complex next = zn.subtract(f(zn).divide(fast_gradient(zn)));

	if ((next.subtract(zn)).abs() < EPSILON)
		return next;

	return newton_raphson(next, timeout);
    }
    
    private Complex fast_gradient(Complex z) { Complex h = z.multiply(SQRTEPS); return (f(z.add(h)).subtract(f(z))).divide(h); }
    
    private static boolean isNum(char ch) { return ch >= '0' && ch <= '9'; }
    
    private static boolean isLetter(char ch) { return ch >= 'a' && ch <= 'z'; }
    
    /**
     * Determines whether the string can be converted to a valid number
     * @param str the string to test
     * @return true if str can be written as a number, false otherwise
     */
    private static boolean isNum(String str)
    {
        try 
        {
            Double.parseDouble(str);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Determines whether the string is an operator
     * @param str the string to test
     * @return true if str is one of the following - +, -, *, /, ^ or neg (unary negate)
     */
    private static boolean isOp(String str) { return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^") || str.equals("neg" ); }
    
    /**
     * Determines whether the string is a function
     * @param str the string to test
     * @return true if the str is in the 'functions' list
     */
    private static boolean isFunction(String str)
    {
        for (int i = 0; i < functions.length; ++i)
            if (str.equals(functions[i]))
                return true;
        return false;
    }
    
    /**
     * Get the precedence of the operator, from 1 to 3.
     * @param token
     * @return the precedence from 1 (lowest) to 3 (highest)
     */
    private static int getPrecedence(String token)
    {
        switch (token) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "neg":
                return 3;
            default:
                return 4;
        }
    }
    
    /**
     * Get the index of the function/operator ready to store directly in Token.data. This
     * is represented as a number from 0 to 24.
     * @param str string operator
     * @return the index from 0 to 24.
     */
    private static int getIndex(String str)
    {
        if (isOp(str) && !str.equals("neg"))
        {
            switch (str) {
                case "+":
                    return 0;
                case "-":
                    return 1;
                case "*":
                    return 2;
                case "/":
                    return 3;
                case "^":
                    return 4;
                default:
                    break;
            }
        }
        else
        {
            for (int i = 0; i < functions.length; ++i)
                if (str.equals(functions[i]))
                    return i + 5;
        }
        return -1;
    }
    
    /**
     * Determines whether the current token represents the unary negate
     * @param output
     * @param opStack
     * @param token
     * @return 
     */
    private static boolean isUnaryNegative(Vector<Token> output, Stack<String> opStack, String prevToken) { return (output.isEmpty() && opStack.isEmpty()) || prevToken.equals("(") || prevToken.equals("neg") || isOp(prevToken); }
    
    /**
     * Removes all whitespace from the string
     * @param str string to remove
     */
    private static String removeWhitespace(String str)
    {
        String ans = "";
        
        for (int i = 0; i < str.length(); ++i)
            if (str.charAt(i) != ' ' && str.charAt(i) != '\n' && str.charAt(i) != '\t')
                ans += str.charAt(i);
        
        return ans;
    }
    
    /**
     * Takes a string token, applies shunting yard algorithm to convert 
     * infix to RPN, then converts to Token type.
     * @param output the final output list
     * @param opStack the stack of operators
     * @param token the token to push
     */
    private static void sendToken(Vector<Token> output, Stack<String> opStack, String token, String prev)
    {
        if (!token.isEmpty())
        {
            if (token.equals("z"))
                output.add(new Token(new Complex(0), Token.INSTRUCTION.VARIABLE));
            else if (isNum(token))
                output.add(new Token(new Complex(Double.parseDouble(token)), Token.INSTRUCTION.CONSTANT));
            else if (token.equals("i"))
                output.add(new Token(new Complex(0, 1), Token.INSTRUCTION.CONSTANT));
            else if (token.equals("pi"))
                output.add(new Token(new Complex(Math.PI), Token.INSTRUCTION.CONSTANT));
            else if (token.equals("e"))
                output.add(new Token(new Complex(Math.E), Token.INSTRUCTION.CONSTANT));
            else if (isFunction(token))
                opStack.push(token);
            else if (token.equals(","))
            {
                while (opStack.size() > 0)
                {
                    if (opStack.peek().equals("("))
                        break;
                    output.add(new Token(new Complex(getIndex(token)), Token.INSTRUCTION.OPERATOR));
                }
            }
            else if (isOp(token))
            {
                if (token.equals("-") && isUnaryNegative(output, opStack, prev))
                    opStack.add("neg");
                else
                {
                    while (!opStack.isEmpty())
                    {
                        if ((isOp(opStack.peek()) && getPrecedence(opStack.peek()) > getPrecedence(token)) || (isOp(opStack.peek()) && getPrecedence(opStack.peek()) == getPrecedence(token) && token.equals("^") && !opStack.peek().equals("(")))
                            output.add(new Token(new Complex(getIndex(opStack.pop())), Token.INSTRUCTION.OPERATOR));
                        else
                            break;
                    }
                    opStack.add(token);
                }
            }
            else if (token.equals("(") || token.equals("{") || token.equals("[")) //Left bracket
            {
                opStack.add("(");
            }
            else if (token.equals(")")|| token.equals("}") || token.equals("]")) //Right bracket
            {
                while (!opStack.isEmpty())
                {
                    if (!opStack.peek().equals("("))
                        output.add(new Token(new Complex(getIndex(opStack.pop())), Token.INSTRUCTION.OPERATOR));
                    else
                        break;
                }

                if (opStack.isEmpty()) //If there is no matching parenthesis, error
                    System.err.println("MISMATCHED PARENTHESIS");

                opStack.pop();

                if (opStack.isEmpty()) //There are no more items on the stack, c'est fini
                    return;
                if (isFunction(opStack.peek()) || isOp(opStack.peek()))
                        output.add(new Token(new Complex(getIndex(opStack.pop())), Token.INSTRUCTION.OPERATOR));
            }
            else
                System.err.println("UNRECOSNISED TOKEN: '" + token + "'");
        }
    }
    
    /**
     * Converts a string to a list of tokens. Takes a string, separates each token and processes them individually. 
     * @param str the string to process
     * @return the formula as a token list
     */
    private static Token[] processString(String str)
    {
        Vector<Token> output = new Vector<Token>();
        Stack<String> opStack = new Stack<String>();
        Token[] ans;
        
        String buff = "";
        String prev = "";
        
        str.toLowerCase();
        
        str = removeWhitespace(str);
        
        for (int i = 0; i < str.length(); ++i)
        {
            char ch = str.charAt(i);
            if (isLetter(ch) || isNum(ch) || ch == '.' || (ch == 'e' && isNum(str.charAt(i-1))) || (ch == '+' || ch == '-') && str.charAt(i-1) == 'e')
            {
                buff += ch;
            }
            else
            {
                sendToken(output, opStack, buff, prev);
                if (!buff.isEmpty())
                    prev = buff;
                sendToken(output, opStack, new String(new char[] {ch} ), prev);
                prev = new String(new char[] {ch} );
                buff = "";
            }
        }
        sendToken(output, opStack, buff, prev);

        while (!opStack.isEmpty())
        {
            String item = opStack.pop();
            if (!item.equals("("))
                    output.add(new Token(new Complex(getIndex(item)), Token.INSTRUCTION.OPERATOR));
            else 
                System.err.println("Right hand brace missing");
        }
        
        if (!verify(output))
            System.err.println("Too many/few values left on stack");
        
        for (int i = 0; i < output.size(); ++i)
            System.out.println("Token " + i + ": " + output.elementAt(i).toString());
        
        ans = new Token[output.size()];
        ans = (Token[])output.toArray(ans);
        return ans;
    }
    
    /**
     * Performs a quick run of the equation, and returns true if a single element
     * remains on the virtual stack, else returns false
     * @param output the tokenlist to test
     * @return true if the tokenlist is correct, otherwise false
     */
    private static boolean verify(Vector<Token> output)
    {
        int pretend_stack_size = 0;
        for (int i = 0; i < output.size(); ++i)
        {
            if (output.elementAt(i).getType() == Token.INSTRUCTION.CONSTANT || output.elementAt(i).getType() == Token.INSTRUCTION.VARIABLE) //If its z or constant we push (so add 1)
                ++pretend_stack_size;
            else if (output.elementAt(i).getType() == Token.INSTRUCTION.OPERATOR && output.elementAt(i).getData().getReal() < 6) //If its binary operator we pop 2 and push 1 (so decrement 1)
                --pretend_stack_size;
        }
        return (pretend_stack_size == 1);
    }
    
    /**
     * Set the _stackmax member variable by iterating through the tokenlist performing
     * a mock run. MUST be called when _tokenlist changes.
     */
    private void calculateStackmax()
    {
        _stackmax = 0;
        int ptr = 0;
        for (int i = 0; i < _tokenlist.length; ++i)
        {
            if (_tokenlist[i].getType() == Token.INSTRUCTION.CONSTANT || _tokenlist[i].getType() == Token.INSTRUCTION.VARIABLE)
                ++ptr;
            else if (_tokenlist[i].getType() == Token.INSTRUCTION.OPERATOR && _tokenlist[i].getData().getReal() < 6)
                --ptr;
            _stackmax = (ptr > _stackmax) ? ptr : _stackmax;
        }
        
    }
}
