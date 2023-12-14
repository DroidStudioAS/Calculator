package com.aa.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView calcDisplay;
    String[] operations = {"+", "-", "/", "*"};
    public static String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calcDisplay = (TextView) findViewById(R.id.resultView);
    }
    public String getLastChar(){
        String lastChar = "";
        if(calcDisplay!=null){
            lastChar = (calcDisplay.getText().length()==0)? "" : calcDisplay.getText().subSequence(calcDisplay.getText().length() - 1, calcDisplay.getText().length()).toString();
        }
        return lastChar;
    }
    public int getLastLogicIndex(){
        int logicIndex= -1;

        String displayText = calcDisplay.getText().toString();
        if (displayText.length()==0)
            return logicIndex;

        for(int i = displayText.length() - 1; i >=0; i--){
            if (displayText.charAt(i) == '+' || displayText.charAt(i) == '-' || displayText.charAt(i) == '/' || displayText.charAt(i) == '*' ){
                logicIndex=i;
                break;
            }
        }
        return logicIndex;
    }
    public void handleNumberClick (View view){
        if(calcDisplay.getText().length()>30){
            return;
        }
        if((calcDisplay.getText().length() - getLastLogicIndex() + 1)>8){
            return;
        }
        switch (view.getId()){
            case R.id.oneButton:
                calcDisplay.append("1");
                break;
            case R.id.twoButton:
                calcDisplay.append("2");
                break;
            case R.id.threeButton:
                calcDisplay.append("3");
                break;
            case R.id.fourButton:
                calcDisplay.append("4");
                break;
            case R.id.fiveButton:
                calcDisplay.append("5");
                break;
            case R.id.sixButton:
                calcDisplay.append("6");
                break;
            case R.id.sevenButton:
                calcDisplay.append("7");
                break;
            case R.id.eightButton:
                calcDisplay.append("8");
                break;
            case R.id.nineButton:
                calcDisplay.append("9");
                break;
            case R.id.dotButton:
                if(calcDisplay.getText().length() > 0){

                    String[] digits = calcDisplay.getText().toString().split("[+-/*]");

                    if(digits.length>0 && !digits[digits.length-1].contains(".")){
                        if(!getLastChar().equals(".")){
                            calcDisplay.append(".");
                        }
                    }
                }
                break;
            case R.id.zeroButton:
                if(calcDisplay.length()>0){
                    String [] digits = calcDisplay.getText().toString().split("[-+*/]");

                    if(digits.length >0 && digits[digits.length-1].length()==1){
                        if(digits[digits.length-1].charAt(0) != '0'){
                            calcDisplay.append("0");
                        }
                    }else{
                        calcDisplay.append("0");
                    }
                    }else {
                        calcDisplay.append("0");
                }
                break;
                }
        }

    public void onePercent(View v){
        if(calcDisplay.getText().length()==0){
            return;
        }else {
            String in = calcDisplay.getText().toString();
            double input = (Double.parseDouble(in)) / 100;
            String output = Double.toString(input);
            calcDisplay.setText(output);
        }

    }

    public void handleLogicButtonClick(View view){
        if(calcDisplay.getText().length()>30){
            return;
        }
        if(Arrays.asList(operations).contains(getLastChar())){
            return;
        }
        switch (view.getId()){
            case R.id.plusButton:
                calcDisplay.append("+");
                break;
            case R.id.minusButton:
                calcDisplay.append("-");
                break;
            case R.id.multiplyButton:
                if(calcDisplay.getText().length()>0) {
                    calcDisplay.append("*");
                }
                break;
            case R.id.divideButton:
                if(calcDisplay.getText().length()>0) {
                    calcDisplay.append("/");
                }
                break;
        }
    }
    public void handleClearButtonClick(View view){
        CharSequence existingText = calcDisplay.getText();
        if(existingText.length()==0){
            return;
        }
        calcDisplay.setText(existingText.subSequence(0, existingText.length() -1));

    }
    public void handleClearAllButton(View view){
        calcDisplay.setText("");
    }
    public void handleEqualsClick(View view){
        if(calcDisplay.getText().length()==0 || Arrays.asList(operations).contains(getLastChar())){
            return;
        }
        if(input!=null){
            calcDisplay.setText(input);
        }
        String result = evaluate(calcDisplay.getText().toString());
        calcDisplay.setText(result);
        input=result;
    }
    public String evaluate (String expression){
        if(expression.charAt(0) == '+' || expression.charAt(0)== '-'){
            expression = 0 + expression;
        }
        String[] numbers = expression.split("[+-/*]");

        List<String> operationList = new ArrayList<>();
        for(int i = 0; i < expression.length(); i++){
            if (expression.charAt(i) == '+' ||expression.charAt(i) == '-' ||expression.charAt(i) == '*' ||expression.charAt(i) == '/'){
                operationList.add(String.valueOf(expression.charAt(i)));
            }
        }
        List<Float> numList = new ArrayList<>();
        for(int i = 0; i<numbers.length; i++){
            if(numbers[i].equals("-Infinity")){
                numList.add(Float.NEGATIVE_INFINITY);
            }else if(numbers[i].equals("Infinity")){
                numList.add(Float.POSITIVE_INFINITY);
            }else{
                try {
                    numList.add(Float.parseFloat(numbers[i]));
                }catch (Exception exc){
                    return "Err";
                }
            }
        }
        calculateAll(numList,operationList);
        String textRes = Float.toString(finalResult);
        return textRes;
    }
    float finalResult;
    public void calculateAll(List<Float> numbers, List<String> operations){
        if(numbers.size() == 1){
            finalResult = numbers.get(0);
            return;
        }
        float res = 0;
        int indexMultiply = operations.indexOf("*");
        int indexDivide = operations.indexOf("/");
        if(indexMultiply!=-1 && indexDivide!=-1){
            if(indexMultiply<indexDivide){
                res+= numbers.get(indexMultiply) * numbers.get(indexMultiply+1);
                numbers.set(indexMultiply,res);
                numbers.remove(indexMultiply+1);
                operations.remove(indexMultiply);
                //rekurzija
                calculateAll(numbers,operations);
                return;
            }else{
                res+= numbers.get(indexDivide) / numbers.get(indexDivide+1);

                numbers.set(indexDivide, res);
                numbers.remove(indexDivide+1);
                operations.remove(indexDivide);
                //rekurzija
                calculateAll(numbers, operations);
                return;
            }
        }
        if(indexMultiply!=-1){
            res+= numbers.get(indexMultiply) * numbers.get(indexMultiply+1);

            numbers.set(indexMultiply, res);
            numbers.remove(indexMultiply+1);
            operations.remove(indexMultiply);
            calculateAll(numbers,operations);
            return;
        }
        if (indexDivide !=-1){
            res+= numbers.get(indexDivide) / numbers.get(indexDivide+1);
            numbers.set(indexDivide, res);
            numbers.remove(indexDivide+1);
            operations.remove(indexDivide);
            calculateAll(numbers, operations);
            return;
        }
        int indexPlus = operations.indexOf("+");
        int indexMinus = operations.indexOf("-");
        if(indexPlus!=-1 && indexMinus!=-1){
            if(indexPlus<indexMinus){
                res+= numbers.get(indexPlus) + numbers.get(indexPlus+1);
                numbers.set(indexPlus,res);
                numbers.remove(indexPlus+1);
                operations.remove(indexPlus);
                calculateAll(numbers,operations);
                return;
            }else {
                res+= numbers.get(indexMinus) - numbers.get(indexMinus+1);
                numbers.set(indexMinus, res);
                numbers.remove(indexMinus+1);
                operations.remove(indexMinus);
                calculateAll(numbers,operations);
                return;
            }
        }
        if(indexPlus!=-1){
            res+= numbers.get(indexPlus) + numbers.get(indexPlus+1);
            numbers.set(indexPlus, res);
            numbers.remove(indexPlus+1);
            operations.remove(indexPlus);
            calculateAll(numbers,operations);
            return;
        }
        if(indexMinus!=-1){
            res+= numbers.get(indexMinus) - numbers.get(indexMinus+1);
            numbers.set(indexMinus, res);
            numbers.remove(indexMinus +1);
            operations.remove(indexMinus);
            calculateAll(numbers,operations);
            return;
        }
    }

}