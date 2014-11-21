/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webbrowser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Ben_Benign
 */
class WebBrowserPane extends JEditorPane{
    private String title;
    private JTextArea statusArea;
    private ArrayList<String> webHistoryList;
    private int currentAddress;
    
    private URLConnection connection;

    public WebBrowserPane(String title, JTextArea status) {

        this.title = title;
        this.statusArea = status;
        this.setContentType("text/html");
        this.setEditable(false);
        this.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                webDisplayPaneHyperlinkUpdated(e);
            }
        });

        this.webHistoryList = new ArrayList<String>();
        this.currentAddress = -1;
    }

    public void goToWeb(String address) {
        Thread thr = new Thread(new Runnable() {

            @Override
            public void run() {
                getStatusArea().append("THREAD " + title + " : start.\n");
                getStatusArea().append("THREAD " + title + " : get page URL = " + address + "\n");
                displayWebPage(address);
                getStatusArea().append("THREAD " + title + " : finished.\n");
                getStatusArea().append("THREAD " + title + " : get response header ...\n");
                URLConnection conn = getConnection();
                Map<String, List<String>> map = conn.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    getStatusArea().append("\tKey : " + entry.getKey()
                            + " ,Value : " + entry.getValue() + "\n");
                }
            }
        });
        thr.start();

        // add web address to webHistory
        if (this.currentAddress == this.webHistoryList.size() - 1) {
            this.currentAddress++;
            this.webHistoryList.add(address);
        } else {
            // remove addresses index: currentAddress -> N
            while (this.currentAddress < this.webHistoryList.size() - 1) {
                this.webHistoryList.remove(this.currentAddress + 1);
            }
            // add web address
            this.currentAddress++;
            this.webHistoryList.add(address);
        }
    }

    public String back() {
        if (this.currentAddress == -1) {
            return null;
        }

        if (this.currentAddress == 0) {
            return "BLANK_PAGE";
        } else {
            Thread thr = new Thread(new Runnable() {

                @Override
                public void run() {
                    getStatusArea().append("THREAD " + title + " : start.\n");
                    getStatusArea().append("THREAD " + title + " : back page URL = " + getWebHistory().get(getCurrentAddress() - 1) + "\n");
                    displayWebPage(getWebHistory().get(getCurrentAddress() - 1));
                    getStatusArea().append("THREAD " + title + " : finished.\n");
                    URLConnection conn = getConnection();
                    Map<String, List<String>> map = conn.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        getStatusArea().append("\tKey : " + entry.getKey()
                                + " ,Value : " + entry.getValue() + "\n");
                    }
                }
            });
            thr.start();

            // set current adress back 1 addr.
            this.currentAddress--;

            return this.webHistoryList.get(this.currentAddress);
        }
    }

    public String forward() {
        if (this.currentAddress == this.webHistoryList.size() - 1) {
            return null;
        }

        Thread thr = new Thread(new Runnable() {

            @Override
            public void run() {
                getStatusArea().append("THREAD " + title + " : start.\n");
                getStatusArea().append("THREAD " + title + " : forward page URL = " + getWebHistory().get(getCurrentAddress() + 1) + "\n");
                displayWebPage(getWebHistory().get(getCurrentAddress() + 1));
                getStatusArea().append("THREAD " + title + " : finished.\n");
                URLConnection conn = getConnection();
                Map<String, List<String>> map = conn.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    getStatusArea().append("\tKey : " + entry.getKey()
                            + " ,Value : " + entry.getValue() + "\n");
                }
            }
        });
        thr.start();
        // set current adress forward 1 addr.
        this.currentAddress++;

        return this.webHistoryList.get(this.currentAddress);
    }

    public void refresh() {
        Thread thr = new Thread(new Runnable() {

            @Override
            public void run() {
                getStatusArea().append("THREAD " + title + " : start.\n");
                getStatusArea().append("THREAD " + title + " : refresh page URL = " + getWebHistory().get(getCurrentAddress()) + "\n");
                displayWebPage(getWebHistory().get(getCurrentAddress()));
                getStatusArea().append("THREAD " + title + " : finished.\n");
                URLConnection conn = getConnection();
                Map<String, List<String>> map = conn.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    getStatusArea().append("\tKey : " + entry.getKey()
                            + " ,Value : " + entry.getValue() + "\n");
                }
            }
        });
        thr.start();
    }

    private void displayWebPage(String address) {
        
        try {
            URL url = new URL(address);
            this.connection = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection)this.connection;
            conn.getResponseCode();
            
            this.setPage(address);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WebBrowserPane.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebBrowserPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void webDisplayPaneHyperlinkUpdated(HyperlinkEvent e) {
        HyperlinkEvent.EventType eventType = e.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent
                        = (HTMLFrameHyperlinkEvent) e;
                HTMLDocument document
                        = (HTMLDocument) this.getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
                goToWeb(e.getURL().toString());
            }
        }
    }

    private URLConnection getConnection() {a
        return this.connection;
    }

    private JTextArea getStatusArea() {
        return this.statusArea;
    }

    public ArrayList<String> getWebHistory() {
        return this.webHistoryList;
    }

    public int getCurrentAddress() {
        return this.currentAddress;
    }
}
