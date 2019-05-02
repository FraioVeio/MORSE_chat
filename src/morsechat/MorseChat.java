package morsechat;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class MorseChat extends javax.swing.JFrame {
    
    public MorseChat() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        text1 = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        statusField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        speedField = new javax.swing.JTextField();
        pb = new javax.swing.JProgressBar();
        receiveButton = new javax.swing.JToggleButton();
        thresholdSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MORSE chat");
        setLocationByPlatform(true);
        setResizable(false);

        text1.setColumns(20);
        text1.setLineWrap(true);
        text1.setRows(5);
        jScrollPane1.setViewportView(text1);

        sendButton.setText("Invia");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        statusField.setEditable(false);

        jLabel1.setText("Velocità (ms):");

        speedField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        speedField.setText("60");

        receiveButton.setText("Ricevi");
        receiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiveButtonActionPerformed(evt);
            }
        });

        thresholdSlider.setMaximum(127);
        thresholdSlider.setValue(25);

        jLabel2.setText("Threshold:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sendButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receiveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(thresholdSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(pb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusField)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sendButton)
                        .addComponent(jLabel1)
                        .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(receiveButton)
                        .addComponent(jLabel2))
                    .addComponent(thresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        sendButton.setEnabled(false);
        receiveButton.setEnabled(false);
        pb.setIndeterminate(true);
        statusField.setText("");
        
        MorseThread mt = new MorseThread(this, MorseTranslator.asciiToMorse(text1.getText()), Float.parseFloat(speedField.getText()));
        mt.start();
    }//GEN-LAST:event_sendButtonActionPerformed
    
    private MorseThread mtasc;
    
    private void receiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiveButtonActionPerformed
        if(receiveButton.isSelected()) {
            // Ascolta
            
            sendButton.setEnabled(false);
            statusField.setText("");
            
            mtasc = new MorseThread(this, false, Float.parseFloat(speedField.getText()));
            mtasc.start();
            
        } else {
            // Smetti di ascoltare
            
        }
    }//GEN-LAST:event_receiveButtonActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}
        
        EventQueue.invokeLater(() -> {
            new MorseChat().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JProgressBar pb;
    public javax.swing.JToggleButton receiveButton;
    public javax.swing.JButton sendButton;
    private javax.swing.JTextField speedField;
    public javax.swing.JTextField statusField;
    public javax.swing.JTextArea text1;
    public javax.swing.JSlider thresholdSlider;
    // End of variables declaration//GEN-END:variables
}
