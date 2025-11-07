package com.example.FlashcardMode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.example.Card;

import com.example.InvisibleCaret;

public class FlashcardView {

    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 500;

    private JButton quitButton, nextButton, prevButton;
    private FlashcardController controller;
    private JTextPane card;
    private Card currentFlashcard;
    JFrame frame;

    public FlashcardView(FlashcardController controller){

        //set up JFrame and Jpanes for components and decalre the controller class
        this.controller = controller;
        this.frame = new JFrame();
        JPanel buttonPanel = new JPanel();
        JPanel rowPanel = new JPanel();
        JPanel flashcardPanel = new JPanel();
       
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        //init and setup of components
        card = new JTextPane();
        card.setEditable(false);
        // card.setPreferredSize(new Dimension(200,200));
        card.setMaximumSize(new Dimension(500,150));
        // card.setMinimumSize(new Dimension(200,200));
        card.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        card.setCaret(new InvisibleCaret());
        card.setHighlighter(null);

        StyledDocument doc = card.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        
        doc.setParagraphAttributes(0, card.getText().length(), center, false);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                controller.handleMouseClicked();
            }
        });

        //setup buttons and thier action listeners
        quitButton = new JButton("Exit");
        quitButton.setMaximumSize(new Dimension(150,25));
        quitButton.addActionListener(e -> this.controller.handleQuitButton());

        nextButton = new JButton("Next");
        nextButton.setMaximumSize(new Dimension(150,25));
        nextButton.addActionListener(e -> this.controller.handleNextButton());

        prevButton = new JButton("Previous");
        prevButton.setMaximumSize(new Dimension(150,25));
        prevButton.addActionListener(e -> this.controller.handlePrevButton());

        //Adding and organizing components and panels with Boarderlayout
        prevButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        nextButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(rowPanel);
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

        rowPanel.add(prevButton);
        rowPanel.add(Box.createHorizontalStrut(10));
        rowPanel.add(nextButton);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(quitButton);
        buttonPanel.add(Box.createVerticalStrut(10));

        flashcardPanel.setLayout(new BoxLayout(flashcardPanel, BoxLayout.Y_AXIS));
        flashcardPanel.add(Box.createVerticalStrut(200));
        flashcardPanel.add(card);

        frame.add(flashcardPanel);

    }

    public void update(Card currentFlashcard, int index) {
        this.currentFlashcard = currentFlashcard;
        frame.setTitle("Flashcard " + (index + 1) + " of " + controller.getDeckSize());
    }

    public void displaySide(boolean termDisplayed){
        if (termDisplayed) {
            card.setText(this.currentFlashcard.getTerm());
        }
        else{
            card.setText(this.currentFlashcard.getDefinition());
        }
    }

    public void dispose(){
        frame.dispose();
    }

    public void show(){
        frame.setVisible(true);
    }
}
