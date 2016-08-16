package com.jamfsoftware.jss.healthcheck.ui.component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.*;

/**
 * @author ludovicianul, Modified by Jacob Schultz This class handles creating a clickable JLabel. If the param text
 *         conatians a URL, open in the browser, otherwise display a help message on click.
 */

public class URLLabel extends JLabel {
	
	private final String url;
	
	public URLLabel(String label, String url) {
		super(label);
		this.url = url;
		setForeground(Color.BLUE.darker());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new URLOpenAdapter());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.blue);
	}
	
	private class URLOpenAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (Desktop.isDesktopSupported()) {
				try {
					if (url.contains("http")) {
						Desktop.getDesktop().browse(new URI(url));
					} else {
						JOptionPane.showMessageDialog(null, url);
					}
				} catch (Exception i) {
					i.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "The utility will not be able to open links on your system.\nThis most often happens inside of VMs.\nPlease consider running on a different system, or copying the report for viewing.", "Java Desktop Unsupported", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}