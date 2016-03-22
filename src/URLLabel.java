import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.*;
import java.io.*;

/**
 * @author ludovicianul, Modified by Jacob Schultz
 * This class handles creating a clickable JLabel.
 * If the param text conatians a URL, open in the browser, otherwise
 * display a help message on click.
 */

public class URLLabel extends JLabel {

    private String url;

    public URLLabel() {
        this("","");
    }

    public URLLabel(String label, String url) {
        super(label);
        this.url = url;
        setForeground(Color.BLUE.darker());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new URLOpenAdapter());
    }

    public void setURL(String url) {
        this.url = url;
    }

    //this is used to underline the text
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Insets insets = getInsets();
        g.setColor(Color.blue);
        int left = insets.left;
        if (getIcon() != null) {
            left += getIcon().getIconWidth() + getIconTextGap();
        }

    }

    private class URLOpenAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (Desktop.isDesktopSupported()) {
                try {
                    if (url.contains("http")){
                        Desktop.getDesktop().browse(new URI(url));
                    } else {
                        JOptionPane.showMessageDialog(null,url);
                    }
                } catch (Exception i) {
                    i.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(),"The utility will not be able to open links on your system.\nThis most often happens inside of VMs.\nPlease consider running on a different system, or copying the report for viewing.", "Java Desktop Unsupported", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void openWebpage(String url) {
        try {
            new ProcessBuilder("x-www-browser", url).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}