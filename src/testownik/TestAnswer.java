package testownik;

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