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

/**
 *
 * @author Will
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JFrame;
import classes.ComplexComponent;

public class ComplexJAVA  {

   
   /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        ComplexComponent pan = new ComplexComponent();
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);
        frame.setVisible(true);
        
        frame.getContentPane().add(pan);
      
   }
    
}



