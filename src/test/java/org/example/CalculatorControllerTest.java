package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextInputControlMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;


public class CalculatorControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(App.class.getResource("calculator.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    @DisplayName("Button 1 can be pressed")
    public void testButton1() {
        clickOn("#one");
        verifyThat("#result", hasText("1"));
    }

    @Test
    @DisplayName("1+1=2")
    public void testSum() {
        clickOn("#one");
        clickOn("#plus");
        clickOn("#one");
        clickOn("#equals");
        verifyThat("#result", hasText("1+1=2"));
    }

    @Test
    @DisplayName("1/3=0.333333333;9 numbers after point")
    public void testScaleAndRounding() {
        clickOn("#one");
        clickOn("#div");
        clickOn("#three");
        clickOn("#equals");
        verifyThat("#result", hasText("1/3=0.333333333"));
    }

    @Test
    @DisplayName("1+9=10; no trailing zeros")
    public void testTrailingZeros() {
        clickOn("#one");
        clickOn("#plus");
        clickOn("#nine");
        clickOn("#equals");
        verifyThat("#result", hasText("1+9=10"));
    }

    @Test
    @DisplayName("Division by zero")
    public void testDivisionByZero() {
        clickOn("#one");
        clickOn("#div");
        clickOn("#zero");
        clickOn("#equals");
        verifyThat("#result", hasText("Division by zero!"));
    }

    @Test
    @DisplayName("Clear and insert new expression")
    public void testClrButton() {
        clickOn("#one");
        clickOn("#div");
        clickOn("#one");
        clickOn("#clear");
        verifyThat("#result", hasText(""));
        clickOn("#one");
        clickOn("#plus");
        clickOn("#nine");
        clickOn("#equals");
        verifyThat("#result", hasText("1+9=10"));

    }

    @Test
    @DisplayName(".1-.2=-0.1")
    public void testAddMissingZerosWhenMinus() {
        clickOn("#point");
        clickOn("#one");
        clickOn("#minus");
        clickOn("#point");
        clickOn("#two");
        clickOn("#equals");
        verifyThat("#result", hasText("0.1-0.2=-0.1"));
    }

    @Test
    @DisplayName(".1+.2=0.3")
    public void testAddMissingZerosWhenOtherOperators() {
        clickOn("#point");
        clickOn("#one");
        clickOn("#plus");
        clickOn("#point");
        clickOn("#two");
        clickOn("#equals");
        verifyThat("#result", hasText("0.1+0.2=0.3"));
    }

    @Test
    @DisplayName("can insert minus after operator")
    public void testMinusAfterOperator() {
        clickOn("#minus");
        clickOn("#two");
        clickOn("#div");
        clickOn("#minus");
        clickOn("#one");
        clickOn("#equals");
        verifyThat("#result", hasText("-2/-1=2"));
    }
    @Test
    @DisplayName("can't insert second minus before first num")
    public void testMinusBeforeFirstNum() {
        clickOn("#minus");
        clickOn("#minus");
        clickOn("#one");
        verifyThat("#result", hasText("-1"));
    }

    @Test
    @DisplayName("can't insert plus/div/mult after operator")
    public void testSecondOperator() {
        clickOn("#two");
        clickOn("#div");
        clickOn("#plus");
        clickOn("#mult");
        clickOn("#div");
        verifyThat("#result", hasText("2/"));
    }

    @Test
    @DisplayName("delete last symbol")
    public void testDelButton() {
        clickOn("#two");
        clickOn("#div");
        clickOn("#minus");
        clickOn("#one");
        clickOn("#del");
        clickOn("#del");
        verifyThat("#result", hasText("2/"));
    }

    @Test
    @DisplayName("div/plus/mult/equals are blocked at start")
    public void testBlockedSymbolsAtStart() {
        clickOn("#div");
        verifyThat("#result", hasText(""));
        clickOn("#mult");
        verifyThat("#result", hasText(""));
        clickOn("#plus");
        verifyThat("#result", hasText(""));
        clickOn("#equals");
        verifyThat("#result", hasText(""));

    }

    @Test
    @DisplayName("equals is blocked if operator is absent")
    public void testEqualsBeforeOperator() {
        clickOn("#one");
        clickOn("#equals");
        verifyThat("#result", hasText("1"));
        clickOn("#minus");
        clickOn("#equals");
        verifyThat("#result", hasText("1-"));

    }
    @Test
    @DisplayName("can't put second point after deleting operator")
    public void testPointWithDeleteLogic() {
        clickOn("#one");
        clickOn("#point");
        clickOn("#two");
        clickOn("#div");
        clickOn("#del");
        clickOn("#point");
        verifyThat("#result", hasText("1.2"));

    }
    @Test
    @DisplayName("history button shows ten last expressions")
    public void testPressHistory(){
        clickOn("#two");
        clickOn("#plus");
        clickOn("#two");
        clickOn("#equals");
        for (int i=0;i<10;i++){
            clickOn("#one");
            clickOn("#plus");
            clickOn("#two");
            clickOn("#equals");
        }
        clickOn("#history");
        String finalText = "\n";
        for (int i=0;i<9;i++){
            finalText=finalText+"1+2=3"+"\n";
        }
        finalText=finalText+"1+2=3";
        verifyThat("#text", TextInputControlMatchers.hasText(finalText));

    }
    @Test
    @DisplayName("Division by zero expression isn't saved to DB")
    public void testDivisionByZeroHistory(){
        for (int i=0;i<10;i++){
            clickOn("#one");
            clickOn("#plus");
            clickOn("#two");
            clickOn("#equals");
        }
        clickOn("#one");
        clickOn("#div");
        clickOn("#zero");
        clickOn("#equals");
        clickOn("#history");
        String finalText = "\n";
        for (int i=0;i<9;i++){
            finalText=finalText+"1+2=3"+"\n";
        }
        finalText=finalText+"1+2=3";
        verifyThat("#text",TextInputControlMatchers.hasText(finalText));

    }
}