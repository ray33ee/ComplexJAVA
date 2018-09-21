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
import com.nativelibs4java.opencl.CLPlatform.DeviceFeature;
import com.nativelibs4java.util.*;
import complex.Complex;
import complex.History;
import org.bridj.Pointer;
import static org.bridj.Pointer.*;
import java.io.IOException;

import complex.Landscape;
import complex.exceptions.RootFinderException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;

/**
 *  ComplexComponent is a widget responsible for painting complex landscapes and handling mouse events
 * @author Will
 */
public class ComplexComponent extends JComponent implements MouseMotionListener, MouseListener  {
    
    enum ActionType { PAN, ZOOM, NEWTON };
    
    /** The number of real numbers per complex number. Since this is always one real component and one imaginary component, this is always 2 */
    private final int REALS_PER_COMPLEX = 2;
    
    private CLContext   _context;
    private CLKernel    _kernel;
    private CLQueue     _queue;
    
    /** Stores the action being used. Either Pan, zoom or newton raphson. */
    private ActionType _action;
    
    /** History class responsible for keeping track of undo/redo history */
    private final History<Landscape> _history;
    
    /** Stores the complex value at the position where the user has initiated a mousePress event */
    private Complex _press; 
    
    /** Stores reference to parent object */
    private final MainFrame _parent;
    
    private static final String realSource;
    
    private static final String complexSource;
    
    private static final String kernelSource;
    
