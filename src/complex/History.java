/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import java.util.ArrayList;

/**
 * History class (as well as being a dry subject) contains the entire history in an 
 * recorded after any change, undo or redo initiated.
 * @author Will
 * @param <T> type of object in list
 */
public class History<T>
{
    /**
     * Variable responsible for storing usage history
     */
    private  ArrayList<Object> _history;
    
    /**
     * pointer to next item
     */
    private int _pointer;
    
    public History() { _history = new ArrayList<>(); _pointer = 0; }
    
    public void add(T val) 
    {
        if (_pointer == _history.size())
            _history.add(val);
        else
        {
            _history.set(_pointer, val);
            _history = new ArrayList<>(_history.subList(0, _pointer+1));
        }
        
        ++_pointer;
    }
    
    public T undo() { return (T)_history.get(--_pointer); }
    
    public T redo() { return (T)_history.get(_pointer++); }
    
    /**
     * Revert to nth item in history
     * @param i index to revert to
     * @return reverted value
     */
    public T revert(int i) { _pointer = i+1; return (T)_history.get(i); }
    
    /**
     * Gets whether the internal pointer is pointing to the bottom item. The 'undo' function
     * does not perform a bounds check, so 'undo' should only be called if isAtBottom returns false
     * @return true if the internal pointer references the bottom item in list
     */
    public boolean isAtBottom() { return _pointer <= 1; }
    
    /**
     * Gets whether the internal pointer is pointing to the top item. The 'redo' function
     * does not perform a bounds check, so 'redo' should only be called if isAtTop returns false
     * @return true if the internal pointer references the bottom item in list
     */
    public boolean isAtTop() { return _pointer == _history.size(); }
    
    /**
     * Gets the index of the next item in list. This is the index that the next
     * item will go on a call to add.
     * @return the index of the next item to be added
     */
    public int getPointer() { return _pointer; }
    
    public Object[] toArray() { return _history.toArray(); }
    
    public T getCurrent() { return (T)_history.get(_pointer-1); }
    
    @Override
    public String toString()
    {
        String ans = "History[" + _pointer + "]{ ";
        
        if (_pointer == 0)
            return ans + " }";
        
        for (int i = 0; i < _pointer-1; ++i)
            ans += _history.get(i).toString() + ", ";
        
        ans += _history.get(_pointer-1).toString() + " }";
        
        return ans;
    }
}
