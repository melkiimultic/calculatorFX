package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


public class CalculatorController {
    @FXML
    private Label result;

    private BigDecimal num1 = new BigDecimal(0);
    private BigDecimal num2 = new BigDecimal(0);
    private String operator = "";
    private boolean start = true;
    private boolean firstHasPoint = false;
    private boolean secondHasPoint = false;
    private boolean positiveSecNum = true;


    Model model = new Model();

    @FXML
    public void pressNumbers(ActionEvent event) {
        if (start) {
            result.setText("");
            start = false;
        }
        String numValue = ((Button) event.getSource()).getText();
        result.setText(result.getText() + numValue);

    }

    @FXML
    public void pressPoint(ActionEvent event) {
        if (start) {
            result.setText("0.");
            start = false;
            firstHasPoint = true;
            return;
        }
        String text = result.getText();
        if (operator.isEmpty()) {
            if (firstHasPoint) {
                return;
            }

            if (text.equals("-")) {
                result.setText("-0.");
                firstHasPoint = true;
                return;
            }
            result.setText(text + ".");
            firstHasPoint = true;
            return;
        }

        if (secondHasPoint) {
            return;
        }

        if (StringUtils.endsWith(text, "-") || StringUtils.endsWith(text, "-")) {
            result.setText(text + "0.");
            secondHasPoint = true;
            return;
        }
        result.setText(text + ".");
        secondHasPoint = true;

    }

    @FXML
    public void pressMinus(ActionEvent event) {
        if (start) {
            result.setText("-");
            start = false;
            return;
        }
        String text = result.getText();
        if (operator.isEmpty()) {
            operator = "-";
            num1 = new BigDecimal(text);
            result.setText(text+"-");
            return;
        }
        if (!positiveSecNum) {
            return;
        }
        result.setText(text + "-");
        positiveSecNum =false;
    }

    @FXML
    public void pressRestThreeOperators(ActionEvent event) {
        if (start || !operator.isEmpty()) {
            return;
        }
        String operValue = ((Button) event.getSource()).getText();
        operator = operValue;
        String text = result.getText();
        num1 = new BigDecimal(text);
        result.setText(text + operValue);

    }

    @FXML
    public void pressEquals(ActionEvent event) {
        if (operator.isEmpty() || start) {
            result.setText("Illegal operation!");
            resetFlags();
            return;//todo some kind of msg
        }

        String[] splitted = StringUtils.split(result.getText(), operator);
        String secNum = splitted[1];
        if (!positiveSecNum && operator.equals("-")) {
            secNum = "-"+secNum;
        }
        num2 = new BigDecimal(secNum);
        String output = model.calculate(num1, num2, operator);
        if (output.equals("Division by zero!")) {
            result.setText(output);
            resetFlags();
            return;
        }
        result.setText(result.getText() + "=" + output);
        resetFlags();
    }

    @FXML
    public void pressClear(ActionEvent event) {
        result.setText("");
        resetFlags();

    }

    @FXML
    public void pressDelete(ActionEvent event) {
        if (!result.getText().isEmpty()) {
            String current = result.getText();

            if (!operator.isEmpty()) {
                if (StringUtils.endsWith(current, "-") && !positiveSecNum) {
                    positiveSecNum = true;
                }
                if (StringUtils.endsWith(current, operator)) {
                    operator = "";
                }
                if (StringUtils.endsWith(current, ".")) {
                    secondHasPoint = false;

                }
            }
            if (StringUtils.endsWith(current, ".")) {
                firstHasPoint = false;
            }

            String chopped = StringUtils.chop(current);
            result.setText(chopped);
        }

    }

    private void resetFlags() {
        operator = "";
        start = true;
        firstHasPoint = false;
        secondHasPoint = false;
        positiveSecNum = true;
    }


}








