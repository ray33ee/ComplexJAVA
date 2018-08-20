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

/**
 *
 * @author Will
 */
public class ComplexComponent extends JComponent {
    
    private Complex2 _min;
    private Complex2 _max;
    
    public ComplexComponent(Complex2 min, Complex2 max)
    {
        super();
        _min = min;
        _max = max;
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
    public Complex2 getMin()
    {
        return _min;
    }
    
    /**
     * Gets the maximum domain of the complex landscape (upper right coordinate)
     * @return the upper right position of the domain
     */
    public Complex2 getMax()
    {
        return _max;
    }
    
    public void paint(Graphics g) {
        Image img = drawImage();
        g.drawImage(img, 10, 10,this);
    }

   private Image drawImage(){
       
       int w = getWidth();
       int h = getHeight();
       
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
      
        Complex2 z;        
        
        for (int j = 0; j < getHeight(); ++j)
        {
            for (int i = 0; i < getWidth(); ++i)
            {
                int index = j * getWidth() + i;
                z = new Complex2(_min.getReal() + (_max.getReal() - _min.getReal()) * i / getWidth(),
                                 _min.getImaginary()+ (_max.getImaginary() - _min.getImaginary()) * j / getHeight());
                imagePixelData[index] = z.color().getRGB();
            }
        }
      
        return bufferedImage;
   }
    
}
