/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JComponent;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Will
 */
public class ComplexComponent extends JComponent {
    
    private Evaluator   _evaltor;
    private Complex     _min;
    private Complex     _max;
    
    /**
     * Construct Complex component and initialise JComponent and member variables.
     * @param min   the lower left position of the domain
     * @param max   the upper right position of the domain
     */
    public ComplexComponent(Complex min, Complex max)
    {
        super();
        _min = min;
        _max = max;
        _evaltor = new Evaluator();
    }
    
    /**
     * Gets the width of the image, in pixels
     * 
     * @return the width of the image
     */
    public int getWidth()
    {
        return super.getWidth() - 10;
    }
    
    /**
     * Gets the height of the image, in pixels
     * 
     * @return the height of the image
     */
    public int getHeight()
    {
        return super.getHeight() - 10 ;
    }
    
    /**
     * Gets the minimum domain of the complex landscape (lower left coordinate)
     * @return the lower left position of the domain
     */
    public Complex getMin()
    {
        return _min;
    }
    
    /**
     * Gets the maximum domain of the complex landscape (upper right coordinate)
     * @return the upper right position of the domain
     */
    public Complex getMax()
    {
        return _max;
    }
    
    public void paint(Graphics g) {
        Image img = drawImage();
        g.drawImage(img, 10, 10,this);
    }

    /**
     * Construct an image of the landscape. 
     * @return the image of the landscape
     */
    private Image drawImage(){
       
       int w = getWidth();
       int h = getHeight();
       
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
      
        
        Complex z;
        
        for (int j = 0; j < getHeight(); ++j)
        {
            for (int i = 0; i < getWidth(); ++i)
            {
                int index = j * getWidth() + i;
                z = new Complex(_min.getReal() + (_max.getReal() - _min.getReal()) * i / getWidth(),
                                 _min.getImaginary()+ (_max.getImaginary() - _min.getImaginary()) * j / getHeight());
                imagePixelData[index] = Complex2.color(_evaltor.f(z)).getRGB();
            }
        }
      
        return bufferedImage;
   }
    
}
