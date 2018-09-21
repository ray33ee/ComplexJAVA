/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget.dialogs;

/**
 * Class representing a modal dialog. Complete with accept and reject functions called
 * by OK or Cancel buttons. 
 * @author Will
 */
public abstract class ModalDialog extends javax.swing.JDialog 
{
    /** Set to true if accept() if dialog is closed via accept() */
    private boolean _accepted;
    
    /**
     * Constructs an empty modal dialog.
     * @param parent the parent of the dialog
     * @param title the title of the dialog
     */
    public ModalDialog(widget.MainFrame parent, String title)
    {
        super(parent, title, true);
        _accepted = false;
        
        super.setResizable(false);
        super.setLocation(parent.getX() + parent.getWidth() / 2 - super.getWidth() / 2, parent.getY() + parent.getHeight()/ 2 - super.getHeight()/ 2);
    }
    
    /** Call this if the dialog is 'accepted' (via OK, YES etc. buttons) */
    public void accept() { _accepted = true; super.dispose(); }
    
    /** Call this if the dialog is 'rejected' (via CANCEL, NO etc. buttons) */
    public void reject() { _accepted = false; super.dispose(); }
    
    /** Returns true if the dialog is closed via accept button (OK, YES, etc.) */
    public boolean getAccepted() { return _accepted; }
}
