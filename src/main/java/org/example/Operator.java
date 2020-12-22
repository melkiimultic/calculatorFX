package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;


public enum Operator {
    PLUS("+"), MINUS("-"), DIVIDE("/"), MULTIPLY("*"), NOOP("");

    final String textValue;

    Operator(String textValue) {
        this.textValue = textValue;
    }

    public static Operator parse(String s) {
        return Stream.of(Operator.values())
                .filter(e -> e.textValue.equals(s))
                .findFirst()
                .orElseThrow();
    }

    public String apply(BigDecimal first, BigDecimal second) {
        BigDecimal result;
        switch (this) {
            case PLUS:
                result = first.add(second).setScale(9, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            case MINUS:
                result = first.subtract(second).setScale(9, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            case MULTIPLY:
                result = first.multiply(second).setScale(9, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            case DIVIDE:
                if (second.compareTo(BigDecimal.ZERO) == 0) {
                    return "Division by zero!";
                }
                result = first.divide(second, 9, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            case NOOP:
            default:
                throw new IllegalArgumentException("Unknown operator " + this);
        }
        return result.toPlainString();
    }
}
