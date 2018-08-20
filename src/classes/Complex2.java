/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import org.apache.commons.math3.complex.Complex;
import java.awt.Color;

/**
 *
 * @author Will
 */
public class Complex2 extends Complex {
    
    public Complex2(double re, double im)
    {
        super(re, im);
    }
    
    public Complex2(double re)
    {
        super(re);
    }
    
    public Color color()
    {
        float arg = (float)getArgument();
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

            float q = 0;

            if (l < 0.5)
                    q = l * (1 + s);
            else
                    q = (l + s) - (s * l);

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
    
}


