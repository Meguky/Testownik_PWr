package testownik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.util.*;


public class TestownikTestController {

    private int testInitialCount, testRetryCount, questionsLeft=0, currentQuestion;
    private String testBaseChoice;
    private ArrayList<TestQuestion> testQuestions = new ArrayList<>();
    private ObservableList<ToggleButton> answerButtons = FXCollections.observableArrayList();

    @FXML
    private Button startTestButton;
    @FXML
    private BorderPane questionPane;
    @FXML
    private ListView answersList;
    @FXML
    private Button checkAnswer;
    @FXML
    private Button nextQuestion;
    @FXML
    private Label questionsLearned;
    @FXML
    private Label questionsAll;
    void initData(int pInitial, int pRetry, String pBase){
        testInitialCount = pInitial;
        testRetryCount = pRetry;
        testBaseChoice = pBase;
    }

    public void initialize(){
        nextQuestion.setDisable(true);
    }

    @FXML
    public void checkAnswer(){
        int goodAnswersSelected = 0;
        int goodAnswers = 0;
        nextQuestion.setDisable(false);
        checkAnswer.setDisable(true);
        System.out.println(testQuestions.get(currentQuestion).getInitialCount());
        for(ToggleButton b : answerButtons){
            if(b.isSelected()){
                if(b.getId() == "true"){
                    b.setStyle("-fx-background-color: green");
                    goodAnswers++;
                    goodAnswersSelected++;
                }else{
                    b.setStyle("-fx-background-color: red");
                }
            }else{
                if(b.getId() == "true"){
                    b.setStyle("-fx-background-color: greenyellow"); //TODO
                    goodAnswers++;
                }
            }
        }
        if(goodAnswersSelected == goodAnswers){
            testQuestions.get(currentQuestion).goodAnswer();
        }else{
            testQuestions.get(currentQuestion).badAnswer();
        }
        System.out.println(testQuestions.get(currentQuestion).getInitialCount());
        if(testQuestions.get(currentQuestion).getInitialCount() == 0){
            testQuestions.remove(testQuestions.get(currentQuestion));
            questionsLeft++;
        }
    }

    @FXML
    public void nextQuestion(){
        nextQuestion.setDisable(true);
        checkAnswer.setDisable(false);
        questionsLearned.setText(Integer.toString(questionsLeft));
        Random rn = new Random();
        currentQuestion = rn.nextInt(testQuestions.size());

        setQuestion(testQuestions.get(currentQuestion).getQuestion());
        answerButtons = setAnswers(testQuestions.get(currentQuestion).getAnswers());
        nextQuestion.setDisable(true);
        checkAnswer.setDisable(false);
    }

    @FXML
    public void startTest(){
        startTestButton.setVisible(false);
        testQuestions = legacyDatabaseLoader(testBaseChoice);
        questionsAll.setText(Integer.toString(testQuestions.size()));
        questionsLearned.setText("0");
        Random rn = new Random();
        currentQuestion = rn.nextInt(testQuestions.size());
        setQuestion(testQuestions.get(currentQuestion).getQuestion());
        answerButtons = setAnswers(testQuestions.get(currentQuestion).getAnswers());

    }

    private ArrayList<TestQuestion> legacyDatabaseLoader(String pPathToDatabase){
        ArrayList<TestQuestion> pTestQuestions = new ArrayList<>();

        int i = 0;
        do{
            i++;
            if(i < 10) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + "00" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 10 && i < 100){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + "0" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 100){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addQuestion(reader));
                } catch (IOException e) { break; }
            }
        }while(true);
        return pTestQuestions;
    }

    private TestQuestion addQuestion(BufferedReader reader) throws IOException{

        String readQuestion, goodAnswerString, line;
        ArrayList<TestAnswer> readAnswers = new ArrayList<>();
        goodAnswerString = reader.readLine();
        readQuestion = reader.readLine();
        int i = 1;
        while((line = reader.readLine()) != null){
            if(goodAnswerString.charAt(i) == '1'){
                readAnswers.add(new TestAnswer(line, true));
            }else{
                readAnswers.add(new TestAnswer(line, false));
            }
            i++;
        }
        return new TestQuestion(readQuestion, readAnswers, testInitialCount, testRetryCount);
    }

    private void setQuestion(String question){
        if(!question.startsWith("[img]")){
            Label questionLabel = new Label();
            questionLabel.setText(question);
            questionLabel.setPrefWidth(1200);
            questionLabel.setPrefHeight(190);
            questionLabel.setAlignment(Pos.CENTER);
            questionLabel.setStyle("-fx-background-color: darkgray; -fx-alignment: center;");
            questionPane.setCenter(questionLabel);
        }else{

        }
    }

    private ObservableList<ToggleButton> setAnswers(ArrayList<TestAnswer> pTestAnswers){
        ObservableList<ToggleButton> answersButtons = FXCollections.observableArrayList();
        for(TestAnswer q : pTestAnswers) {
            if (!q.getAnswerText().startsWith("[img]")) {
                ToggleButton btn = new ToggleButton();
                btn.setAlignment(Pos.CENTER);
                btn.setPrefWidth(720);
                btn.setText(q.getAnswerText());
                btn.setId(Boolean.toString(q.isCorrect()));
                answersButtons.add(btn);
            } else {

            }
        }
        answersList.setItems(answersButtons);
        return answersButtons;
    }
}

class TestQuestion{

    private String question;
    private ArrayList<TestAnswer> answers;
    private int retryCount, retryCountWhenErrorOcurred;
    TestQuestion(String pQ, ArrayList<TestAnswer> pA, int pIC, int pRC){
        question = pQ;
        answers = pA;
        retryCount = pIC;
        retryCountWhenErrorOcurred = pRC;
    }

    @Override
    public String toString() {
        String testQuestion = "TestQuestion{" +
                "question='" + question + '\'' + "answers{";
        for( TestAnswer a : answers){
            testQuestion += " " + a.getAnswerText() + "--->" + a.isCorrect() + " -|||- ";
        }
        testQuestion += "}";
        return testQuestion;
    }

    String getQuestion(){
        return question;
    }

    ArrayList<TestAnswer> getAnswers(){
        return answers;
    }

    int getInitialCount(){
        return retryCount;
    }

    int getRetryCount(){
        return retryCountWhenErrorOcurred;
    }

    void badAnswer(){
        retryCount += retryCountWhenErrorOcurred;
    }

    void goodAnswer(){
        retryCount--;
    }
}

class TestAnswer{
    private String answerText;
    private Boolean isCorrect;

    TestAnswer(String pT, Boolean pIsCorrect){
        answerText = pT;
        isCorrect =  pIsCorrect;
    }

    String getAnswerText(){
        return answerText;
    }

    Boolean isCorrect(){
        return isCorrect;
    }
}