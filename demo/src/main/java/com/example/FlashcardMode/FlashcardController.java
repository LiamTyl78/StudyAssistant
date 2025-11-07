package com.example.FlashcardMode;

import com.example.MainMenu;
import com.example.StudyMode;

public class FlashcardController extends StudyMode{
    private FlashcardView view;
    private FlashcardModel model;
    private MainMenu mainMenu;

    public FlashcardController(String filePath, MainMenu mainMenu){
        this.model = new FlashcardModel(filePath, false);
        this.view = new FlashcardView(this);
        this.mainMenu = mainMenu;
        
    }

    public void handleQuitButton(){
        view.dispose();
        this.mainMenu.show();
    }

    public void handleNextButton(){
        view.update(model.loadNextFlashcard(), model.getCurrentIndex());
        view.displaySide(false);
    }

    public void handlePrevButton(){
        view.update(model.loadPreviousFlashcard(), model.getCurrentIndex());
        view.displaySide(false);
    }

    public void handleMouseClicked(){
        view.displaySide(model.flipFlashcard());
    }

    public int getDeckSize(){
        return model.getFlashcardsSize();
    }

    @Override
    public int startMode() {
        view.update(model.getCurrentFlashcard(), model.getCurrentIndex());
        view.displaySide(false);
        view.show();
        return 0;
    }
}
