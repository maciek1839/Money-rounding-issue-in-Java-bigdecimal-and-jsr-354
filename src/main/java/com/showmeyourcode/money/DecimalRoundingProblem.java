package com.showmeyourcode.money;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.util.Locale;

public class DecimalRoundingProblem {

    public static final double A = 0.7;
    public static final double B = 0.9;
    public static final double DIFFERENCE_UNIT = 0.1;
    public static final BigDecimal BIG_DECIMAL_A = BigDecimal.valueOf(A);
    public static final BigDecimal BIG_DECIMAL_B = BigDecimal.valueOf(B);

    public static void main(String[] args) {
        roundingProblem();

        solutionUsingBigDecimal();
        bigDecimalWarning();

        solutionUsingJavaMoney();
    }

    private static void bigDecimalWarning() {
        System.out.println("\n=== BigDecimal - Be AWARE ===");
        double value = 0.585;
        System.out.println(new BigDecimal(value));
        System.out.println(BigDecimal.valueOf(value));
    }

    private static void solutionUsingBigDecimal() {
        //setup
        BigDecimal differenceUnit = BigDecimal.valueOf(0.1);

        // calculations
        BigDecimal result = BIG_DECIMAL_A.subtract(BIG_DECIMAL_B);
        BigDecimal x = BIG_DECIMAL_A.add(differenceUnit);
        BigDecimal y = BIG_DECIMAL_B.subtract(differenceUnit);

        System.out.println("\n=== BigDecimal ===");
        printResult(BIG_DECIMAL_A, BIG_DECIMAL_B, result, x, y);
    }

    private static void solutionUsingJavaMoney() {
        // setup - monetary format (currency details)
        AmountFormatQuery formatQuery = AmountFormatQueryBuilder
                .of(Locale.US)
                .set(CurrencyStyle.SYMBOL)
                .set("pattern", "###.##")
                .build();
        MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(formatQuery);

        // setup - values using various ways of creating a currency unit
        MonetaryAmount a = Money.of(BIG_DECIMAL_A, "USD");
        MonetaryAmount b = Money.of(B, Monetary.getCurrency("USD"));
        MonetaryAmount unit = Money.of(DIFFERENCE_UNIT, Monetary.getCurrency(Locale.US));

        // calculations
        MonetaryAmount result = a.subtract(b);
        MonetaryAmount x = a.add(unit);
        MonetaryAmount y = b.subtract(unit);

        System.out.println("\n=== JAVA Money ===");
        printResult(
                amountFormat.format(a),
                amountFormat.format(b),
                amountFormat.format(result),
                amountFormat.format(x),
                amountFormat.format(y)
        );
    }

    private static void printResult(Object a,
                                    Object b,
                                    Object result,
                                    Object x,
                                    Object y) {
        System.out.println("A: " + a);
        System.out.println("B: " + b);
        System.out.println("X: A + UNIT(" + DIFFERENCE_UNIT + ") = " + x);
        System.out.println("Y: B - UNIT(" + DIFFERENCE_UNIT + ") = " + y);
        System.out.println("A - B: " + result);
        System.out.println("X == Y equals: " + (x == y));
    }

    private static void roundingProblem() {
        double result = A - B;
        double x = A + DIFFERENCE_UNIT;
        double y = B - DIFFERENCE_UNIT;

        System.out.println("\nTypical decimal rounding issue");
        printResult(A, B, result, x, y);
    }
}
