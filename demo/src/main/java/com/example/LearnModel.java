package com.example;
import java.io.File;
import java.util.*;


import com.fasterxml.jackson.databind.ObjectMapper;

public class LearnModel extends StudyMode{
    private ArrayList<Card> unsortedTerms = new ArrayList<>();
    private ArrayList<Card> knownTerms = new ArrayList<>(), unknownTerms = new ArrayList<>();
    private ArrayList<String> selectedAnswers = new ArrayList<>(), answers = new ArrayList<>();
    private int termIndex, correct = 0, ansPos, currentRoundTerms, currentTermIndex = 1;
    private LearnView learnView;
    private MainMenu start;
    private RandomInteger random = new RandomInteger(0, 3);
    private String filename;

    public LearnModel(String csvPath){

        String[] filepathSplit = csvPath.split("\\\\");
        filepathSplit = filepathSplit[filepathSplit.length-1].split("\\.");
        filename = filepathSplit[0];
        File file = new File("demo/saves/" + filename + ".json");

        if (file.exists() && file.isFile()) {
            loadJson(file.getPath());
        }
        else{
            loadQuestions(csvPath);
        }
        loadAnswers();
        this.start = App.start;
        currentRoundTerms = unsortedTerms.size();
        learnView = new LearnView(this);
    }

    public void loadAnswers(){
        unsortedTerms.forEach(card -> answers.add(card.getTerm()));
        knownTerms.forEach(card -> answers.add(card.getTerm()));
        unknownTerms.forEach(card -> answers.add(card.getTerm()));
    }
    
    @Override
    public void startMode(){
        
        if (unsortedTerms.size() == 0) {
            start.show();
            learnView.displayMessage("Hi there! Looks like you've already learned all the terms in this set.\nIf you would like to start over click on the reset progress button\nbefore launching learn mode again.");
            learnView.dispose();
            return;
        }

        random.SetMax(unsortedTerms.size() - 1);
        termIndex = random.Generate();

        random.SetMax(answers.size() - 1);
        String currentImage = unsortedTerms.get(termIndex).getImageLink();
        String currentTerm = unsortedTerms.get(termIndex).getTerm();
        String currentDefinition = unsortedTerms.get(termIndex).getDefinition();

        learnView.update(currentImage, currentDefinition);
        learnView.setTitle(currentTermIndex++, currentRoundTerms);
        int index = random.Generate();
        for (int i = 0; i < 3 && i < answers.size() - 1; i++) {
            boolean containsCurrent = true;
            boolean equalsAnswer = true;
            // make sure the answer is not the same as the current question and is not already in the list
            while ( containsCurrent || equalsAnswer) { 
                index = random.Generate();
                containsCurrent = selectedAnswers.contains(answers.get(index));
                equalsAnswer = currentTerm.equals(answers.get(index));
            }
            String correctAnswer = answers.get(index);
            selectedAnswers.add(correctAnswer);
        }
        random.SetMax(selectedAnswers.size());
        ansPos = random.Generate();
        selectedAnswers.add(ansPos, currentTerm);
        int buttonsShown = selectedAnswers.size();
        learnView.setButtons(selectedAnswers, buttonsShown);
        learnView.show();
    }

    /**
     * Checks if the answer is correct
     * @param ans the answer the user selected
     */
    public void checkAnswer(int ans) {
        String correctAnswer = unsortedTerms.get(termIndex).getTerm();
        Card currentCard = unsortedTerms.get(termIndex);

        currentCard.setSorted(true);
        if (ans == (ansPos + 1)) {
            learnView.displayMessage("Correct!");
            currentCard.setKnown(true);
            correct++;
            knownTerms.add(unsortedTerms.get(termIndex));
            unsortedTerms.remove(termIndex);
        } else {
            learnView.incorrect("Incorrect, the correct answer was \"" + correctAnswer + "\"");
            currentCard.setKnown(false);
            unknownTerms.add(unsortedTerms.get(termIndex));
            unsortedTerms.remove(termIndex);
        }

        selectedAnswers.clear();

        if (unsortedTerms.size() == 0) {
            float percent = ((float)correct / currentRoundTerms) * 100;
            percent = (float) (Math.round(percent * 10) / 10.0);
            if (percent == 100) {
                start.show();
                learnView.hide();
                learnView.displayMessage("You've learned all the terms in this set. Nice Job!");
                learnView.dispose();
                saveProgress();
                return;
  
            } else {
                learnView.displayMessage("You got " + correct + " answers correct out of " + currentRoundTerms +  " this round. Lets keep on practicing the terms you missed.");
                currentTermIndex = 1;
                correct = 0;
            }

            for (Card card : unknownTerms) {
                unsortedTerms.add(card);
                card.setSorted(false);
            }
            currentRoundTerms = unsortedTerms.size(); 
            unknownTerms.clear();
        }
        
        startMode();
    }
    
    /**
     * Shuffles the questions
     */
    

    /**
     * Loads the questions from a file
     * @param filepath
     */
    private void loadQuestions(String filepath) {
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
                    unsortedTerms.add(new Card(definition, term, image));
                }
            }
            sc.close();
        } catch (Exception e) {

        }
        
        
    }

    public void saveProgress(){
        ArrayList<Card> saveCards = new ArrayList<>();

        saveCards.addAll(unknownTerms);
        saveCards.addAll(unsortedTerms);
        saveCards.addAll(knownTerms);

        ObjectMapper objectMapper = new ObjectMapper();
        int totalCards = unknownTerms.size() + knownTerms.size() + unsortedTerms.size();
        try {
            objectMapper.writeValue(new File("demo/saves/" + filename + ".json"), new SaveData(saveCards, knownTerms.size(), totalCards));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadJson(String filepath){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SaveData save = objectMapper.readValue(new File(filepath), SaveData.class);
            for (Card card : save.getCards()) {
                if (card.isKnown() && card.isSorted()) {
                    knownTerms.add(card);
                }
                else if(!card.isKnown() && card.isSorted()){
                    unknownTerms.add(card);
                }
                else{
                    unsortedTerms.add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetProgress(){
        for (Card card : knownTerms) {
            card.setKnown(false);
            card.setSorted(false);
        }
        for (Card card : unknownTerms) {
            card.setKnown(false);
            card.setSorted(false);
        }
        unsortedTerms.addAll(knownTerms);
        unsortedTerms.addAll(unknownTerms);
        unknownTerms.clear();
        knownTerms.clear();
        currentRoundTerms = unsortedTerms.size();
        saveProgress();
    }

    public int getTotalCards(){
        int totalCards = unknownTerms.size() + knownTerms.size() + unsortedTerms.size();
        return totalCards;
    }

    public int getKnownCards(){
        return knownTerms.size();
    }
}
