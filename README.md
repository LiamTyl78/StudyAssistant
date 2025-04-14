Unlock your full potential with this study assistant application! Reinforce concepts effortlessly with engaging learning modes like Flashcards and Learn Mode. Everything available offline, right on your desktop.
# Application Features
![image](https://github.com/user-attachments/assets/09c57ccf-6f65-4e0f-b892-d2cbf853e50b)
## Flashcard Mode

The Flashcard Mode provides an interactive way to study and reinforce knowledge. Users can cycle through a deck of flashcards using the Previous and Next buttons, allowing them to review each card at their own pace. Each flashcard displays a definition, prompting users to recall and guess the corresponding term before revealing the answer. This mode is perfect for self-paced learning, helping users strengthen their memory and retention of key concepts.

![image](https://github.com/user-attachments/assets/0aec5d55-9456-4c9b-bcf9-4deea07adc85)
## Learn Mode

Learn Mode transforms studying into an interactive challenge by presenting a definition and four randomized term choices, requiring users to select the correct answer. This active recall approach reinforces learning while keeping users engaged. The program tracks progress within each study set, allowing users to resume where they left off or reset their progress for a fresh start. Whether you're preparing for an exam or reinforcing key concepts, Learn Mode provides a structured and effective way to master your material.
# Setup and execution
To run correctly, this project requires Apache Maven to be set up and properly configured.
https://maven.apache.org/download.cgi  
Once Maven is configured properly navigate to the root 'StudyAssist/' directory in a terminal window then run the commands below:  
First clean and install by running:  
```
mvn clean install -f "pom.xml"
```
Then, execute our newly compiled program using:  
```
& mvn exec:java -f "pom.xml"
```
If all goes well, you should see the program pop up and be able to study the included example study set.
