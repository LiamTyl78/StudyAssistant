package com.example;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.*;

public class MainMenu {
    private JScrollPane pane;
    private DefaultListModel<StudyDeckFile> studySets;
    private JFrame frame;
    private JButton studyButton, editButton, helpButton;
    private JMenuBar menuBar;
    private ArrayList<StudyDeckFile> files = new ArrayList<>();


    public MainMenu(){
        System.out.println("Loading Main Menu Interface...");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(500, 400);
        frame.setResizable(false);
        frame.setTitle("Study Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setLocationRelativeTo(null);
        JMenu fileMenu = new JMenu("File");
        JMenuItem importMenuItem = new JMenuItem("Import study set...");
        importMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                StudyDeckFile newFile = importFile();
                if (!(newFile == null)) {
                    files.add(newFile);
                    updateFlashcardList();
                }
            }
        });
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutWindow about = new AboutWindow(frame);
            }
            
        });

        fileMenu.add(importMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        


        files = readFlashcardSets();
        
        studyButton = new JButton("Study");
        editButton = new JButton("Edit");
        helpButton = new JButton("Help");
        studyButton.setBounds(145, 300, 100, 30);
        editButton.setBounds(255,300,100,30);
        helpButton.setBounds(200,335,100,30);
        
        studySets = new DefaultListModel<>();

        updateFlashcardList();

        JList<StudyDeckFile> studySetList = new JList<>(studySets);
        
        pane = new JScrollPane(studySetList);
        pane.setBounds(50, 50, 400, 200);
        pane.setVisible(true);
        // pane.setSize(400, 200);
        studyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (!studySetList.isSelectionEmpty()) {
                    String selectedFile = (studySetList.getSelectedValue().getFullPath());
                    SwingUtilities.invokeLater(() -> new DeckPreviewWindow(selectedFile, frame, MainMenu.this));
                    
                }
            }
        });

        editButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (!studySetList.isSelectionEmpty()) {
                    StudyDeckFile selectedFile = studySetList.getSelectedValue();
                    SwingUtilities.invokeLater(() -> new EditPane(selectedFile.getFullPath(), frame));
                }
            }
        });

        frame.add(pane);
        frame.add(studyButton);
        frame.add(editButton);
        frame.add(menuBar);
        // frame.add(helpButton);
        System.out.println("Done.");

    }

    public void show(){
        frame.setVisible(true);
    }

    public void hide(){
        frame.setVisible(false);
    }

    

    private ArrayList<StudyDeckFile> readFlashcardSets(){
        ArrayList<StudyDeckFile> files = new ArrayList<>();
        String directoryPath = "demo/studysets";
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(path -> path.toString().toLowerCase().endsWith(".csv"))
            .forEach(path -> {
                files.add(new StudyDeckFile(path.toAbsolutePath().toString()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    private StudyDeckFile importFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        while (true) {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION ) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                StudyDeckFile file = new StudyDeckFile(selectedFile.getAbsolutePath());
                if (!selectedFile.getAbsolutePath().toLowerCase().endsWith(".csv")) {
                    error("Invalid file type", "Invalid file type. Please select a CSV file.");
                }
                else if (!files.contains(file)) {
                    return file;
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
    }

    private void updateFlashcardList(){
        studySets.clear();
        for (StudyDeckFile file : files) {
            studySets.addElement(file);
        }
    }


    private void error(String title, String message){
        JOptionPane.showMessageDialog(frame, message , title, JOptionPane.ERROR_MESSAGE);  
    }
}
