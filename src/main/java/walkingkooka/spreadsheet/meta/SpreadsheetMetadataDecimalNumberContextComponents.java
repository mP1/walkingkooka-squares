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

package walkingkooka.spreadsheet.meta;

import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Handles building a {@link DecimalNumberContext} for {@link SpreadsheetMetadata#decimalNumberContext()}.
 */
final class SpreadsheetMetadataDecimalNumberContextComponents {

    static SpreadsheetMetadataDecimalNumberContextComponents with(final SpreadsheetMetadata metadata) {
        return new SpreadsheetMetadataDecimalNumberContextComponents(metadata);
    }

    private SpreadsheetMetadataDecimalNumberContextComponents(final SpreadsheetMetadata metadata) {
        super();
        this.components = SpreadsheetMetadataComponents.with(metadata);
    }

    final DecimalNumberContext decimalNumberContext() {
        final SpreadsheetMetadataComponents components = this.components;

        final String currencySymbol = components.getOrElse(SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL, this::localeCurrencySymbol);
        final Character decimalPoint = components.getOrElse(SpreadsheetMetadataPropertyName.DECIMAL_POINT, this::localeDecimalPoint);
        final Character exponentSymbol = components.getOrNull(SpreadsheetMetadataPropertyName.EXPONENT_SYMBOL);
        final Character groupingSeparator = components.getOrElse(SpreadsheetMetadataPropertyName.GROUPING_SEPARATOR, this::localeGroupingSeparator);
        final Character minusSign = components.getOrElse(SpreadsheetMetadataPropertyName.MINUS_SIGN, this::localeMinusSign);
        final Character percentSymbol = components.getOrElse(SpreadsheetMetadataPropertyName.PERCENTAGE_SYMBOL, this::localePercentageSymbol);
        final Character plusSign = components.getOrElse(SpreadsheetMetadataPropertyName.PLUS_SIGN, this::localePlusSign);

        final Integer precision = components.getOrNull(SpreadsheetMetadataPropertyName.PRECISION);
        final RoundingMode roundingMode = components.getOrNull(SpreadsheetMetadataPropertyName.ROUNDING_MODE);

        components.reportIfMissing();

        return DecimalNumberContexts.basic(currencySymbol,
                decimalPoint,
                exponentSymbol,
                groupingSeparator,
                minusSign,
                percentSymbol,
                plusSign,
                new MathContext(precision, roundingMode));
    }

    private String localeCurrencySymbol() {
        return this.tryPropertyFromLocale(DecimalNumberContext::currencySymbol);
    }

    private Character localeDecimalPoint() {
        return this.tryPropertyFromLocale(DecimalNumberContext::decimalPoint);
    }

    private Character localeGroupingSeparator() {
        return this.tryPropertyFromLocale(DecimalNumberContext::groupingSeparator);
    }

    private Character localeMinusSign() {
        return this.tryPropertyFromLocale(DecimalNumberContext::minusSign);
    }

    private Character localePercentageSymbol() {
        return this.tryPropertyFromLocale(DecimalNumberContext::percentageSymbol);
    }

    private Character localePlusSign() {
        return this.tryPropertyFromLocale(DecimalNumberContext::plusSign);
    }

    private <T> T tryPropertyFromLocale(final Function<DecimalNumberContext, T> localDecimalNumberContextGetter) {
        final DecimalNumberContext decimalNumberContext = this.localDecimalNumberContext();
        return null != decimalNumberContext ?
                localDecimalNumberContextGetter.apply(decimalNumberContext) :
                null;
    }

    /**
     * Lazy {@link Locale} getter.
     */
    private DecimalNumberContext localDecimalNumberContext() {
        if (null == this.locale) {
            this.locale = this.components.metadata.get(SpreadsheetMetadataPropertyName.LOCALE);
            this.decimalNumberContext = this.locale
                    .map(this::localDecimalNumberContext0)
                    .orElse(null);
        }
        return this.decimalNumberContext;
    }

    private DecimalNumberContext localDecimalNumberContext0(final Locale locale) {
        return DecimalNumberContexts.decimalFormatSymbols(DecimalFormatSymbols.getInstance(locale),
                'E',
                '+',
                MathContext.DECIMAL32); // exponent, plus, MathContext ignored
    }

    private Optional<Locale> locale;
    private DecimalNumberContext decimalNumberContext;

    final SpreadsheetMetadataComponents components;

    @Override
    public String toString() {
        return this.components.toString();
    }
}