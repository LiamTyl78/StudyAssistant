package com.example.LearnMode;

import java.util.ArrayList;

import com.example.App;
import com.example.MainMenu;

public class LearnController implements LearnModeUserEvents, LearnModelEvents{
    private LearnModel model;
    private LearnView view;
    MainMenu start;

    public LearnController(){
        this.start = App.start;
    }

    public int start(){
        if (model.getUnknownTermsLeft() == 0) {
            start.show();
            view.displayMessage("Hi there! Looks like you've already learned all the terms in this set.\nIf you would like to start over click on the reset progress button\nbefore launching learn mode again.");
            view.dispose();
            return 1;
        }
        model.init();
        return 0;
    }

    public int getKnownCards(){
        return model.getKnownCards();
    }

    public int getTotalCards(){
        return model.getTotalCards();
    }

    public void setView(LearnView view){
        this.view = view;
    }

    public void setModel(LearnModel model){
        this.model = model;
    }

    @Override
    public void checkAnswer(int buttonNum) {
        model.checkAnswer(buttonNum);
        
    }

    @Override
    public void saveProgress() {
        model.saveProgress();
    }

    public void resetProgress(){
        model.resetProgress();
    }

    @Override
    public void displayMessage(String string) {
        view.displayMessage(string);
    }

    @Override
    public void disposeView() {
        view.dispose();
    }

    @Override
    public void hideView() {
        view.hide();
    }

    @Override
    public void displayIcorrectMessage(String string) {
        view.incorrect(string);
    }

    @Override
    public void showView() {
        view.show();
    }

    @Override
    public void setViewButtons(ArrayList<String> selectedAnswers, int buttonsShown) {
        view.setButtons(selectedAnswers, buttonsShown);
    }

    @Override
    public void setViewTitle(int i, int currentRoundTerms) {
        view.setTitle(i, currentRoundTerms);
    }

    @Override
    public void updateView(String currentImage, String currentDefinition) {
        view.update(currentImage, currentDefinition);
    }

    @Override
    public void showStart(){
        start.show();
    }

}
