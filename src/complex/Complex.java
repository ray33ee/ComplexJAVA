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
    public Complex() { super(0); }
    
    public Complex(double real) { super(real); }
    
    public Complex(double real, double imag) { super(real, imag); }
    
    public Complex(org.apache.commons.math3.complex.Complex z) { super(z.getReal(), z.getImaginary()); }
    
    public Complex add(Complex z) { return new Complex(super.add(z)); }
    
    public Complex subtract(Complex z) { return new Complex(super.subtract(z)); }
    
    public Complex multiply(Complex z) { return new Complex(super.multiply(z)); }
    
    @Override
    public Complex multiply(double r) { return new Complex(super.multiply(r)); }
    
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
    
    public Complex asinh() { return new Complex(super.asin()); }
    
    public Complex acosh() { return new Complex(super.acos()); }
    
    public Complex atanh() { return new Complex(super.atan()); }
    
    @Override public Complex sinh() { return new Complex(super.sinh()); }
    
    @Override public Complex cosh() { return new Complex(super.cosh()); }
    
    @Override public Complex tanh() { return new Complex(super.tanh()); }
    
    
    /**
    
    * Gets the colour of the complex number, as per the domain colouring method
     * @param z the complex number, z
     * @return  the colour of the complex number, z
     */
    public Color color()
    {
        float arg = (float)super.getArgument();
        float hue = arg;
        
        //Convert argument from -pi to pi --> 0 to 2pi
        if (arg < 0)
            hue = 2.0f * (float)Math.PI + arg;
        
        //Convert from 0 to 2pi --> 0 to 1
        hue /= 2.0f * (float)Math.PI;
        
        return HSLtoRGB(hue, 1.0f, 0.5f);
    }
    

    private static Color HSLtoRGB(float h, float s, float l)
    {

            //  Formula needs all values between 0 - 1.

            //h = h % 360.0f;

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

            if (6 * h < 1)
            {
                    return p + ((q - p) * 6 * h);
            }

            if (2 * h < 1 )
            {
                    return  q;
            }

            if (3 * h < 2)
            {
                    return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );
            }

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
        
        if (val > 1e7 || val < -1e7)
            form = new DecimalFormat("0.####E0");
        else
            form = new DecimalFormat("######0.####");
        
        return form.format(val);
    }
    
    @Override
    public String toString()
    {
        if (getImaginary() == 0.0) //If the number is real
            return myConvert(getReal());
            
        if (getReal() == 0.0) //If the number is exclusively imaginary
            return myConvert(getImaginary()) + "*i";
        
        return myConvert(getReal()) + (getImaginary() < 0 ? " - " : " + ") + (Math.abs(getImaginary()) == 1 ? "i" : (myConvert(Math.abs(getImaginary())) + "*i"));
    }
}
