/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

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
       
        
        int threadcount = 8;
       
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        Concurrent[] threads = new Concurrent[threadcount];
        
        double diff = _max.getImaginary() - _min.getImaginary();
        int blocksize = (getHeight() / threadcount) * getWidth();  
        
        for (int i = 0; i < threadcount; ++i)
        {
            threads[i] = new Concurrent(_evaltor, blocksize * i, getWidth(), getHeight() / threadcount, 
                    new Complex(_min.getReal(), _min.getImaginary() + diff / threadcount * i), 
                    new Complex(_max.getReal(), _min.getImaginary() + diff / threadcount * (i+1)), imagePixelData);
            threads[i].start();
        }
        
        try 
        {
            for (int i = 0; i < threadcount; ++i)
                threads[i].join();
        } 
        catch ( InterruptedException e) 
        {
           System.out.println("Interrupted");
        }
        
        return bufferedImage;
   }
    
}
