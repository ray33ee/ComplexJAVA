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

import javax.swing.JFrame;
import widget.ComplexComponent;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Will
 */
public class ComplexJAVA  {

   /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        ComplexComponent pan = new ComplexComponent(new Complex(-10,-10), new Complex(10,10));
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 1000);
        frame.setVisible(true);
        
        frame.getContentPane().add(pan);
      
   }
    
}



