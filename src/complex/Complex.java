/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import java.awt.Color;
import java.text.DecimalFormat;

/**
 * Complex class inherited from org.apache.commons.math3.complex.Complex. Adds 
 * refined toString() override and color() method.
 * @author Will
 */
public class Complex extends org.apache.commons.math3.complex.Complex
{
    public static final Complex ONE;
    public static final Complex I;
    
    static { ONE = new Complex(1,0); I = new Complex(0,1); }
    
    public Complex() { super(0); }
    
    public Complex(double real) { super(real); }
    
    public Complex(double real, double imag) { super(real, imag); }
    
    public Complex(org.apache.commons.math3.complex.Complex z) { super(z.getReal(), z.getImaginary()); }
    
    public Complex add(Complex z) { return new Complex(super.add(z)); }
    
    public Complex subtract(Complex z) { return new Complex(super.subtract(z)); }
    
    public Complex multiply(Complex z) { return new Complex(super.multiply(z)); }
    
    @Override public Complex multiply(double r) { return new Complex(super.multiply(r)); }
    
    public Complex divide(Complex z) { return new Complex(super.divide(z)); }
    
    public Complex pow(Complex z) { return new Complex(super.pow(z)); }
    
    public Complex log(Complex z) { return new Complex(super.log().divide(z.log())); }
    
    public Complex ln() { return new Complex(super.log()); }
    
    @Override public Complex negate() { return new Complex(super.negate()); }
    
    @Override public Complex conjugate() { return new Complex(super.conjugate()); }
    
    @Override public Complex exp() { return new Complex(super.exp()); }
    
    @Override public Complex sqrt() { return new Complex(super.sqrt()); }
    
    @Override public Complex asin() { return new Complex(super.asin()); }
    
    @Override public Complex acos() { return new Complex(super.acos()); }
    
    @Override public Complex atan() { return new Complex(super.atan()); }
    
    @Override public Complex sin() { return new Complex(super.sin()); }
    
    @Override public Complex cos() { return new Complex(super.cos()); }
    
    @Override public Complex tan() { return new Complex(super.tan()); }
    
    public Complex asinh() { return add(sqrd().add(ONE).sqrt()).ln(); }
    
    public Complex acosh() { return add(sqrd().subtract(ONE).sqrt()).ln(); }
    
    public Complex atanh() { return add(ONE).divide(ONE.subtract(this)).ln().multiply(0.5); }
    
    @Override public Complex sinh() { return new Complex(super.sinh()); }
    
    @Override public Complex cosh() { return new Complex(super.cosh()); }
    
    @Override public Complex tanh() { return new Complex(super.tanh()); }
    
    @Override public Complex reciprocal() { return new Complex(super.reciprocal()); }
    
    /**
     * Gets the complex number this multiplied by itself. The square of a+bi is calculated as
     * (a+bi)^2 = (a^2 - b^2) + (2ab)i.
     * @return this * this
     */
    public Complex sqrd() 
    {  
        return new Complex(getReal() * getReal() - getImaginary() * getImaginary(), 2*getReal() * getImaginary());
    }
    
    
    /**
    * Gets the colour of this complex number, as per the domain colouring method
     * @return  the colour of this complex number
     */
    public Color color()
    {
        //If z is zero, or contains a NaN component display as white
        if ((getReal() == 0.0 && getImaginary()== 0.0) || Double.isNaN(getReal()) || Double.isNaN(getImaginary()) )
            return new Color(255, 255, 255);

        //Both components are +/-inf
        if (Double.isInfinite(getReal()) && Double.isInfinite(getImaginary()))
        {
            if (getReal() > 0 && getImaginary() > 0)
                return new Color(255, 0, 191) ;
            else if (getReal() < 0 && getImaginary() > 0)
                return new Color(0, 64, 255) ;
            else if (getReal() < 0 && getImaginary() < 0) 
                return new Color(0, 255, 64) ;
            else
                return new Color(255, 191, 0) ;
        }

        //Either one, or the other component is +/1 inf
        if (Double.isInfinite(getReal()) || Double.isInfinite(getImaginary()))
        {
            if (Double.isInfinite(getReal()) && getReal() > 0)
                return new Color(255, 0, 0) ;
            else if (Double.isInfinite(getReal()) && getReal() < 0)
                return new Color(0, 255, 255) ;
            else if(Double.isInfinite(getImaginary()) && getImaginary() > 0)
                return new Color(128, 0, 255) ;
            else
                return new Color(128, 255, 0) ;
        }
    
        float arg = (float)super.getArgument();
        float hue = arg;
        float modarg = (float)Math.log(super.abs());
        float lightness;

        //Convert argument from -pi to pi --> 0 to 2pi
        if (arg < 0)
            hue = 2.0f * (float)Math.PI + arg;
        
        //Convert from 0 to 2pi --> 0 to 1
        hue = 1.0f - hue / (2.0f * (float)Math.PI);
        
        if (modarg < 0)
        {
            lightness = 0.75f - (float)super.abs() / 2.0f;
        }
        else
        {
            if ((int)modarg % 2 == 0) //If whole part of modarg is even, 0 --> 1 maps to black --> white
                    lightness = (modarg - (float)Math.floor(modarg)) / 2.0f + 0.25f;
            else //If whole part of modarg is odd 0 --> 1 maps to white --> black
                    lightness = 0.75f - (modarg - (float)Math.floor(modarg)) / 2.0f;
        }
        
        return HLtoRGB(hue, lightness);
    }
    
