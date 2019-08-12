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

import walkingkooka.Context;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;

/**
 * The context that accompanies each {@link NumberSpreadsheetTextFormatterComponent}.
 */
final class NumberSpreadsheetTextFormatterComponentContext implements Context {

    /**
     * Factory that creates a new context.
     */
    static NumberSpreadsheetTextFormatterComponentContext with(final NumberSpreadsheetTextFormatterDigits integer,
                                                               final NumberSpreadsheetTextFormatterDigits fraction,
                                                               final NumberSpreadsheetTextFormatterDigits exponent,
                                                               final NumberSpreadsheetTextFormatter formatter,
                                                               final SpreadsheetTextFormatContext context) {
        return new NumberSpreadsheetTextFormatterComponentContext(integer, fraction, exponent, formatter, context);
    }

    /**
     * Private ctor use factory.
     */
    private NumberSpreadsheetTextFormatterComponentContext(final NumberSpreadsheetTextFormatterDigits integer,
                                                           final NumberSpreadsheetTextFormatterDigits fraction,
                                                           final NumberSpreadsheetTextFormatterDigits exponent,
                                                           final NumberSpreadsheetTextFormatter formatter,
                                                           final SpreadsheetTextFormatContext context) {
        super();

        this.integer = integer;
        this.fraction = fraction;
        this.exponent = exponent;

        this.formatter = formatter;
        this.context = context;

        this.digits = integer;
    }

    void appendCurrencySymbol() {
        this.text.append(this.context.currencySymbol());
    }

    void appendDigit(final int symbolDigitPosition, final NumberSpreadsheetTextFormatterZero zero) {
        this.digits.append(symbolDigitPosition, zero, this);
    }

    void appendDigit(final char c, final int numberDigitPosition) {
        this.digits.sign(this);
        this.text.append(c);
        this.digits.thousandsSeparator(numberDigitPosition, this);
    }

    void appendDecimalPoint(final NumberSpreadsheetTextFormatterDigits next) {
        this.digits.sign(this);
        this.text.append(this.context.decimalPoint());
        this.digits = next;
    }

    void appendExponent() {
        this.text.append(this.context.exponentSymbol());
        this.digits = this.exponent;
    }

    void appendGroupingSeparator() {
        this.text.append(this.context.groupingSeparator());
    }

    void appendNegativeSign() {
        this.text.append(this.context.negativeSign());
    }

    void appendPercentage() {
        this.text.append(this.context.percentageSymbol());
    }

    void appendText(final String text) {
        this.text.append(text);
    }

    private final SpreadsheetTextFormatContext context;

    NumberSpreadsheetTextFormatterDigits digits;

    final NumberSpreadsheetTextFormatterDigits integer;
    final NumberSpreadsheetTextFormatterDigits fraction;
    final NumberSpreadsheetTextFormatterDigits exponent;
    final NumberSpreadsheetTextFormatter formatter;

    /**
     * Getter that returns the formatted text.
     */
    String formattedText() {
        return this.text.toString();
    }

    /**
     * Accumulates the formatted text.
     */
    private final StringBuilder text = new StringBuilder();

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .separator("")
                .labelSeparator("")
                .value(this.integer)
                .value(this.fraction)
                .value(this.exponent)
                .label(" ")
                .value(this.text)
                .build();
    }
}
