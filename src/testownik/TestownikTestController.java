package testownik;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.*;

import java.util.ArrayList;


public class TestownikTestController {

    private int testInitialCount, testRetryCount;
    private String testBaseChoice;
    private ArrayList<TestQuestion> testQuestions = new ArrayList<>();

    @FXML
    private Button startTestButton;

    void initData(int pInitial, int pRetry, String pBase){
        testInitialCount = pInitial;
        testRetryCount = pRetry;
        testBaseChoice = pBase;
    }

    public void initialize(){ }

    @FXML
    public void startTest(){
        startTestButton.setVisible(false);
        testQuestions = legacyDatabaseLoader(testBaseChoice);
        for(TestQuestion q : testQuestions){
            System.out.println(q.toString() + "\n");
        }
        System.out.println(testQuestions.size());
    }

    private ArrayList<TestQuestion> legacyDatabaseLoader(String pPathToDatabase){
        ArrayList<TestQuestion> pTestQuestions = new ArrayList<>();

        int i = 0;
        do{
            i++;
            if(i < 10) {
                try (BufferedReader reader = new BufferedReader(new FileReader(new File(pPathToDatabase + "\\" + "00" + i + ".txt")))) {
                    pTestQuestions.add(addQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 10 && i < 100){
                try (BufferedReader reader = new BufferedReader(new FileReader(new File(pPathToDatabase + "\\" + "0" + i + ".txt")))) {
                    pTestQuestions.add(addQuestion(reader));
                } catch (IOException e) { break; }
            }
            if(i >= 100){
                try (BufferedReader reader = new BufferedReader(new FileReader(new File(pPathToDatabase + "\\" + i + ".txt")))) {
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
        return new TestQuestion(readQuestion, readAnswers);
    }
}

class TestQuestion{

    private String question;
    private ArrayList<TestAnswer> answers;

    TestQuestion(String pQ, ArrayList<TestAnswer> pA){
        question = pQ;
        answers = pA;
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