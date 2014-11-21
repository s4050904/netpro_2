/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webbrowser;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author Ben_Benign
 */
class BrowserToolBar extends JToolBar {
    private JButton backButton;
    private JButton forwardButton;
    private JButton refreshButton;
    private JButton goButton;
    private JTextField addressTxtField;
    
    private WebBrowserPane displayPane;
    
    public BrowserToolBar(WebBrowserPane display){
        super("Browser Tool Bar");
        
        this.displayPane = display;
        initializeBackButton();
        initializeforwardButton();
        initializeRefreshButton();
        initializeGoButton();
        initializeAddressTxtField();
        
        this.add(this.backButton);
        this.add(this.forwardButton);
        this.add(this.refreshButton);
        this.add(this.addressTxtField);
        this.add(this.goButton);
   }  
  private void initializeBackButton() {
        this.backButton = new JButton();
        this.backButton.setText("Back");
        this.backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonActionPerformed(e);
            }

        });
    }

    private void backButtonActionPerformed(ActionEvent evt) {
        
        String backAddress = this.displayPane.back();
        
        if (backAddress == null) {
            return;
        }

        if (backAddress.equals("BLANK_PAGE")) {
            this.addressTxtField.setText("");

            //virtually clear JEditorPane
            this.displayPane.setText("");
            this.displayPane.setBackground(Color.white);
        } else {
            this.addressTxtField.setText(backAddress);
        }
    }
    
    private void initializeforwardButton() {
        this.forwardButton = new JButton();
        this.forwardButton.setText("Forward");
        this.forwardButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                forwardButtonActionPerformed(e);
            }
        
        });
    }
    
    private void forwardButtonActionPerformed(ActionEvent evt){
        String forwardAddress = this.displayPane.forward();
        
        if(forwardAddress == null)
            return;
        
        this.addressTxtField.setText(forwardAddress);
    }
    
    private void initializeRefreshButton() {
        this.refreshButton = new JButton();
        this.refreshButton.setText("Refresh");
        this.refreshButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                refreshButtonActionPerformed(e);
            }
        
        });
    }
    
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        this.displayPane.refresh();
    }
    
    private void initializeGoButton() {
        this.goButton = new JButton();
        this.goButton.setText("Go!");
        this.goButton.addActionListener(new ActionListener(){
            
         
            @Override
            public void actionPerformed(ActionEvent e) {
                goButtonActionPerformed(e);
            }
        
        });
    }
    
    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        
        
        if (this.addressTxtField.getText().isEmpty()) {
            return;
        }
        if(!(this.addressTxtField.getText().startsWith("https://")))
        {   
            String urlTxt = this.addressTxtField.getText();
            urlTxt = "https://"+this.addressTxtField.getText();
            this.displayPane.goToWeb(urlTxt);
        }
        else
        {
            this.displayPane.goToWeb(this.addressTxtField.getText());
        }
        
    } 

    private void initializeAddressTxtField() {
        this.addressTxtField = new JTextField();
        this.addressTxtField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                addressTextFieldKeyReleased(evt);
            }
        });       
    }
    
    private void addressTextFieldKeyReleased(java.awt.event.KeyEvent evt) {                                             
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.addressTxtField.getText().isEmpty()) {
                return;
            }
            this.displayPane.goToWeb(this.addressTxtField.getText());
        }
        
    }  

    public JTextField getAddressTxtField(){
        return this.addressTxtField;
    }
}
