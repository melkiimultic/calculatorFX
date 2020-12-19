package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Model {

    public  String calculate(BigDecimal firstNum, BigDecimal secondNum, String operator) {
        switch (operator){
            case "+":
                BigDecimal bd1 = firstNum.add(secondNum).setScale(9,RoundingMode.HALF_UP).stripTrailingZeros();
                String ans1 =bd1.toPlainString();
                return ans1;
            case "-":
                BigDecimal bd2 = firstNum.subtract(secondNum).setScale(9, RoundingMode.HALF_UP).stripTrailingZeros();
                String ans2 = bd2.toPlainString();
                return ans2;
            case "/":
                if(secondNum.compareTo(BigDecimal.ZERO)==0){
                   return "Division by zero!";// todo illegal operation
                }
                BigDecimal bd3 = firstNum.divide(secondNum,9,RoundingMode.HALF_UP).stripTrailingZeros();
                String ans3 = bd3.toPlainString();
                return ans3;
            case "*":
                BigDecimal bd4 = firstNum.multiply(secondNum).setScale(9, RoundingMode.HALF_UP).stripTrailingZeros();
                String ans4 = bd4.toPlainString();
                return ans4;
            default:
                throw new IllegalArgumentException("Don't know shit on "+operator);
        }
    }
}
