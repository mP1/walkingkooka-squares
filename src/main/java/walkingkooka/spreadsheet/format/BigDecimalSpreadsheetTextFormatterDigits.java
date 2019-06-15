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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;

/**
 * Base for the three categories of digits: Integers, Fractions and Exponent digits.
 */
abstract class BigDecimalSpreadsheetTextFormatterDigits implements UsesToStringBuilder {

    /**
     * {@see BigDecimalSpreadsheetTextFormatterIntegerDigits}
     */
    static BigDecimalSpreadsheetTextFormatterDigits integer(final BigDecimalSpreadsheetTextFormatterMinusSign minusSign,
                                                            final String text,
                                                            final BigDecimalSpreadsheetTextFormatterThousandsSeparator thousandsSeparator) {
        return BigDecimalSpreadsheetTextFormatterIntegerDigits.with(minusSign, text, thousandsSeparator);
    }

    /**
     * {@link BigDecimalSpreadsheetTextFormatterFractionDigits}
     */
    static BigDecimalSpreadsheetTextFormatterDigits fraction(final String text) {
        return BigDecimalSpreadsheetTextFormatterFractionDigits.with(text);
    }

    /**
     * {@see BigDecimalSpreadsheetTextFormatterExponentDigits}
     */
    static BigDecimalSpreadsheetTextFormatterDigits exponent(final BigDecimalSpreadsheetTextFormatterMinusSign minusSign,
                                                             final String text) {
        return BigDecimalSpreadsheetTextFormatterExponentDigits.with(minusSign, text);
    }

    /**
     * Package private to limit sub classing.
     */
    BigDecimalSpreadsheetTextFormatterDigits(final String text) {
        super();
        this.text = "0".equals(text) ?
                "" :
                text;
    }

    /**
     * Appends a digit, and possibly sign if necessary.
     */
    abstract void append(final int position,
                         final BigDecimalSpreadsheetTextFormatterZero zero,
                         final BigDecimalSpreadsheetTextFormatterComponentContext context);

    /**
     * Conditionally appends the sign if necessary, during INTEGER and EXPONENT digit formatting.
     */
    abstract void sign(final BigDecimalSpreadsheetTextFormatterComponentContext context);

    /**
     * Conditionally appends the thousands separator. This may only happen during formatting of INTEGER digits.
     */
    abstract void thousandsSeparator(final int numberDigitPosition, final BigDecimalSpreadsheetTextFormatterComponentContext context);

    /**
     * Text that holds the individual digit characters.
     */
    final String text;

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }
}
