package com.example;

import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AboutWindow {
    private JDialog frame;
    private final int WINDOW_HEIGHT = 300, WINDOW_WIDTH = 300;

    public AboutWindow(JFrame parent){

        frame = new JDialog(parent,"About",true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setType(Type.NORMAL);
        frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        frame.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);

        JTextPane aboutText = new JTextPane();
        aboutText.setText("StudyAssistant v0.1\n\n11/9/2025\nMaintained by Liam Tyler");
        aboutText.setLocation(0,WINDOW_HEIGHT/2);
        aboutText.setEditable(false);
        aboutText.setCaret(new InvisibleCaret());
        aboutText.setHighlighter(null);

        //align text to center
        StyledDocument doc = aboutText.getStyledDocument();
        SimpleAttributeSet centerAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAlign, false);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
            }
        });

        
        panel.add(aboutText);
        panel.add(closeButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
