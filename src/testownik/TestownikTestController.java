package testownik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.util.*;


public class TestownikTestController {

    private int testInitialOccurrenceCount, testQuestionRetryCount, questionsLearned, currentQuestion;
    private String testBasePath;
    private ArrayList<TestQuestion> testQuestions = new ArrayList<>();
    private ObservableList<ToggleButton> answerButtons = FXCollections.observableArrayList();

    @FXML
    private BorderPane questionPane;
    @FXML
    private ListView answersList;
    @FXML
    private Button startTestButton;
    @FXML
    private Button checkAnswerButton;
    @FXML
    private Button nextQuestionButton;
    @FXML
    private Label questionsLearnedLabel;
    @FXML
    private Label questionsAllLabel;

    void initData(int pInitial, int pRetry, String pBase){
        testInitialOccurrenceCount = pInitial;
        testQuestionRetryCount = pRetry;
        testBasePath = pBase;
    }

    public void initialize(){
        nextQuestionButton.setDisable(true);
        answersList.setFocusTraversable(false);
    }

    @FXML
    public void startTest(){
        startTestButton.setVisible(false);
        //Loading questions; "legacy" loading refers to old to system which was used by older "Testownik"
        testQuestions = legacyDatabaseLoader(testBasePath);
        //Initializing labels
        System.out.println(testQuestions.size());
        questionsAllLabel.setText(Integer.toString(testQuestions.size()));
        questionsLearnedLabel.setText("0");
        //Picking first random question
        Random rn = new Random();
        currentQuestion = rn.nextInt(testQuestions.size());
        //Setting question and answers
        setQuestion(testQuestions.get(currentQuestion).getQuestion());
        answerButtons = setAnswers(testQuestions.get(currentQuestion).getAnswers());
        answersList.setItems(answerButtons);
    }

    @FXML
    public void checkAnswer(){
        int goodAnswersSelected = 0;
        int goodAnswers = 0;
        nextQuestionButton.setDisable(false);
        checkAnswerButton.setDisable(true);
        //Marking good and bad answers and also checking if user chose good one
        for(ToggleButton b : answerButtons){
            if(b.isSelected()){
                if(b.getId().equals("true")){
                    b.setStyle("-fx-background-color: green; -fx-alignment: center; -fx-font-size: 18px");
                    goodAnswers++;
                    goodAnswersSelected++;
                }else{
                    b.setStyle("-fx-background-color: red; -fx-alignment: center; -fx-font-size: 18px");
                }
            }else{
                if(b.getId().equals("true")){
                    b.setStyle("-fx-background-color: greenyellow; -fx-alignment: center; -fx-font-size: 18px"); //TODO
                    goodAnswers++;
                }
            }
        }
        //If user answered correctly question occurrence is lowered, otherwise retryCount is added to occurrence count
        if(goodAnswersSelected == goodAnswers){
            testQuestions.get(currentQuestion).goodAnswer();
        }else{
            testQuestions.get(currentQuestion).badAnswer();
        }
        //If question is learned, or occurrence count is 0 then remove question from the array.
        if(testQuestions.get(currentQuestion).getOccurrenceCount() == 0){
            testQuestions.remove(testQuestions.get(currentQuestion));
            questionsLearned++;
        }
    }

    @FXML
    public void nextQuestion(){
        if(testQuestions.size() == 0){
            new Dialog("Koniec", "Udało się! Wszystkie pytania przerobione!", Alert.AlertType.INFORMATION);
            System.exit(0); //TODO Better exit window
        }
        nextQuestionButton.setDisable(true);
        checkAnswerButton.setDisable(false);
        //Updating labels
        questionsLearnedLabel.setText(Integer.toString(questionsLearned));
        //Picking next random question from remaining ones
        Random rn = new Random();
        currentQuestion = rn.nextInt(testQuestions.size());
        //Setting question and answers
        setQuestion(testQuestions.get(currentQuestion).getQuestion());
        answerButtons = setAnswers(testQuestions.get(currentQuestion).getAnswers());
        answersList.setItems(answerButtons);
        nextQuestionButton.setDisable(true);
        checkAnswerButton.setDisable(false);
    }

