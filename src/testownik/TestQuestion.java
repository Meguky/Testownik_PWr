package testownik;

import java.util.ArrayList;

class TestQuestion{
    private String question;
    private ArrayList<TestAnswer> answers;
    private int occurrenceCount, retryCountWhenErrorOccurred;
    TestQuestion(String pQ, ArrayList<TestAnswer> pA, int pIC, int pRC){
        question = pQ;
        answers = pA;
        occurrenceCount = pIC;
        retryCountWhenErrorOccurred = pRC;
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

    int getOccurrenceCount(){
        return occurrenceCount;
    }

    int getRetryCount(){
        return retryCountWhenErrorOccurred;
    }

    void badAnswer(){
        occurrenceCount += retryCountWhenErrorOccurred;
    }

    void goodAnswer(){
        occurrenceCount--;
    }
}