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

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetFormatterContextTest implements SpreadsheetFormatterContextTesting<BasicSpreadsheetFormatterContext> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;
    private final static Locale LOCALE = Locale.CANADA_FRENCH;

    @Test
    public void testWithNullNumberToColorFails() {
        this.withFails(null,
                this.nameToColor(),
                CELL_CHARACTER_WIDTH,
                this.defaultSpreadsheetFormatter(),
                this.converterContext());
    }

    @Test
    public void testWithNullNameToColorFails() {
        this.withFails(this.numberToColor(),
                null,
                CELL_CHARACTER_WIDTH,
                this.defaultSpreadsheetFormatter(),
                this.converterContext());
    }

    @Test
    public void testWithInvalidCellCharacterWidthFails() {
        assertThrows(IllegalArgumentException.class, () -> BasicSpreadsheetFormatterContext.with(this.numberToColor(),
                this.nameToColor(),
                -1,
                this.defaultSpreadsheetFormatter(),
                this.converterContext()));
    }

    @Test
    public void testWithInvalidCellCharacterWidthFails2() {
        assertThrows(IllegalArgumentException.class, () -> BasicSpreadsheetFormatterContext.with(this.numberToColor(),
                this.nameToColor(),
                0,
                this.defaultSpreadsheetFormatter(),
                this.converterContext()));
    }

    @Test
    public void testWithNullDefaultSpreadsheetFormatterFails() {
        this.withFails(this.numberToColor(),
                this.nameToColor(),
                CELL_CHARACTER_WIDTH,
                null,
                this.converterContext());
    }

    @Test
    public void testWIthNullConverterContextFails() {
        this.withFails(this.numberToColor(),
                this.nameToColor(),
                CELL_CHARACTER_WIDTH,
                this.defaultSpreadsheetFormatter(),
                null);
    }

    private void withFails(final Function<Integer, Optional<Color>> numberToColor,
                           final Function<SpreadsheetColorName, Optional<Color>> nameToColor,
                           final int width,
                           final SpreadsheetFormatter defaultSpreadsheetFormatter,
                           final ExpressionNumberConverterContext converterContext) {
        assertThrows(NullPointerException.class, () -> BasicSpreadsheetFormatterContext.with(numberToColor,
                nameToColor,
                width,
                defaultSpreadsheetFormatter,
                converterContext));
    }

    @Test
    public void testColorNumber() {
        this.colorNumberAndCheck(this.createContext(), 1, Optional.of(this.color()));
    }

    @Test
    public void testColorName() {
        this.colorNameAndCheck(this.createContext(),
                SpreadsheetColorName.with("bingo"),
                Optional.of(this.color()));
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(1, Boolean.class, Boolean.TRUE);
    }

    @Test
    public void testConvert2() {
        this.convertAndCheck(0, Boolean.class, Boolean.FALSE);
    }

    @Test
    public void testDefaultFormatText() {
        this.defaultFormatTextAndCheck(BigDecimal.valueOf(12.5),
                Optional.of(SpreadsheetText.with(SpreadsheetText.WITHOUT_COLOR, "012.500")));
    }

    @Test
    public void testLocale() {
        this.hasLocaleAndCheck(this.createContext(), LOCALE);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(),
                "cellCharacterWidth=1 numberToColor=1=#123456 nameToColor=bingo=#123456 converterContext=locale=\"fr-CA\" twoDigitYear=50 \"$$\" '!' \"E\" 'G' 'N' 'P' 'L' fr_CA precision=7 roundingMode=HALF_EVEN DOUBLE");
    }

    @Override
    public BasicSpreadsheetFormatterContext createContext() {
        final DecimalNumberContext decimalNumberContext = this.decimalNumberContext();

        return BasicSpreadsheetFormatterContext.with(this.numberToColor(),
                this.nameToColor(),
                CELL_CHARACTER_WIDTH,
                this.defaultSpreadsheetFormatter(),
                this.converterContext());
    }

    private Function<Integer, Optional<Color>> numberToColor() {
        return new Function<>() {

            @Override
            public Optional<Color> apply(final Integer number) {
                assertEquals(number, 1, "color number");
                return Optional.of(BasicSpreadsheetFormatterContextTest.this.color());
            }

            @Override
            public String toString() {
                return 1 + "=" + BasicSpreadsheetFormatterContextTest.this.color();
            }
        };
    }

    private Function<SpreadsheetColorName, Optional<Color>> nameToColor() {
        return new Function<>() {

            @Override
            public Optional<Color> apply(final SpreadsheetColorName name) {
                assertEquals(name, SpreadsheetColorName.with("bingo"), "color name");
                return Optional.of(BasicSpreadsheetFormatterContextTest.this.color());
            }

            @Override
            public String toString() {
                return "bingo=" + BasicSpreadsheetFormatterContextTest.this.color();
            }
        };
    }

    private Color color() {
        return Color.fromRgb(0x123456);
    }

    private final static int CELL_CHARACTER_WIDTH = 1;

    private SpreadsheetFormatter defaultSpreadsheetFormatter() {
        return new SpreadsheetFormatter() {
            @Override
            public boolean canFormat(final Object value,
                                     final SpreadsheetFormatterContext context) throws SpreadsheetFormatException {
                return value instanceof BigDecimal;
            }

            @Override
            public Optional<SpreadsheetText> format(final Object value, final SpreadsheetFormatterContext context) throws SpreadsheetFormatException {
                return Optional.of(SpreadsheetText.with(SpreadsheetText.WITHOUT_COLOR, new DecimalFormat("000.000").format(value)));
            }
        };
    }

    private ExpressionNumberConverterContext converterContext() {
        return ExpressionNumberConverterContexts.basic(
                Converters.truthyNumberBoolean(),
                ConverterContexts.basic(Converters.fake(),
                        this.dateTimeContext(),
                        this.decimalNumberContext()),
                EXPRESSION_NUMBER_KIND
        );
    }

    private DateTimeContext dateTimeContext() {
        return DateTimeContexts.locale(
                LOCALE,
                1900,
                50
        );
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.basic(this.currencySymbol(),
                this.decimalSeparator(),
                this.exponentSymbol(),
                this.groupingSeparator(),
                this.negativeSign(),
                this.percentageSymbol(),
                this.positiveSign(),
                LOCALE,
                this.mathContext());
    }

    @Override
    public String currencySymbol() {
        return "$$";
    }

    @Override
    public char decimalSeparator() {
        return '!';
    }

    @Override
    public String exponentSymbol() {
        return "E";
    }

    @Override
    public char groupingSeparator() {
        return 'G';
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char negativeSign() {
        return 'N';
    }

    @Override
    public char percentageSymbol() {
        return 'P';
    }

    @Override
    public char positiveSign() {
        return 'L';
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicSpreadsheetFormatterContext> type() {
        return BasicSpreadsheetFormatterContext.class;
    }
}
