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

import complex.Token;
import complex.Landscape;
import complex.evaluator.Evaluator;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *  ComplexComponent is a widget responsible for painting complex landscapes and handling mouse events
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
     * Stores the action being used. Either Pan, zoom or newton raphson. 
     */
    private ActionType _action;
    
    /**
     * History class responsible for keeping track of undo/redo history
     */
    private History<Landscape> _history;
    
    /**
     * Stores the complex value at the position where the user has initiated a mousePress event
     */
    private Complex _press; 
    
    /**
     * Stores reference to parent object
     */
    private final MainFrame _parent;
    
    /**
     * Construct Complex component and initialise JComponent and member variables.
     * @param parent the parent of the Component
     */
    public ComplexComponent(MainFrame parent)
    {
        super();
        
        _history = new History();
        
        //Add first landscape, using a default Evaluator
        _history.add(new Landscape(new Evaluator(), new Complex(-10,-10), new Complex(10,10)));
        
        //Set the default action to pan
        _action = ActionType.PAN; 
        
        //Set initial press value
        _press = new Complex();

        _parent = parent;
        
        prioritiseSpeed(false);

        
            
        super.addMouseMotionListener(this);
        super.addMouseListener(this);
    }
    
    /**
     * Changes the priority of the OpenCL device selection from fast to oaccurate. If priority is speed,
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
        
        String src = "";
        try
        {
            src = IOUtils.readText(ComplexComponent.class.getResource("/kernel/kernel.cl"));
        }
        catch (IOException e)
        {
            System.err.println("IOException in widget.ComplexComponent.<init>()" + e.getMessage());
        }
        CLProgram program = _context.createProgram(src);
        program.addInclude("/include/");
        _kernel = program.createKernel("get_landscape");
        
        
        //Display all devices in the selected context
        System.out.println("Prioritising " + (fast ? "speed" : "accuracy") + ", displaying chosen device(s)...");
        for (int i = 0; i < _context.getDevices().length; ++i)
            System.out.println("    " + _context.getDevices()[i].getName());
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
        
        _history.add(land);
        changeLandscape(_history.getCurrent());
    }
    
    /**
     * Revert to the last landscape
     */
    public void undo()
    {
        if (!_history.isAtBottom())
        {
            _history.undo();
            repaint();
        }
    }
    
    /**
     * Revert to the previously undone landscape
     */
    public void redo()
    {
        if (!_history.isAtTop())
        {
            _history.redo();
            repaint();
        }
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
     * Get the area of the image, in pixels.
     * @return the area, getWidth() * getHeight()
     */
    public int getArea() { return getWidth() * getHeight(); }
    
    /**
     * Set the action for the component, either pan, zoom or newton/raphson method
     * @param a 
     */
    public void setAction(ActionType a) { _action = a; }
    
    
    
    /**
     * Get the current landscape on display
     * @return _landscape
     */
    public Landscape getLandscape() { return _history.getCurrent(); }
    
    /**
     * Set the current landscape
     * @param land the landscape to set to
     */
    public void changeLandscape(Landscape land) { _history.add(land); super.repaint(); }
    
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
        Complex release;
        Landscape land;
        switch (_action)
        {
            case PAN:
                release = trace(e.getPoint());
                Complex movement = release.subtract(_press);

                land = new Landscape(_history.getCurrent().getEvaluator(), _history.getCurrent().getMinDomain().subtract(movement),  _history.getCurrent().getMaxDomain().subtract(movement));

                changeLandscape(land);
                break;
            case ZOOM:
                release = trace(e.getPoint());
                
                Complex min = new Complex (Math.min(_press.getReal(), release.getReal()), Math.min(_press.getImaginary(), release.getImaginary()));
                Complex max = new Complex (Math.max(_press.getReal(), release.getReal()), Math.max(_press.getImaginary(), release.getImaginary()));
                
                land = new Landscape(_history.getCurrent().getEvaluator(), min, max);
                
                changeLandscape(land);
                break;
            case NEWTON:
                
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
        
        //Get a copy of the token list
        Token[] list = _history.getCurrent().getEvaluator().getTokens();
        
        CLBuffer tokenBuff;
        if (_context.isDoubleSupported()) //_context.isDoubleSupported()
        {
            //Token list is implemented as a list of floats. Each group of three floats represents real, imaginary and type of a token
            Pointer<Double> tokenPtr = allocateDoubles(list.length * 3); //.order(_context.getByteOrder());

            //Copy tokens
            for (int i = 0; i < list.length; ++i)
            {
                tokenPtr.set(3*i, list[i].getData().getReal());
                tokenPtr.set(3*i+1, list[i].getData().getImaginary());
                tokenPtr.set(3*i+2, (double)list[i].getInt());
            }

            //Create input buffer for token list
            tokenBuff = _context.createDoubleBuffer(CLMem.Usage.Input, tokenPtr);
        }
        else
        {
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
                list.length,
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
                list.length,
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
