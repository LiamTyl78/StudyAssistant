package com.example.FlashcardMode;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.Card;
import com.example.StudyMode;

import java.io.File;


public class FlashcardModel extends StudyMode{
    private ArrayList<Card> flashcards = new ArrayList<>();
    private int currentIndex = 0;
    private boolean termDisplayed = true, termDisplayedFirst;
    
    public FlashcardModel(String filePath, boolean termDisplayedFirst){
        loadData(filePath);
        super.shuffle(flashcards);
        this.termDisplayedFirst = termDisplayedFirst;
    }

    private void loadData(String filepath) {
        try {
            Scanner sc = new Scanner(new File(filepath));

            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String definition = parts[0];
                    String term = parts[1];
                    String image = parts[2];
                    flashcards.add(new Card(definition, term, image));
                }
            }
            sc.close();
        } catch (Exception e) {

        }
    }

    public Card loadNextFlashcard(){
        if (currentIndex < flashcards.size() - 1) {
            currentIndex++;
        }
        return getCurrentFlashcard();
    }

    public Card loadPreviousFlashcard(){
        if (currentIndex > 0) {
            currentIndex--;
        }
        return getCurrentFlashcard();
    }

    public Card getCurrentFlashcard(){
        return flashcards.get(currentIndex);
    }

    public boolean flipFlashcard(){
        termDisplayed = !termDisplayed;
        return termDisplayed;
    }

    public boolean getFirstDisplayed(){
        return termDisplayedFirst;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }   

    public int getFlashcardsSize(){
        return flashcards.size();
    }

    

    
}