    /**
     * Converts HSL model to RGB, synonymous with the kernel HSLtoRGB conversion. Uses a default value of 1.0f for saturation.
     * @param h hue
     * @param l lightness
     * @return the color in RGB format
     */
    private static Color HLtoRGB(float h, float l)
    {

            //  Formula needs all values between 0 - 1.


            float q = l < 0.5 ? l*2 : 1;

            float p = 2 * l - q;

            float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
            float g = Math.max(0, HueToRGB(p, q, h));
            float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

            r = Math.min(r, 1.0f);
            g = Math.min(g, 1.0f);
            b = Math.min(b, 1.0f);

            return new Color(r, g, b);
    }

    private static float HueToRGB(float p, float q, float h)
    {
            if (h < 0) h += 1;

            if (h > 1 ) h -= 1;

            if (6 * h < 1) return p + ((q - p) * 6 * h); 

            if (2 * h < 1 ) return  q;

            if (3 * h < 2) return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );

            return p;
    }
    
    /**
     * Private function to convert double to string. If the magnitude of the value is larger than 1e7, 
     * scientific notation is used, otherwise fixed point notation is used.
     * @param val value to convert
     * @return the formatted string
     */
    private static String myConvert(double val)
    {
        DecimalFormat form;
        String str, ans = "";
        
        if (val > 1e7 || val < -1e7 || (val > -1e-4 && val < 1e-4) && val != 0)
            form = new DecimalFormat("0.####E0");
        else
            form = new DecimalFormat("######0.####");
        
        str = form.format(val);
        
        for (int i = 0; i < str.length(); ++i)
        {
            switch (str.charAt(i)) {
                case '\uFFFD':
                    ans += "NaN";
                    break;
                case '\u221E':
                    ans += "infinity";
                    break;
                default:
                    ans += str.charAt(i);
                    break;
            }
        }
        
        return ans;
    }
    
    /**
     * Uses the default String.valueOf command to get exact, accurate
     * representation of the complex number.
     * @return the complex number as a string (most accurate)
     */
    public String toAccurateString()
    {
        if (getImaginary() == 0.0) //If the number is real
            return String.valueOf(getReal());
            
        if (getReal() == 0.0) //If the number is exclusively imaginary
            return Math.abs(getImaginary()) == 1.0 ? (getImaginary() < 0 ? "-i" : "i") : (String.valueOf(getImaginary()) + "*i");
        
        return String.valueOf(getReal()) + (getImaginary() < 0 ? " - " : " + ") + (Math.abs(getImaginary()) == 1 ? "i" : (String.valueOf(Math.abs(getImaginary())) + "*i"));
    }
    
    /**
     * Gets the complex number as a string, represented as polar coordinates.
     * @return the complex number as a string (polar coordinates)
     */
    public String toPolarString()
    {
        return myConvert(abs()) + "\u2220" + myConvert(getArgument());
    }
    
    /**
     * Gets the string representation of the complex number, using the double to 
     * string conversion used in myConvert function.
     * @return the complex number as a string
     */
    @Override
    public String toString()
    {
        if (getImaginary() == 0.0) //If the number is real
            return myConvert(getReal());
            
        if (getReal() == 0.0) //If the number is exclusively imaginary
            return Math.abs(getImaginary()) == 1.0 ? (getImaginary() < 0 ? "-i" : "i") : (myConvert(getImaginary()) + "*i");
        
        return myConvert(getReal()) + (getImaginary() < 0 ? " - " : " + ") + (Math.abs(getImaginary()) == 1.0 ? "i" : (myConvert(Math.abs(getImaginary())) + "*i"));
    }
}
