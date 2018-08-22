/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author Will
 */
public class Concurrent extends Thread {
    
    private Thread            _thread;
    
    private final int         _start;
    
    private final Evaluator   _evaltor;
    
    private final Complex     _min;
    private final Complex     _max;
    
    private final int         _width;
    private final int         _height;
    
    private final int[]       _data;
    
        
    public Concurrent(Evaluator eval, int start, int width, int height, Complex min, Complex max, int[] data)
    {
        _start = start;
        _evaltor = eval;
        _width = width;
        _height = height;
        _min = min;
        _max = max;
        _data = data;
    }
    
    @Override public void run()
    {
        Complex z;
        for (int j = 0; j < _height; ++j)
        {
            for (int i = 0; i < _width; ++i)
            {
                int index = j * _width + i + _start;
                z = new Complex(_min.getReal() + (_max.getReal() - _min.getReal()) * i / _width,
                                 _min.getImaginary()+ (_max.getImaginary() - _min.getImaginary()) * j / _height);
                _data[index] = ComplexColor.color(_evaltor.f(z)).getRGB();
            }
        }
    }
    
    @Override public void start()
    {
        super.start();
        if (_thread == null)
        {
            _thread = new Thread(this, "COMPLEX " + _start);
            _thread.setPriority(8);
            _thread.start();
        }
    }
}
