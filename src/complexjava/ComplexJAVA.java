/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complexjava;


import classes.Complex2;
/**
 *
 * @author Will
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.image.DataBufferInt;

import java.awt.Color;
import classes.Complex2;


/**
 *
 * @author Will
 */
    /**
     * @param args the command line arguments
     */


public class ComplexJAVA extends JPanel {

   public void paint(Graphics g) {
        Image img = createImageWithText();
        g.drawImage(img, 20,20,this);
   }

   private Image createImageWithText(){
        BufferedImage bufferedImage = new BufferedImage(1900,1000,BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
      
        Complex2 c = new Complex2(0.0f, -1.0f);
      
        System.out.println(c.getArgument());
      
      
        for (int i = 0; i < 1900*1000; ++i)
        {
            imagePixelData[i] = c.color().getRGB();
        }
      
      
        return bufferedImage;
   }
   
   public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTextPane textb = new JTextPane();
        frame.getContentPane().add(new ComplexJAVA());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1900, 1000);
        frame.setVisible(true);

        textb.setSize(100, 100);
        textb.setVisible(true);
      
      
      
      
      
      
      
      
   }
    
}



