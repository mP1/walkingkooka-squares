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

import org.junit.jupiter.api.Test;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePatterns;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatternsTest extends SpreadsheetMetadataPropertyNameTestCase<SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns, SpreadsheetNumberParsePatterns> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    @Test
    public void testExtractLocaleValue() throws ParseException{
        this.extractLocaleValueAndCheck("1.25");
    }

    @Test
    public void testExtractLocaleValueInteger() throws ParseException{
        this.extractLocaleValueAndCheck("789");
    }

    private void extractLocaleValueAndCheck(final String text) throws ParseException {
        this.extractLocaleValueAndCheck2(
                text,
                ExpressionNumberKind.BIG_DECIMAL
        );
        this.extractLocaleValueAndCheck2(
                text,
                ExpressionNumberKind.DOUBLE
        );
    }

    private void extractLocaleValueAndCheck2(final String text,
                                             final ExpressionNumberKind kind) throws ParseException {
        final Locale locale = Locale.ENGLISH;
        final SpreadsheetNumberParsePatterns pattern = SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns.instance()
                .extractLocaleValue(locale)
                .get();

        final ExpressionNumber value = pattern.converter()
                .convertOrFail(text,
                        ExpressionNumber.class,
                        ExpressionNumberConverterContexts.basic(
                                Converters.fake(),
                                ConverterContexts.basic(
                                        Converters.fake(),
                                        DateTimeContexts.locale(locale, 1900, 20),
                                        DecimalNumberContexts.american(MathContext.DECIMAL32)
                                ),
                                kind)
                );

        final DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setParseBigDecimal(true);
        final Number expected = decimalFormat.parse(text);

        assertEquals(
                kind.create(expected),
                value,
                () -> pattern + "\n" + kind + "\nDecimalFormat: " + decimalFormat.toPattern()
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns.instance(), "number-parse-patterns");
    }

    @Override
    SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns createName() {
        return SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns.instance();
    }

    @Override
    SpreadsheetNumberParsePatterns propertyValue() {
        return SpreadsheetNumberParsePatterns.parseNumberParsePatterns("#.## \"pattern-1\";#.00 \"pattern-2\"");
    }

    @Override
    String propertyValueType() {
        return "Number parse patterns";
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns> type() {
        return SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns.class;
    }
}
