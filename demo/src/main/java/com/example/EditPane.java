package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class EditPane {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 500;
    private static final int MAX_IMAGE_SIZE = 200;
    private static final String TERM_PLACEHOLDER = "Term...";
    private static final String DEFINITION_PLACEHOLDER = "Definition...";

    private JDialog frame;
    private JPanel cardSetPanel, buttonPanel;
    private int questionFields;
    private ArrayList<Card> questions = new ArrayList<>();
    private ArrayList<JTextField> defFields = new ArrayList<>();
    private ArrayList<JTextField> termFields = new ArrayList<>();
    private ArrayList<StringBuilder> imageFields = new ArrayList<>();
    private StudyDeckFile selectedFile;

    /**
     * Constructor for the Deck Modify window that allows users to edit their study
     * set
     * 
     * @param selectedFile the file to be edited
     */
    public EditPane(String selectedFilePath, JFrame parent) {
        StudyDeckFile selectedFile = new StudyDeckFile(selectedFilePath);
        selectedFile.open();
        this.selectedFile = selectedFile;
        frame = new JDialog(parent, "Editing " + removeFileExtension(selectedFile.getName()), true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        loadFromFile();
        int questionNum = questions.size();
        cardSetPanel = new JPanel();
        cardSetPanel.setLayout(new BoxLayout(cardSetPanel, BoxLayout.Y_AXIS));

        while (questionFields < questionNum) {
            String term = questions.get(questionFields).getTerm();
            String definition = questions.get(questionFields).getDefinition();
            StringBuilder imgPath = new StringBuilder();
            imgPath.append(questions.get(questionFields).getImageLink());
            addFlashcardFields(term, definition, imgPath);
        }
        cardSetPanel.add(Box.createVerticalGlue());

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add new...");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFlashcardFields(null, null, new StringBuilder());
                frame.revalidate();
                frame.repaint();
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save(selectedFile);
                frame.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // set up the scroll pane layout within the frame to be above the button panel
        JScrollPane scrollPane = new JScrollPane(cardSetPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(parent);
        selectedFile.close();
    }

    private void addFlashcardFields( String definition, String term, StringBuilder imagePath) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        JLabel termLabel = new JLabel("Term:");
        JLabel defLabel = new JLabel("Defintion:");

        JLabel questionLabel = new JLabel("Question " + (questionFields++ + 1));
        JTextField termTextField = new JTextField(20);
        JLabel image = new JLabel();
        JButton selectImageButton = new JButton("Select Image...");

        termTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        termTextField.setText(TERM_PLACEHOLDER);
        if (term != null && !term.equals("na")) {
            termTextField.setText(term);
        }
        termTextField.addFocusListener(new FocusListener() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (termTextField.getText().equals(TERM_PLACEHOLDER)) {
                    termTextField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (termTextField.getText().equals("")) {
                    termTextField.setText(TERM_PLACEHOLDER);
                }
            }
        });

        JTextField defTextField = new JTextField(20);
        defTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        defTextField.setText(DEFINITION_PLACEHOLDER);
        if (definition != null && !definition.equals("na")) {
            defTextField.setText(definition);
        }
        defTextField.addFocusListener(new FocusListener() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (defTextField.getText().equals(DEFINITION_PLACEHOLDER)) {
                    defTextField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (defTextField.getText().equals("")) {
                    defTextField.setText(DEFINITION_PLACEHOLDER);
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                termTextField.setText(TERM_PLACEHOLDER);
                defTextField.setText(DEFINITION_PLACEHOLDER);
                cardSetPanel.remove(termTextField);
                cardSetPanel.remove(questionPanel);
                cardSetPanel.remove(defTextField);
                cardSetPanel.remove(deleteButton);
                cardSetPanel.remove(questionLabel);
                cardSetPanel.remove(defLabel);
                cardSetPanel.remove(termLabel);
                cardSetPanel.remove(image);
                cardSetPanel.remove(selectImageButton);
                frame.revalidate();
                frame.repaint();
            }
        });

        if (imagePath != null) {
            setImage(image, imagePath);
        }

        selectImageButton.addActionListener(e -> selectImage(image, imagePath));


        termFields.add(termTextField);
        defFields.add(defTextField);
        imageFields.add(imagePath);

        cardSetPanel.add(Box.createVerticalStrut(10));
        cardSetPanel.add(questionLabel);
        cardSetPanel.add(image);
        cardSetPanel.add(termLabel);
        cardSetPanel.add(termTextField);
        cardSetPanel.add(defLabel);
        cardSetPanel.add(defTextField);
        cardSetPanel.add(selectImageButton);
        cardSetPanel.add(deleteButton);
    }

    private void selectImage(JLabel image, StringBuilder imagePath){
        boolean usrSelectingImg = true;
                while (usrSelectingImg) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setFileFilter(
                            new javax.swing.filechooser.FileNameExtensionFilter("Image Fields", "jpg", "png"));
                    int result = fileChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        java.io.File selectedFile = fileChooser.getSelectedFile();
                        boolean isJPG = selectedFile.getAbsolutePath().toLowerCase().endsWith(".jpg");
                        boolean isPNG = selectedFile.getAbsolutePath().toLowerCase().endsWith(".png");
                        if (!isJPG && !isPNG) {
                            error("Invalid file type", "Invalid file type, Please select a JPG or PNG file!");
                        } else {
                            imagePath.setLength(0);
                            System.out.println(selectedFile.getPath());
                            imagePath.append(selectedFile.getAbsolutePath());
                            usrSelectingImg = false;
                        }
                    } else {
                        usrSelectingImg = false;
                    }
                }

                setImage(image, imagePath);
    }

    private void loadFromFile() {
        try {
            String line = "";
            selectedFile.readLine();
            while ((line = selectedFile.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String definition = parts[0];
                    String term = parts[1];
                    String image = parts[2];
                    questions.add(new Card(term, definition, image));
                }
            }
        } catch (Exception e) {

        }
    }

    private ArrayList<String[]> getFlashcardValues() {
        ArrayList<String[]> flashcards = new ArrayList<>();
        for (int i = 0; i < termFields.size(); i++) {
            String term = termFields.get(i).getText();
            String definition = defFields.get(i).getText();
            String image = imageFields.get(i).toString();
            if (image.equals("")) {
                image = "na";
            }
            if (term.equals(TERM_PLACEHOLDER)) {
                term = "na";
            }
            if (definition.equals(DEFINITION_PLACEHOLDER)) {
                definition = "na";
            }
            if (!term.equals("na")) {
                flashcards.add(new String[] { definition, term, image });
            }
        }
        return flashcards;
    }

    private void save(StudyDeckFile file) {
        file.save(getFlashcardValues());
    }

    private String removeFileExtension(String filename) {
        int dotindx = filename.indexOf(".");
        if (dotindx == -1) {
            return filename;
        }
        return filename.substring(0, dotindx);
    }

    private void setImage(JLabel image, StringBuilder imagePath) {
        // System.out.println("opening: " + imagePath.toString());
        ImageIcon originalIcon = new ImageIcon(imagePath.toString());
        // System.out.println("Width: " + originalIcon.getIconWidth() + " Height: " +
        // originalIcon.getIconHeight());
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        double widthRatio = (double) MAX_IMAGE_SIZE / originalWidth;
        double heightRatio = (double) MAX_IMAGE_SIZE / originalHeight;
        double scale = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        image.setIcon(scaledIcon);
        image.setSize(originalWidth, originalHeight);
        image.setVisible(true);
    }

    private void error(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
