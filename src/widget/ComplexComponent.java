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

import com.nativelibs4java.opencl.*;
import com.nativelibs4java.util.*;
import org.bridj.Pointer;
import static org.bridj.Pointer.*;
import java.io.IOException;

import complex.Token;
import complex.Landscape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *  ComplexComponent is a widget responsible for painting complex landscapes
 * @author Will
 */
public class ComplexComponent extends JComponent implements MouseMotionListener, MouseListener  {
    
    enum ActionType { PAN, ZOOM, NEWTON };
    
    /**
     * The number of real numbers per complex number. Since this is always one real component and one imaginary component, this is always 2
     */
    private final int REALS_PER_COMPLEX = 2;
    
    private CLContext   _context;
    private CLKernel    _kernel;
    private CLQueue     _queue;
    
    /**
     * Contains the variables needed to draw a complex landscape. See Landscape class.
     */
    private Landscape _landscape;
    
    /**
     * Defines which action to user interacts with component
     */
    private ActionType _action;
    
    /**
     * Construct Complex component and initialise JComponent and member variables.
     * @param land the initial landscape to draw
     */
    public ComplexComponent(Landscape land)
    {
        super();
        _context = JavaCL.createBestContext(CLPlatform.DeviceFeature.DoubleSupport);
        
        _landscape = new Landscape(land.getEvaluator(), land.getMinDomain(), land.getMaxDomain());
        
        _action = ActionType.PAN; 
       
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
        }
        catch (IOException e)
        {
            
        }       
        
        addMouseMotionListener(this);
        addMouseListener(this);
    }
    
    /**
     * Get the area of the image, in pixels.
     * @return the area, getWidth() * getHeight()
     */
    public int getArea() { return getWidth() * getHeight(); }
    
    public Landscape getLandscape() { return _landscape; }
    
    public void setLandscape(Landscape land) { _landscape = land; }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        System.out.println("MOVE: " + e.toString());
    }
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        System.out.println("DOWN");
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        System.out.println("UP");
    }
    
    @Override
    public void paint(Graphics g) { Image img = drawImage(); g.drawImage(img, 0, 0,this); }

    /**
     * Construct an image of the landscape. 
     * @return the image of the landscape
     */
    private Image drawImage()
    {
        //Get a reference to the underlying image pixel data
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] imagePixelData = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
        
        //Get a copy of the token list
        Token[] list = _landscape.getEvaluator().getTokens();
        
        //Token list is implemented as a list of floats. Each group of three floats represents real, imaginary and type of a token
        Pointer<Float> tokenPtr = allocateFloats(list.length * 3); //.order(_context.getByteOrder());
        
        //Copy tokens
        for (int i = 0; i < list.length; ++i)
        {
            tokenPtr.set(3*i, (float)list[i].getData().getReal());
            tokenPtr.set(3*i+1, (float)list[i].getData().getImaginary());
            tokenPtr.set(3*i+2, (float)list[i].getInt());
        }
        
        //Create input buffer for token list
        CLBuffer<Float> tokenBuff = _context.createFloatBuffer(CLMem.Usage.Input, tokenPtr);
        
        // Create output buffer for colour array :
        CLBuffer<Integer> outbuff = _context.createIntBuffer(CLMem.Usage.Output, getArea());
        
        //Create a stack of floats or doubles, depending on support
        CLBuffer stackbuff;
        if (_context.isDoubleSupported())
            stackbuff = _context.createDoubleBuffer(CLMem.Usage.Output, getArea() * _landscape.getEvaluator().getStackMax() * REALS_PER_COMPLEX);
        else
            stackbuff = _context.createFloatBuffer(CLMem.Usage.Output, getArea() * _landscape.getEvaluator().getStackMax() * REALS_PER_COMPLEX);
        
        // Get and call the kernel :
        _kernel.setArgs(
                tokenBuff, stackbuff,
                list.length,
                (float)_landscape.getMinDomain().getReal(), 
                (float)_landscape.getMinDomain().getImaginary(), 
                (float)(_landscape.getMaxDomain().getReal() - _landscape.getMinDomain().getReal()), 
                (float)(_landscape.getMaxDomain().getImaginary() - _landscape.getMinDomain().getImaginary()),
                getWidth(), getHeight(), outbuff, getArea(), _landscape.getEvaluator().getStackMax());
        
        int[] globalSizes = new int[] { getWidth(), getHeight() };
        
        //Send kernel to event queue
        CLEvent addEvt = _kernel.enqueueNDRange(_queue, globalSizes);
        
        //Wait for completion and get results
        outbuff.read(_queue, addEvt).getInts(imagePixelData); // blocks until add_floats finished
        
        /*for (int i = 0; i < 10; ++i)
            System.out.println("DEBUG imagePixelData[" + i + "] = " + imagePixelData[i]);*/

        return bufferedImage;
   }

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}
    
    @Override public void mouseDragged(MouseEvent e) {}
    
}
