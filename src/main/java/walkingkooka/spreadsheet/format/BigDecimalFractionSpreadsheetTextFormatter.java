/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.format;

import walkingkooka.color.Color;
import walkingkooka.math.Fraction;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatBigDecimalParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatFractionParserToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link SpreadsheetTextFormatter} that unconditionally formats a {@link BigDecimal}, without a {@link Color} using a pattern
 * parsed into a {@link SpreadsheetFormatFractionParserToken}.
 */
final class BigDecimalFractionSpreadsheetTextFormatter extends SpreadsheetTextFormatter3<SpreadsheetFormatFractionParserToken> {

    /**
     * Creates a {@link BigDecimalFractionSpreadsheetTextFormatter} from a {@link SpreadsheetFormatBigDecimalParserToken}.
     */
    static BigDecimalFractionSpreadsheetTextFormatter with(final SpreadsheetFormatFractionParserToken token,
                                                           final Function<BigDecimal, Fraction> fractioner) {
        check(token);
        Objects.requireNonNull(fractioner, "fractioner");

        return new BigDecimalFractionSpreadsheetTextFormatter(token, fractioner);
    }

    /**
     * Private ctor use static parse.
     */
    private BigDecimalFractionSpreadsheetTextFormatter(final SpreadsheetFormatFractionParserToken token,
                                                       final Function<BigDecimal, Fraction> fractioner) {
        super(token);

        this.fractioner = fractioner;

        final BigDecimalFractionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor =
                BigDecimalFractionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor.analyze(token);

        this.components = visitor.components;

        this.numeratorDigitSymbolCount = visitor.numeratorDigitSymbolCount;
        this.denominatorDigitSymbolCount = visitor.denominatorDigitSymbolCount;
        this.multiplier = visitor.multiplier;
    }

    @Override
    public boolean canFormat(final Object value) {
        return value instanceof BigDecimal;
    }

    @Override
    Optional<SpreadsheetFormattedText> format0(final Object value, final SpreadsheetTextFormatContext context) {
        return Optional.of(SpreadsheetFormattedText.with(
                SpreadsheetFormattedText.WITHOUT_COLOR,
                this.format1(BigDecimal.class.cast(value), context)));
    }

    /**
     * Compute the fraction of the {@link BigDecimal value}, and if the denominator is too many places, perform some
     * rounding until its the right number of digits.
     */
    private String format1(final BigDecimal value, final SpreadsheetTextFormatContext context) {
        final BigDecimal rounded = value.multiply(this.multiplier, context.mathContext())
                .setScale(this.denominatorDigitSymbolCount, RoundingMode.HALF_UP);

        final Fraction fraction = this.fractioner.apply(rounded);

        final int sign = rounded.signum();
        BigInteger numerator = fraction.numerator();
        if (numerator.signum() < 0) {
            numerator = numerator.negate();
        }

        BigInteger denominator = fraction.denominator().abs();

        final int places = decimalPlaces(denominator);
        for (int i = this.denominatorDigitSymbolCount; i < places; i++) {
            numerator = numerator.add(FIVE).divide(BigInteger.TEN);
            denominator = denominator.add(FIVE).divide(BigInteger.TEN);
        }

        return this.format2(BigDecimalFractionSpreadsheetTextFormatterComponentContext.with(BigDecimalFractionSpreadsheetTextFormatterMinusSign.fromSignum(sign),
                BigDecimalFractionSpreadsheetTextFormatterDigits.numerator(numerator.toString()),
                BigDecimalFractionSpreadsheetTextFormatterDigits.denominator(denominator.toString()),
                this,
                context));
    }

    /**
     * A non zero value multiplied against the {@link BigDecimal} being formatted as text.
     */
    private final BigDecimal multiplier;

    /**
     * Converts a {@link BigDecimal} into a {@link Fraction}.
     */
    private final Function<BigDecimal, Fraction> fractioner;

    /**
     * Returns the number of places in the value in its decimal form.
     */
    private static int decimalPlaces(final BigInteger value) {
        return new BigDecimal(value).precision();
    }

    private final static BigInteger FIVE = BigInteger.valueOf(5);

    /**
     * Applies the pattern and value into text.
     */
    private String format2(final BigDecimalFractionSpreadsheetTextFormatterComponentContext context) {
        this.components.forEach(c -> c.append(context));
        return context.formattedText();
    }

    /**
     * Components for each symbol in the original pattern.
     */
    private final List<BigDecimalFractionSpreadsheetTextFormatterComponent> components;

    final int numeratorDigitSymbolCount;
    final int denominatorDigitSymbolCount;

    @Override
    String toStringSuffix() {
        return "";
    }
}