    private ArrayList<TestQuestion> legacyDatabaseLoader(String pPathToDatabase){
        //Function loads questions from files and uses addTestQuestion function to parse text from files. Then it adds questions to array.
        ArrayList<TestQuestion> pTestQuestions = new ArrayList<>();
        int i = 0;
        do{
            i++;
            if(i < 10) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + "00" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addTestQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 10 && i < 100){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + "0" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addTestQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 100){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pPathToDatabase + "\\" + i + ".txt"), "Windows-1250"))) {
                    pTestQuestions.add(addTestQuestion(reader));
                } catch (IOException e) { break; }
            }
        }while(true);

        return pTestQuestions;
    }

    private TestQuestion addTestQuestion(BufferedReader reader) throws IOException{
        //Function parse questions from text files
        String readQuestion, goodAnswerString, currentLine;
        ArrayList<TestAnswer> readAnswers = new ArrayList<>();
        //Good answer string (in format X0100011 where 1 is good answer and 0 is bad answer) which is remembered and used to mark good answers later on.
        goodAnswerString = reader.readLine();
        //Question text.
        readQuestion = reader.readLine();
        //Loop for adding answers, it ends at the end of the file.
        int goodQuestionCheck = 1;
        while((currentLine = reader.readLine()) != null){
            if(goodAnswerString.charAt(goodQuestionCheck) == '1'){
                readAnswers.add(new TestAnswer(currentLine, true));
            }else{
                readAnswers.add(new TestAnswer(currentLine, false));
            }
            goodQuestionCheck++;
        }
        return new TestQuestion(readQuestion, readAnswers, testInitialOccurrenceCount, testQuestionRetryCount);
    }

    private void setQuestion(String question){
        //Function setups questions when new test question is chosen.
        Label questionLabel = new Label();
        if(!question.startsWith("[img]")){
            //Normal text questions.
            questionLabel.setPrefWidth(1200);
            questionLabel.setPrefHeight(190);
            questionLabel.setAlignment(Pos.CENTER);
            questionLabel.setStyle("-fx-alignment: center; -fx-font-size: 20px");
            questionLabel.setWrapText(true);
            questionLabel.setText(question);
            questionPane.setCenter(questionLabel);
        }else{
            //Image questions.
            String imageQuestion;
            imageQuestion = question.substring(5,12);
            try{
                System.out.println(testBasePath + "\\" + imageQuestion);
                Image image = new Image(new FileInputStream(testBasePath + "\\" + imageQuestion));
                questionPane.setCenter(new ImageView(image));
            }catch(FileNotFoundException e){
                e.getStackTrace();
            }
        }
    }

    private ObservableList<ToggleButton> setAnswers(ArrayList<TestAnswer> pTestAnswers){
        //Function setups answers when new test question is chosen.
        ObservableList<ToggleButton> answersButtons = FXCollections.observableArrayList();
        for(TestAnswer q : pTestAnswers) {
            if (!q.getAnswerText().startsWith("[img]")) {
                //Normal text answers.
                ToggleButton btn = new ToggleButton();
                btn.setWrapText(true);
                btn.setAlignment(Pos.CENTER);
                btn.setPrefWidth(715);
                btn.setStyle("-fx-alignment: center; -fx-font-size: 18px");
                btn.setText(q.getAnswerText());
                btn.setId(Boolean.toString(q.isCorrect()));
                answersButtons.add(btn);
            }else {
                //Image answers.
                String imageAnswer;
                imageAnswer = q.getAnswerText().substring(5,13);
                try{
                    System.out.println(testBasePath + "\\" + imageAnswer);
                    Image image = new Image(new FileInputStream(testBasePath + "\\" + imageAnswer));
                    ToggleButton btn = new ToggleButton();
                    btn.setAlignment(Pos.CENTER);
                    btn.setPrefWidth(715);
                    btn.setGraphic(new ImageView(image));
                    btn.setId(Boolean.toString(q.isCorrect()));
                    answersButtons.add(btn);
                }catch(FileNotFoundException e){
                    e.getStackTrace();
                }
            }
        }
        answersButtons = shuffle(answersButtons);
        return answersButtons;
    }

    private ObservableList<ToggleButton> shuffle(ObservableList<ToggleButton> list){
        Random rn = new Random();
        ObservableList<ToggleButton> tmpList = FXCollections.observableArrayList();
        while(list.size() > 0){
            int randomIndex = rn.nextInt(list.size());
            tmpList.add(list.get(randomIndex));
            list.remove(list.get(randomIndex));
        }
        return tmpList;
    }
}