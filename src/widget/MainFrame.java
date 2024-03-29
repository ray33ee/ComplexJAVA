/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import widget.dialogs.CalculatorDialog;
import widget.dialogs.HistoryDialog;
import widget.dialogs.PropertyDialog;
import complex.Complex;
import java.awt.BorderLayout;
import java.io.IOException;

/**
 * Main frame showing ComplexComponent
 * @author Will
 */
public class MainFrame extends javax.swing.JFrame {
    
    /**
     * ComplexComponent widget responsible for drawing complex landscapes an handling
     * mouse events
     */
    private final ComplexComponent _canvas;

    /** Creates new MainFrame form */
    public MainFrame()
    {
        super();
        initComponents();
        
        complex.Landscape land = new complex.Landscape(new complex.evaluator.Evaluator(), new Complex(-1,-1), new Complex(1,1));
        
        //Initialise ComplexComponent with first landscape
        _canvas = new ComplexComponent(land, this);
        
        //Setup physical attributes for ComplexComponent and frame
        _canvas.setLayout(new BorderLayout());
        _canvas.setLocation(10, 6);
        
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setSize(1800, 1000);
        super.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/resources/final.ico")).getImage());
        
        lblZ.setText("");
        lblFz.setText("");
        lblPolar.setText("");
        lblLandscape.setText(_canvas.getHistory().getCurrent().toString());
        
        CanvasPane.add(_canvas);
    }
    
    @Override
    public void paint(java.awt.Graphics g) 
    { 
        if (_canvas != null && CanvasPane != null)
            _canvas.setSize( CanvasPane.getWidth() - 20 , CanvasPane.getHeight() - 10);
        super.paint(g); 
    }
    
    /**
     * This function is called by the ComplexComponent widget in the mouse move event.
     * @param val 
     */
    public void onTrace(Complex val)
    {
        lblZ.setText(val.toString());
        Complex result = _canvas.getLandscape().getEvaluator().f(val);
        lblFz.setText(result.toString());
        lblColor.setBackground(result.color());
        lblPolar.setText(result.toPolarString());
    }
    
    /**
     * Function called by the ComplexComponent anytime the landscape changes 
     * @param land the new landscape
     */
    public void landscapeChange(complex.Landscape land) { lblLandscape.setText(land.toString()); }
    
