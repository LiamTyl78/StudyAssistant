package com.example;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.example.FlashcardMode.FlashcardController;
import com.example.LearnMode.LearnModeMain;

public class DeckPreviewWindow {

    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 500;

    private JDialog frame;
    private JPanel panel;
    private MainMenu mainMenu;
    private JButton flashcardsButton, learnModeButton, resetButton;
    
    /**
     *initialize the components and window elements along with referencing the main menu window
     * 
     * @param filePath
     * @param parent
     * @param mainMenu
     */
    public DeckPreviewWindow(String filePath, JFrame parent, MainMenu mainMenu){
        StudyDeckFile file = new StudyDeckFile(filePath);
        this.mainMenu = mainMenu;
        frame = new JDialog(parent, "", true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
        frame.setResizable(false);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        
        JLabel label = new JLabel("How do you want to study this set?");


        JLabel deckNameLabel = new JLabel(file.toString());
        
        LearnModeMain learnMode = new LearnModeMain(filePath);
        JLabel learnProgressLabel = new JLabel(learnMode.getKnownCards() + " out of " + learnMode.getTotalCards() + " learned");
        flashcardsButton = new JButton("Flashcards");
        learnModeButton = new JButton("Learn");
        resetButton = new JButton("Reset Progress");

        JPanel learnButtonPanel = new JPanel();

        JScrollPane flashcardPreviewPane = populateDeckPreview(file);
        flashcardPreviewPane.getVerticalScrollBar().setUnitIncrement(5);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        
       

        learnModeButton.setSize(150, 40);
        learnModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
                learnMode.startMode();
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
                learnMode.resetProgress();
                learnProgressLabel.setText(learnMode.getKnownCards() + " out of " + learnMode.getTotalCards() + " learned");
            }
        });

        //add components and dividers
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(deckNameLabel);
        panel.add(flashcardPreviewPane);
        
        panel.add(label);
        
        learnButtonPanel.setLayout(new BoxLayout(learnButtonPanel, BoxLayout.X_AXIS));
        learnButtonPanel.add(learnModeButton);
        learnButtonPanel.add(resetButton);
        learnButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(learnButtonPanel);

        panel.add(Box.createVerticalStrut(2));//divider
        flashcardsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(flashcardsButton);
        panel.add(Box.createVerticalStrut(2));

        
        
        panel.add(Box.createVerticalStrut(2));
        learnProgressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(learnProgressLabel);
        frame.add(panel);
        frame.setVisible(true);

    }

    JScrollPane populateDeckPreview(StudyDeckFile file){
        file.open();
        JScrollPane previewPane = new JScrollPane();
        JPanel flashcardList = new JPanel();
        flashcardList.setLayout(new BoxLayout(flashcardList, BoxLayout.Y_AXIS));
        final int DEF_COLUMN_WIDTH = 400;
        final int TERM_COLUMN_WIDTH = 300;
        try {
            String line;
            while ((line = file.readLine()) != null) {
                
                String[] values = line.split(",");
                
                JPanel flashcard = new JPanel(new GridBagLayout());
                
                

                JLabel term = new JLabel(values[1]);
                Dimension term_column = new Dimension(TERM_COLUMN_WIDTH,term.getPreferredSize().height);

                term.setMinimumSize(term_column);
                term.setPreferredSize(term_column);
                term.setHorizontalAlignment(SwingConstants.LEFT);
                GridBagConstraints gbc_term = new GridBagConstraints();
                gbc_term.gridx = 0;
                gbc_term.gridy = 0;
                gbc_term.weightx = 0.0;
                gbc_term.insets = new Insets(5, 5, 5, 5);
                gbc_term.fill = GridBagConstraints.BOTH;
                flashcard.add(term, gbc_term);

                
                JTextArea def = new JTextArea(values[0]);
                
                def.setColumns(DEF_COLUMN_WIDTH / 10);
                def.setMinimumSize(new Dimension(DEF_COLUMN_WIDTH, 1));
                def.setLineWrap(true);
                def.setWrapStyleWord(true);
                def.setEditable(false);
                def.setOpaque(false);
                def.setCaret(new InvisibleCaret());
                def.setHighlighter(null);
                
                GridBagConstraints gbc_def = new GridBagConstraints();
                gbc_def.gridx = 1;
                gbc_def.gridy = 0;
                gbc_def.fill = GridBagConstraints.BOTH;
                gbc_def.weightx = 0.0;
                gbc_def.weighty = 1.0;
                flashcard.add(def, gbc_def);

                flashcardList.add(flashcard);
                flashcardList.add(Box.createVerticalStrut(5));

            }
        }catch (Exception e){
            
        }
        previewPane.setViewportView(flashcardList);
        
        file.close();
        return previewPane;
    }

    private void close(){
        frame.dispose();
        mainMenu.hide();
    }
}
