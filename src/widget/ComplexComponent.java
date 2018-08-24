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
import java.nio.ByteOrder;
import static org.bridj.Pointer.*;
import static java.lang.Math.*;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Will
 */
public class ComplexComponent extends JComponent {
    
    private Complex     _min;
    private Complex     _max;
    private CLContext _context;
    private CLKernel _kernel;
    private CLQueue _queue;
    
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
        
        
                
        /*Pointer<Double>
            aPtr = allocateDoubles(n).order(byteOrder),
            bPtr = allocateDoubles(n).order(byteOrder);*/
        
        // Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
        /*CLBuffer<Double> 
            a = context.createDoubleBuffer(CLMem.Usage.Input, aPtr),
            b = context.createDoubleBuffer(CLMem.Usage.Input, bPtr);*/
        
        // Create an OpenCL output buffer :
        CLBuffer<Integer> _outbuff = _context.createIntBuffer(CLMem.Usage.Output, getArea());
        
        // Get and call the kernel :
        _kernel.setArgs(
                (float)_min.getReal(), 
                (float)_min.getImaginary(), 
                (float)(_max.getReal() - _min.getReal()), 
                (float)(_max.getImaginary() - _min.getImaginary()),
                getWidth(), getHeight(), _outbuff, getArea());
        _kernel.setLocalArg(8, 4 * 2 * 100);
        
        int[] globalSizes = new int[] { getArea() };
        
        //Send kernel to event queue
        CLEvent addEvt = _kernel.enqueueNDRange(_queue, globalSizes);
        
        //Wait for completion and get results
        _outbuff.read(_queue, addEvt).getInts(imagePixelData); // blocks until add_floats finished

        return bufferedImage;
   }
    
}