    /**
     * Exposes the current evaluator in use, for use with the calculator dialog
     * @return the current evaluator
     */
    public complex.evaluator.Evaluator getCurrentEvaluator() { return _canvas.getLandscape().getEvaluator(); }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuPane = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnPan = new javax.swing.JButton();
        btnZoom = new javax.swing.JButton();
        btnNewton = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnRedo = new javax.swing.JButton();
        btnZoomIn = new javax.swing.JButton();
        btnCalculate = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        tglSpeed = new javax.swing.JToggleButton();
        btnCentre = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        CanvasPane = new javax.swing.JPanel();
        StatusPane = new javax.swing.JPanel();
        lblPolar = new javax.swing.JLabel();
        lblFz = new javax.swing.JLabel();
        lblZ = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        lblLandscape = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Complex Grpaher");
        setMinimumSize(new java.awt.Dimension(610, 580));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/new.png"))); // NOI18N
        btnNew.setToolTipText("Create new landscape");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/save.png"))); // NOI18N
        btnSave.setToolTipText("Save landscape as image");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnPan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/pan.png"))); // NOI18N
        btnPan.setToolTipText("Move around landscape");
        btnPan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanActionPerformed(evt);
            }
        });

        btnZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/zoom.png"))); // NOI18N
        btnZoom.setToolTipText("Select zoom");
        btnZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomActionPerformed(evt);
            }
        });

        btnNewton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/newton.png"))); // NOI18N
        btnNewton.setToolTipText("Root finder");
        btnNewton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewtonActionPerformed(evt);
            }
        });

        btnUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/undo.png"))); // NOI18N
        btnUndo.setToolTipText("Undo");
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/redo.png"))); // NOI18N
        btnRedo.setToolTipText("Redo");
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/zoom_in.png"))); // NOI18N
        btnZoomIn.setToolTipText("Zoom in");
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        btnCalculate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/calculator.png"))); // NOI18N
        btnCalculate.setToolTipText("Show calculator");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/zoom_out.png"))); // NOI18N
        btnZoomOut.setToolTipText("Zoom out");
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        tglSpeed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/speed.png"))); // NOI18N
        tglSpeed.setToolTipText("Click to prioritise speed");
        tglSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglSpeedActionPerformed(evt);
            }
        });

        btnCentre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/centre.png"))); // NOI18N
        btnCentre.setToolTipText("Centre on zero");
        btnCentre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCentreActionPerformed(evt);
            }
        });

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toolbar/history.png"))); // NOI18N
        btnHistory.setToolTipText("Show undo/redo history");
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuPaneLayout = new javax.swing.GroupLayout(MenuPane);
        MenuPane.setLayout(MenuPaneLayout);
        MenuPaneLayout.setHorizontalGroup(
            MenuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPaneLayout.createSequentialGroup()
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnPan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnNewton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCentre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tglSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MenuPaneLayout.setVerticalGroup(
            MenuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, MenuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnCentre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(btnZoomIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnZoomOut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(tglSpeed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnHistory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout CanvasPaneLayout = new javax.swing.GroupLayout(CanvasPane);
        CanvasPane.setLayout(CanvasPaneLayout);
        CanvasPaneLayout.setHorizontalGroup(
            CanvasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1414, Short.MAX_VALUE)
        );
        CanvasPaneLayout.setVerticalGroup(
            CanvasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 810, Short.MAX_VALUE)
        );

        lblPolar.setText("jLabel1");
        lblPolar.setToolTipText("Polar coorinates at trace");

        lblFz.setText("jLabel1");
        lblFz.setToolTipText("f(z) at graph trace");

        lblZ.setText("jLabel1");
        lblZ.setToolTipText("Graph trace");

        lblColor.setToolTipText("Colour at graph trace");
        lblColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColor.setOpaque(true);

        lblLandscape.setText("jLabel1");
        lblLandscape.setToolTipText("Current equation, bottom left and top right coordinates");

        javax.swing.GroupLayout StatusPaneLayout = new javax.swing.GroupLayout(StatusPane);
        StatusPane.setLayout(StatusPaneLayout);
        StatusPaneLayout.setHorizontalGroup(
            StatusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StatusPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLandscape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblZ, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFz, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lblPolar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        StatusPaneLayout.setVerticalGroup(
            StatusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StatusPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(StatusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatusPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPolar)
                        .addComponent(lblFz)
                        .addComponent(lblZ)
                        .addComponent(lblLandscape))
                    .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatusPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(MenuPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(CanvasPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CanvasPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusPane, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("Complex Grpaher - Version 1.1.17");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        CalculatorDialog d = new CalculatorDialog(this);
        d.setVisible(true);
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        _canvas.centreZoom(-1.0/3.0);
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        _canvas.redo();
    }//GEN-LAST:event_btnRedoActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        _canvas.undo();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnNewtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewtonActionPerformed
        _canvas.setAction(ComplexComponent.ActionType.NEWTON);
    }//GEN-LAST:event_btnNewtonActionPerformed

    private void btnZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomActionPerformed
        // TODO add your handling code here:
        _canvas.setAction(ComplexComponent.ActionType.ZOOM);
    }//GEN-LAST:event_btnZoomActionPerformed

    private void btnPanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanActionPerformed
        _canvas.setAction(ComplexComponent.ActionType.PAN);
    }//GEN-LAST:event_btnPanActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        PropertyDialog d = new PropertyDialog(this, _canvas.getLandscape());
        
        if (d.getAccepted())
            _canvas.changeLandscape(d.getLandscape());
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        _canvas.centreZoom(1.0);
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnCentreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCentreActionPerformed
        _canvas.zeroCentre();
    }//GEN-LAST:event_btnCentreActionPerformed

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        HistoryDialog history = new HistoryDialog(this, _canvas.getHistory());
        
        if (history.getAccepted())
            _canvas.revert(history.getChosen());
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void tglSpeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglSpeedActionPerformed
        _canvas.prioritiseSpeed(tglSpeed.isSelected());

        if (tglSpeed.isSelected())
            tglSpeed.setToolTipText("Click to prioritise accuracy");
        else
            tglSpeed.setToolTipText("Click to prioritise speed");
    }//GEN-LAST:event_tglSpeedActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CanvasPane;
    private javax.swing.JPanel MenuPane;
    private javax.swing.JPanel StatusPane;
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnCentre;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNewton;
    private javax.swing.JButton btnPan;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUndo;
    private javax.swing.JButton btnZoom;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblFz;
    private javax.swing.JLabel lblLandscape;
    private javax.swing.JLabel lblPolar;
    private javax.swing.JLabel lblZ;
    private javax.swing.JToggleButton tglSpeed;
    // End of variables declaration//GEN-END:variables
}
