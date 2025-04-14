package com.example;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StudyModeSelector {

    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 200;

    private JDialog frame;
    private JPanel panel;
    private MainMenu mainMenu;
    private JButton flashcardsButton, learnModeButton, resetButton;

    
    public StudyModeSelector(String filePath, JFrame parent, MainMenu mainMenu){
        //initialize the components and window elements along with referencing the main menu window
        LearnModel learnDeck = new LearnModel(filePath);
        this.mainMenu = mainMenu;
        frame = new JDialog(parent, "", true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(parent);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("How do you want to study this set?");
        JLabel learnProgressLabel = new JLabel(learnDeck.getKnownCards() + " out of " + learnDeck.getTotalCards() + " learned");
        flashcardsButton = new JButton("Flashcards");
        learnModeButton = new JButton("Learn");
        resetButton = new JButton("Reset Progress");

        learnModeButton.setSize(150, 40);
        learnModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
                learnDeck.startMode();
            }
        });

        flashcardsButton.setSize(150, 40);
        flashcardsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudyMode deck = new FlashcardController(filePath, mainMenu);
                close();
                deck.startMode();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                learnDeck.resetProgress();
                learnProgressLabel.setText(learnDeck.getKnownCards() + " out of " + learnDeck.getTotalCards() + " learned");
            }
        });

        //add components and dividers
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(2));//divider
        flashcardsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(flashcardsButton);
        panel.add(Box.createVerticalStrut(2));

        JPanel learnButtonPanel = new JPanel();
        learnButtonPanel.setLayout(new BoxLayout(learnButtonPanel, BoxLayout.X_AXIS));
        learnButtonPanel.add(learnModeButton);
        learnButtonPanel.add(resetButton);
        learnButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(learnButtonPanel);
        
        panel.add(Box.createVerticalStrut(2));
        learnProgressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(learnProgressLabel);
        frame.add(panel);
        frame.setVisible(true);

    }
    private void close(){
        frame.dispose();
        mainMenu.hide();
    }
}