    static
    {
        try
        {
            java.net.URL complexpath = ComplexComponent.class.getResource("/inc/real.h");
            java.net.URL realpath = ComplexComponent.class.getResource("/inc/complex.h");
            java.net.URL kernelpath = ComplexComponent.class.getResource("/kernel/kernel.cl");
            
            if (kernelpath == null || realpath == null || complexpath == null)
                throw new RuntimeException("Exception at widget.ComplexComponent.prioritiseSpeed(), error \"" + new NullPointerException().toString() + "\". Could not find resource.");
            
            complexSource = IOUtils.readText(complexpath);
            realSource = IOUtils.readText(realpath);
            kernelSource = IOUtils.readText(kernelpath);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException at widget.ComplexComponent.prioritiseSpeed(), error \"" + e.toString() + "\". Problem with accessing kernel source code.");
        }
    }
    
    /**
     * Construct Complex component and initialise JComponent and member variables.
     * @param first the first landscape to draw on creation
     * @param parent the parent of the Component
     */
    public ComplexComponent(Landscape first, MainFrame parent)
    {
        super();
        
       /*if (!JavaCL.OpenCLProbeLibrary.hasOpenCL1_0())
            throw new Error("No OpenCL");*/
        
        _history = new History();
        
        //Set the default action to pan
        _action = ActionType.PAN; 
        
        //Set initial press value
        _press = new Complex();

        _parent = parent;
        
        //Add first landscape, using a default Evaluator
        changeLandscape(first);
        
        prioritiseSpeed(false);

        super.addMouseMotionListener(this);
        super.addMouseListener(this);
    }
    
    /**
     * Get the area of the image, in pixels.
     * @return the area, getWidth() * getHeight()
     */
    public int getArea() { return getWidth() * getHeight(); }
    
    /**
     * Get the current landscape on display
     * @return _landscape
     */
    public Landscape getLandscape() { return _history.getCurrent(); }
    
    /**
     * Get the current history
     * @return the history object
     */
    public History getHistory() { return _history; }
    
    /**
     * Set the action for the component, either pan, zoom or newton/raphson method
     * @param a 
     */
    public void setAction(ActionType a) { _action = a; }
    
    /**
     * Changes the priority of the OpenCL device selection from fast to accurate. If priority is speed,
     * JavaCL will select the fastest context available. If accuracy is the chosen priority, the fastest
     * context with double precision support is chosen.
     * @param fast choose true to prioritise speed
     */
    public void prioritiseSpeed(boolean fast)
    {
        DeviceFeature df; 
        
        if (fast)
            df = DeviceFeature.MaxComputeUnits;
        else
            df = DeviceFeature.DoubleSupport;
        
        _context = JavaCL.createBestContext(df);
        
        _queue = _context.createDefaultQueue();
        
        CLProgram program = _context.createProgram(complexSource, realSource, kernelSource);
        
        _kernel = program.createKernel("get_landscape");
        
        //Display all devices in the selected context
        System.out.println("Prioritising " + (fast ? "speed" : "accuracy") + ", displaying chosen device(s)...");
        for (int i = 0; i < _context.getDevices().length; ++i)
            System.out.println("    " + _context.getDevices()[i].getName());
    }
    
    /** Center the current landscape on 0+0i, whilst preserving scale. */
    public void zeroCenter()
    {
        Complex min = _history.getCurrent().getMinDomain();
        Complex max = _history.getCurrent().getMaxDomain();
        
        Complex diff = max.subtract(min).divide(2.0);
        
        changeLandscape(new Landscape(_history.getCurrent().getEvaluator(), diff.negate(), diff));
    }
    
    /**
     * Performs a zoom about the center of the landscape with the given factor
     * @param factor the zoom scale factor
     */
    public void centerZoom(double factor)
    {
        Complex min = _history.getCurrent().getMinDomain();
        Complex max = _history.getCurrent().getMaxDomain();
        
        Complex diff = max.subtract(min);
        
        Landscape land = new Landscape(_history.getCurrent().getEvaluator(), min.subtract(diff.multiply(factor)), max.add(diff.multiply(factor)));
        
        changeLandscape(land);
    }
    
    /** Revert to the last landscape */
    public void undo()
    {
        if (!_history.isAtBottom())
        {
            _history.undo();
            repaint();
            _parent.landscapeChange(_history.getCurrent());
        }
    }
    
    /** Revert to the previously undone landscape */
    public void redo()
    {
        if (!_history.isAtTop())
        {
            _history.redo();
            repaint();
            _parent.landscapeChange(_history.getCurrent());
        }
    }
    
    /**
     * Revert to the ith event in history
     * @param i ith element to revert to
     */
    public void revert(int i)
    {
        _history.revert(i);
        repaint();
        _parent.landscapeChange(_history.getCurrent());
    }
    
    /**
     * Gets the complex value the user is pointing to.
     * @param p the point from which to get the trace
     * @return the complex value at point p
     */
    public Complex trace(Point p) 
    {
        Complex diff = new Complex(_history.getCurrent().getMaxDomain().getReal() - _history.getCurrent().getMinDomain().getReal(), _history.getCurrent().getMinDomain().getImaginary() - _history.getCurrent().getMaxDomain().getImaginary());
        
        return new Complex((double)p.x / getWidth() * diff.getReal() + _history.getCurrent().getMinDomain().getReal(), (double)p.y / getHeight()* diff.getImaginary()+ _history.getCurrent().getMaxDomain().getImaginary());
    }
    
    /**
     * Set the current landscape
     * @param land the landscape to set to
     */
    public void changeLandscape(Landscape land) { _history.add(land); super.repaint(); _parent.landscapeChange(land); }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        _parent.onTrace(trace(e.getPoint()));
    }
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        if (_action == ActionType.PAN || _action == ActionType.ZOOM)
            _press = trace(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        Complex release = trace(e.getPoint());;
        Landscape land;
        switch (_action)
        {
            case PAN:
                Complex movement = release.subtract(_press);

                land = new Landscape(_history.getCurrent().getEvaluator(), _history.getCurrent().getMinDomain().subtract(movement),  _history.getCurrent().getMaxDomain().subtract(movement));

                changeLandscape(land);
                break;
            case ZOOM:                
                Complex min = new Complex (Math.min(_press.getReal(), release.getReal()), Math.min(_press.getImaginary(), release.getImaginary()));
                Complex max = new Complex (Math.max(_press.getReal(), release.getReal()), Math.max(_press.getImaginary(), release.getImaginary()));
                
                land = new Landscape(_history.getCurrent().getEvaluator(), min, max);
                
                changeLandscape(land);
                break;
            case NEWTON:
                try
                {
                    Complex root = _history.getCurrent().getEvaluator().newton_raphson(release);
                    JOptionPane.showMessageDialog(this, "Root found at: " + root.toString() + " (" + root.toPolarString() + ")", "Input box error", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (RootFinderException ex)
                {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Root finder error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
        
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
        
        CLBuffer tokenBuff;
        if (_context.isDoubleSupported()) //_context.isDoubleSupported()
        {
            //Token list is implemented as a list of floats. Each group of three floats represents real, imaginary and type of a token
            Pointer<Double> tokenPtr = allocateDoubles(_history.getCurrent().getEvaluator().getTokenLength() * 3).setArray(_history.getCurrent().getEvaluator().getTokensDouble());

            //Create input buffer for token list
            tokenBuff = _context.createDoubleBuffer(CLMem.Usage.Input, tokenPtr);
        }
        else
        {
            //Token list is implemented as a list of floats. Each group of three floats represents real, imaginary and type of a token
            Pointer<Float> tokenPtr = allocateFloats(_history.getCurrent().getEvaluator().getTokenLength() * 3).setArray(_history.getCurrent().getEvaluator().getTokensFloat()); //.order(_context.getByteOrder());

            //Create input buffer for token list
            tokenBuff = _context.createFloatBuffer(CLMem.Usage.Input, tokenPtr);
        }
        // Create output buffer for colour array :
        CLBuffer<Integer> outbuff = _context.createIntBuffer(CLMem.Usage.Output, getArea());
        
        //Create a stack of floats or doubles, depending on support
        CLBuffer stackbuff;
        if (_context.isDoubleSupported())
            stackbuff = _context.createDoubleBuffer(CLMem.Usage.Output, getArea() * _history.getCurrent().getEvaluator().getStackMax() * REALS_PER_COMPLEX);
        else
            stackbuff = _context.createFloatBuffer(CLMem.Usage.Output, getArea() * _history.getCurrent().getEvaluator().getStackMax() * REALS_PER_COMPLEX);
        
        // Get and call the kernel : max.imag and min.imag have been swapped because 0,0 (which is top left) should be bottom 
        if (_context.isDoubleSupported())
        {
            _kernel.setArgs(
                tokenBuff, stackbuff,
                _history.getCurrent().getEvaluator().getTokenLength(),
                _history.getCurrent().getMinDomain().getReal(), 
                _history.getCurrent().getMaxDomain().getImaginary(), 
                (_history.getCurrent().getMaxDomain().getReal() - _history.getCurrent().getMinDomain().getReal()), 
                (_history.getCurrent().getMinDomain().getImaginary() - _history.getCurrent().getMaxDomain().getImaginary()),
                getWidth(), getHeight(), outbuff, getArea(), _history.getCurrent().getEvaluator().getStackMax());
        }
        else
        {
            _kernel.setArgs(
                tokenBuff, stackbuff,
                _history.getCurrent().getEvaluator().getTokenLength(),
                (float)_history.getCurrent().getMinDomain().getReal(), 
                (float)_history.getCurrent().getMaxDomain().getImaginary(), 
                (float)(_history.getCurrent().getMaxDomain().getReal() - _history.getCurrent().getMinDomain().getReal()), 
                (float)(_history.getCurrent().getMinDomain().getImaginary() - _history.getCurrent().getMaxDomain().getImaginary()),
                getWidth(), getHeight(), outbuff, getArea(), _history.getCurrent().getEvaluator().getStackMax());
        }
        int[] globalSizes = new int[] { getWidth(), getHeight() };
        
        //Send kernel to event queue
        CLEvent addEvt = _kernel.enqueueNDRange(_queue, globalSizes);
        
        //Wait for completion and get results
        outbuff.read(_queue, addEvt).getInts(imagePixelData); // blocks until add_floats finished

        return bufferedImage;
   }

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}
    
    @Override public void mouseDragged(MouseEvent e) {}
    
}
