package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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

    private final CalculatorDataSource dataSource = CalculatorDataSource.getInstance();


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

        if (StringUtils.endsWith(text, operator) || StringUtils.endsWith(text, "-")) {
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
        if (text.equals("-")) {
            return;
        }
        if (operator.isEmpty()) {
            operator = "-";
            num1 = new BigDecimal(text);
            result.setText(text + "-");
            return;
        }
        if (!positiveSecNum) {
            return;
        }
        result.setText(text + "-");
        positiveSecNum = false;
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
        if (start || operator.isEmpty()) {
            return;
        }
        String text = result.getText();
        if (text.endsWith(operator) || text.endsWith("-")) {
            return;
        }
        String[] splitted = StringUtils.split(text, operator);
        String secNum = splitted[1];
        if (!positiveSecNum && operator.equals("-")) {
            secNum = "-" + secNum;
        }
        num2 = new BigDecimal(secNum);
        String output = Model.calculate(num1, num2, operator);
        if (output.equals("Division by zero!")) {
            result.setText(output);
            resetFlags();
            return;
        }
        String finalText = result.getText() + "=" + output;
        result.setText(finalText);
        resetFlags();
        dataSource.saveExpression(finalText);//todo transactional
    }

    @FXML
    public void pressClear(ActionEvent event) {
        result.setText("");
        resetFlags();

    }

    @FXML
    public void pressDelete(ActionEvent event) {
        if (start) {
            return;
        }

        if (!result.getText().isEmpty()) {
            String current = result.getText();

            if (operator.isEmpty() && StringUtils.endsWith(current, ".")) {
                firstHasPoint = false;
            }

            if (!operator.isEmpty()) {
                if (StringUtils.endsWith(current, "-") && !positiveSecNum) {
                    positiveSecNum = true;
                    String chopped = StringUtils.chop(current);
                    result.setText(chopped);
                    return;
                }
                if (StringUtils.endsWith(current, operator)) {
                    operator = "";
                }
                if (StringUtils.endsWith(current, ".")) {
                    secondHasPoint = false;

                }
            }

            String chopped = StringUtils.chop(current);
            result.setText(chopped);
        }

    }

    @FXML
    private void pressHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));
            Parent root = loader.load();
            HistoryController controller = loader.getController();
            controller.showHistory(root);

        } catch (IOException e) {
            throw new IllegalStateException("Can't create history window");
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








