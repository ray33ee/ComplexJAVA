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
import complex.ComplexComponent;
import complex.Concurrent;
import complex.Evaluator;
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
        ComplexComponent pan = new ComplexComponent(new Complex(-4,-4), new Complex(4,4));
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 1000);
        frame.setVisible(true);
        
        frame.getContentPane().add(pan);
        
        /*Evaluator _evaltor = new Evaluator();
        
        int[] data = new int[1800*1000];
        Concurrent c = new Concurrent(_evaltor, 0, 1800, 1000, new Complex(-4,-4), new Complex(4,4), data);
        
        c.start();
        
        try 
        {
            c.join();
        } 
        catch ( InterruptedException e) 
        {
           System.out.println("Interrupted");
        }
        
        data = c.getData();
        
        System.out.println("complex.ComplexComponent.drawImage() " + data[0]);*/
      
   }
    
}



