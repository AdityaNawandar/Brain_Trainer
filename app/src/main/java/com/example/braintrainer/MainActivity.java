package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity<intIndexOfCorrectAns> extends AppCompatActivity {

    Button btnGo;
    Button btnPlayAgain;
    Button btnA;
    Button btnB;
    Button btnC;
    Button btnD;
    TextView tvEquation;
    TextView tvRemark;
    TextView tvScore;
    TextView tvTimer;
    int intLeftSide = 0;
    int intRightSide = 0;
    String strOperator = "";
    ArrayList<Integer> intarrlstAnswers = new ArrayList<Integer>();
    int intIndexOfCorrectAns = 0;
    int intNoOfCorrectAns = 0;
    int intNoOfQuestions = 0;
    int intCorrectAns = 0;
    String strScore = "";
    ConstraintLayout clGameLayout;

    public void playAgain(View view){

        newQuestion();

        intNoOfCorrectAns = 0;
        intNoOfQuestions = 0;
        strScore = intNoOfCorrectAns + "/" + intNoOfQuestions;
        tvScore.setText(strScore);
        tvRemark.setText("");
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);

        new CountDownTimer(31000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                tvRemark.setText("Done!");
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnA.setEnabled(false);
                btnB.setEnabled(false);
                btnC.setEnabled(false);
                btnD.setEnabled(false);
            }

        }.start();
    }

    public void checkAnswer(View view) {
        intNoOfQuestions++;
        String strButtonPressedTag = view.getTag().toString();
        if (Integer.toString(intIndexOfCorrectAns).equals(strButtonPressedTag)) {
            tvRemark.setText("Correct!");
            intNoOfCorrectAns++;
        } else {
            tvRemark.setText("Wrong! :(");
        }
        strScore = intNoOfCorrectAns + "/" + intNoOfQuestions;
        tvScore.setText(strScore);

        newQuestion();

    }

    public void start(View view) {
        btnGo.setVisibility(View.INVISIBLE);
        clGameLayout.setVisibility(View.VISIBLE);
        playAgain(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRemark = findViewById(R.id.txtvwRemark);
        tvScore = findViewById(R.id.txtvwScore);
        tvTimer = findViewById(R.id.txtvwTimer);
        btnGo = findViewById(R.id.btnGo);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnGo.setVisibility(View.VISIBLE);
        clGameLayout = findViewById(R.id.clGameLayout);
        clGameLayout.setVisibility(View.INVISIBLE);
        }

    private void assignAnswers(){

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        btnA.setText(Integer.toString(intarrlstAnswers.get(0)));
        btnB.setText(Integer.toString(intarrlstAnswers.get(1)));
        btnC.setText(Integer.toString(intarrlstAnswers.get(2)));
        btnD.setText(Integer.toString(intarrlstAnswers.get(3)));
    }

    private void generateEquation() {
        //generate operands for the equation
        Random objRandom = new Random();
        intLeftSide = objRandom.nextInt(21);
        intRightSide = objRandom.nextInt(21);

        //choose a random operator
        char[] chrarrOperators = {'+', '-', '/', '*'};
        int intRandomIndex = objRandom.nextInt(chrarrOperators.length);    // [0, chrarrOperators.length-1]
        char charOperator = chrarrOperators[intRandomIndex];
        strOperator = String.valueOf(charOperator);

        //create equation
        String strEquation = intLeftSide + " " + strOperator + " " + intRightSide;
        tvEquation = findViewById(R.id.txtvwEquation);
        tvEquation.setText(strEquation);

        getCorrectAnswer(intLeftSide, intRightSide, strOperator);
    }

    private void generateAnswers(int intCorrectAns) {
        Random objRandom = new Random();
        intIndexOfCorrectAns = objRandom.nextInt(4);
        intarrlstAnswers.clear();
        for (int i = 0; i < 4; i++) {
            if (i == intIndexOfCorrectAns) {
                intarrlstAnswers.add(intCorrectAns);
            } else {
                int intIncorrectAns = objRandom.nextInt((intCorrectAns + 20) - (intCorrectAns - 20)) + (intCorrectAns - 20);
                while (intIncorrectAns == intCorrectAns) {
                    intIncorrectAns = objRandom.nextInt((intCorrectAns + 20) - (intCorrectAns - 20)) + (intCorrectAns - 20);
                }
                intarrlstAnswers.add(intIncorrectAns);
            }
        }

    }

    private int getCorrectAnswer(int intLeftSide, int intRightSide, String strOperator) {

        switch (strOperator) {
            case "+":
                intCorrectAns = intLeftSide + intRightSide;
                break;
            case "-":
                intCorrectAns = intLeftSide - intRightSide;
                break;
            case "*":
                intCorrectAns = intLeftSide * intRightSide;
                break;
            case "/":
                if ((intLeftSide % intRightSide == 0) && (intLeftSide > intRightSide)) {
                    intCorrectAns = intLeftSide / intRightSide;
                } else {
                    generateEquation();
                }
                break;
        }
        return intCorrectAns;

    }

    private void newQuestion(){
        generateEquation();
        generateAnswers(intCorrectAns);
        assignAnswers();
    }
}