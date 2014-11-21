/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webbrowser;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Ben_Benign
 */
class BrowserPanel extends JPanel{
     private BrowserToolBar toolBar;
    private WebBrowserPane webDisplayPane;

    public BrowserPanel(BrowserToolBar bar, WebBrowserPane webPane) {
        super(new BorderLayout());

        this.webDisplayPane = webPane;
        this.toolBar = bar;
        this.add(this.toolBar, BorderLayout.NORTH);
        this.add(new JScrollPane(this.webDisplayPane), BorderLayout.CENTER);
    }
    
    public BrowserToolBar getBrowserToolBar(){
        return this.toolBar;
    }
    public WebBrowserPane getWebPane(){
        return this.webDisplayPane;
    }
}
