/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JComponent;

import org.apache.commons.math3.complex.Complex;

import com.nativelibs4java.opencl.*;
import com.nativelibs4java.opencl.util.*;
import com.nativelibs4java.util.*;
import java.awt.image.Raster;
import org.bridj.Pointer;
import static org.bridj.Pointer.*;
import static java.lang.Math.*;
import java.io.IOException;
import java.net.URL;

import complex.Token;

/**
 *  ComplexComponent is a widget responsible for painting complex landscapes
 * @author Will
 */
public class ComplexComponent extends JComponent {
    
    private Complex     _min;
    private Complex     _max;
    private Token[]     _tokens;
    private int         _stackmax;
    private CLContext   _context;
    private CLKernel    _kernel;
    private CLQueue     _queue;
    
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
        _context = JavaCL.createBestContext(CLPlatform.DeviceFeature.GPU);
        
        _tokens = new Token[] { new Token(new Complex(0, 0), Token.INSTRUCTION.VARIABLE),
                                new Token(new Complex(8, 0), Token.INSTRUCTION.OPERATOR) };
        _stackmax = 1;
        
        System.out.println("Local memory size: " + _context.getDevices()[0].getLocalMemSize());
                
        for (int i = 0; i < JavaCL.listPlatforms().length; ++i)
        {
            
            System.out.println("Platform: " + i + " is " + JavaCL.listPlatforms()[i].getName());
            for (int j = 0; j < JavaCL.listPlatforms()[i].listAllDevices(true).length; ++j)
                System.out.println("    Device: " + j + " " + JavaCL.listPlatforms()[i].listAllDevices(true)[j].getName());
        }
        
        try
        {
            _queue = _context.createDefaultQueue();
            String src = IOUtils.readText(ComplexComponent.class.getResource("/kernel/kernel.cl"));
            CLProgram program = _context.createProgram(src);
            program.addInclude("/include/");
            _kernel = program.createKernel("get_landscape");
            
            for (int i = 0; i < _context.getDevices().length; ++i)
                System.out.println("Using Device " + i + " " + _context.getDevices()[i].getName());
            
            //_outbuff = _context.createIntBuffer(CLMem.Usage.Output, getWidth() * getHeight());
        }
        catch (IOException e)
        {
            
        }
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
    
    public int getArea()
    {
        return getWidth() * getHeight();
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
        
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        
        //Token list is implemented as a list of floats. Each group of three floats represents real, imaginary and type of a token
        Pointer<Float> tokenPtr = allocateFloats(_tokens.length * 3); //.order(_context.getByteOrder());
        
        //Copy tokens
        for (int i = 0; i < _tokens.length; ++i)
        {
            tokenPtr.set(3*i, (float)_tokens[i].getData().getReal());
            tokenPtr.set(3*i+1, (float)_tokens[i].getData().getImaginary());
            tokenPtr.set(3*i+2, (float)_tokens[i].getInt());
        }
        
        //Create input buffer for token list
        CLBuffer<Float> tokenBuff = _context.createFloatBuffer(CLMem.Usage.Input, tokenPtr);
        
        // Create output buffer for colour array :
        CLBuffer<Integer> outbuff = _context.createIntBuffer(CLMem.Usage.Output, getArea());
        
        CLBuffer<Double> stackbuff = _context.createDoubleBuffer(CLMem.Usage.Output, getArea() * _stackmax * 2);
                
        // Get and call the kernel :
        _kernel.setArgs(
                tokenBuff,
                _tokens.length,
                (float)_min.getReal(), 
                (float)_min.getImaginary(), 
                (float)(_max.getReal() - _min.getReal()), 
                (float)(_max.getImaginary() - _min.getImaginary()),
                getWidth(), getHeight(), outbuff, getArea(), _stackmax, stackbuff);
        //_kernel.setLocalArg(10, 8 * 2* 50); //When adding argumnts to _kernel, be sure to update the first argument of this function
        
        int[] globalSizes = new int[] { getArea() };
        
        //Send kernel to event queue
        CLEvent addEvt = _kernel.enqueueNDRange(_queue, globalSizes);
        
        //Wait for completion and get results
        outbuff.read(_queue, addEvt).getInts(imagePixelData); // blocks until add_floats finished
        
        /*for (int i = 0; i < 1000; ++i)
            System.out.println("ID: " + imagePixelData[i]);*/

        return bufferedImage;
   }
    
}
