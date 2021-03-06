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
import walkingkooka.color.Color;
import walkingkooka.convert.Converters;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeParsePatterns;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.visit.Visiting;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetMetadataVisitorTest implements SpreadsheetMetadataVisitorTesting<SpreadsheetMetadataVisitor> {

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public void testClassVisibility() {
    }

    @Override
    public void testStartVisitMethodsSingleParameter() {
    }

    @Override
    public void testEndVisitMethodsSingleParameter() {
    }

    @Test
    public void testVisitSpreadsheetMetadataSkip() {
        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

        new FakeSpreadsheetMetadataVisitor() {
            @Override
            protected Visiting startVisit(final SpreadsheetMetadata t) {
                assertSame(metadata, t);
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final SpreadsheetMetadata t) {
                assertSame(metadata, t);
            }
        }.accept(metadata);
    }

    @Test
    public void testVisitSpreadsheetMetadataPropertyNameSkip() {
        final SpreadsheetMetadataPropertyName<EmailAddress> propertyName = SpreadsheetMetadataPropertyName.CREATOR;
        final EmailAddress value = this.emailAddress();
        final SpreadsheetMetadata metadata = metadata(propertyName, value);

        new FakeSpreadsheetMetadataVisitor() {
            @Override
            protected Visiting startVisit(final SpreadsheetMetadata t) {
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SpreadsheetMetadata t) {
            }

            @Override
            protected Visiting startVisit(final SpreadsheetMetadataPropertyName<?> p, final Object v) {
                assertSame(propertyName, p, "propertyName");
                assertSame(value, v, "value");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final SpreadsheetMetadataPropertyName<?> p, final Object v) {
                assertSame(propertyName, p, "propertyName");
                assertSame(value, v, "value");
            }
        }.accept(metadata);
    }

    @Test
    public void testVisitCellCharacterWidth() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitCellCharacterWidth(final Integer i) {
                this.visited = i;
            }
        }.accept(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 0);
    }

    @Test
    public void testVisitCreator() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitCreator(final EmailAddress e) {
                this.visited = e;
            }
        }.accept(SpreadsheetMetadataPropertyName.CREATOR, this.emailAddress());
    }

    @Test
    public void testVisitCreateDateTime() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitCreateDateTime(final LocalDateTime d) {
                this.visited = d;
            }
        }.accept(SpreadsheetMetadataPropertyName.CREATE_DATE_TIME, this.dateTime());
    }

    @Test
    public void testVisitCurrencySymbol() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitCurrencySymbol(final String c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL, "$$");
    }

    @Test
    public void testVisitDateFormatPattern() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDateFormatPattern(final SpreadsheetDateFormatPattern p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN, SpreadsheetPattern.parseDateFormatPattern("DD/MM/YYYY"));
    }

    @Test
    public void testVisitDateParsePatterns() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDateParsePatterns(final SpreadsheetDateParsePatterns p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERNS, SpreadsheetDateTimeParsePatterns.parseDateParsePatterns("DD/MM/YYYY"));
    }

    @Test
    public void testVisitDateTimeFormatPattern() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDateTimeFormatPattern(final SpreadsheetDateTimeFormatPattern p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.DATETIME_FORMAT_PATTERN, SpreadsheetPattern.parseDateTimeFormatPattern("DD/MM/YYYY hh:mm"));
    }

    @Test
    public void testVisitDateTimeOffset() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDateTimeOffset(final Long offset) {
                this.visited = offset;
            }
        }.accept(SpreadsheetMetadataPropertyName.DATETIME_OFFSET, Converters.EXCEL_1900_DATE_SYSTEM_OFFSET);
    }

    @Test
    public void testVisitDateTimeParsePatterns() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDateTimeParsePatterns(final SpreadsheetDateTimeParsePatterns p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.DATETIME_PARSE_PATTERNS, SpreadsheetDateTimeParsePatterns.parseDateTimeParsePatterns("DD/MM/YYYY HH:MM:SS;DDMMYYYY HHMMSS"));
    }

    @Test
    public void testVisitDecimalSeparator() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDecimalSeparator(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR, '.');
    }

    @Test
    public void testVisitDefaultYear() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitDefaultYear(final int i) {
                this.visited = i;
            }
        }.accept(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 1901);
    }

    @Test
    public void testVisitExponentSymbol() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitExponentSymbol(final String s) {
                this.visited = s;
            }
        }.accept(SpreadsheetMetadataPropertyName.EXPONENT_SYMBOL, ".");
    }

    @Test
    public void testVisitGroupingSeparator() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitGroupingSeparator(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.GROUPING_SEPARATOR, ',');
    }

    @Test
    public void testVisitLocale() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitLocale(final Locale l) {
                this.visited = l;
            }
        }.accept(SpreadsheetMetadataPropertyName.LOCALE, Locale.ENGLISH);
    }

    @Test
    public void testVisitModifiedBy() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitModifiedBy(final EmailAddress e) {
                this.visited = e;
            }
        }.accept(SpreadsheetMetadataPropertyName.MODIFIED_BY, this.emailAddress());
    }

    @Test
    public void testVisitModifiedDateTime() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitModifiedDateTime(final LocalDateTime d) {
                this.visited = d;
            }
        }.accept(SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME, this.dateTime());
    }

    @Test
    public void testVisitNegativeSign() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitNegativeSign(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.NEGATIVE_SIGN, '-');
    }

    @Test
    public void testVisitNamedColor() {
        final SpreadsheetColorName name = SpreadsheetColorName.with("shiny");

        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitNamedColor(final SpreadsheetColorName n, final Color c) {
                assertEquals(name, n, "name");
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.namedColor(name), this.color());
    }

    @Test
    public void testVisitNumberedColor() {
        final int number = 7;

        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitNumberedColor(final int n, final Color c) {
                assertEquals(number, n, "number");
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.numberedColor(number), this.color());
    }

    @Test
    public void testVisitNumberFormatPattern() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitNumberFormatPattern(final SpreadsheetNumberFormatPattern p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.NUMBER_FORMAT_PATTERN, SpreadsheetPattern.parseNumberFormatPattern("#0.0"));
    }

    @Test
    public void testVisitNumberParsePatterns() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitNumberParsePatterns(final SpreadsheetNumberParsePatterns p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.NUMBER_PARSE_PATTERNS, SpreadsheetPattern.parseNumberParsePatterns("#0.0;#0.00"));
    }

    @Test
    public void testVisitPercentageSymbol() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitPercentageSymbol(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.PERCENTAGE_SYMBOL, '.');
    }

    @Test
    public void testVisitPositiveSign() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitPositiveSign(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.POSITIVE_SIGN, '+');
    }

    @Test
    public void testVisitPrecision() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitPrecision(final Integer i) {
                this.visited = i;
            }
        }.accept(SpreadsheetMetadataPropertyName.PRECISION, 123);
    }

    @Test
    public void testVisitRoundingMode() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitRoundingMode(final RoundingMode r) {
                this.visited = r;
            }
        }.accept(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP);
    }

    @Test
    public void testVisitSelectionSpreadsheetCellReference() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitSelection(final SpreadsheetSelection selection) {
                this.visited = selection;
            }
        }.accept(SpreadsheetMetadataPropertyName.SELECTION, SpreadsheetCellReference.parseCellReference("A2:B3"));
    }

    @Test
    public void testVisitSelectionSpreadsheetLabelName() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitSelection(final SpreadsheetSelection selection) {
                this.visited = selection;
            }
        }.accept(SpreadsheetMetadataPropertyName.SELECTION, SpreadsheetExpressionReference.labelName("Label123"));
    }

    @Test
    public void testVisitSpreadsheetId() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitSpreadsheetId(final SpreadsheetId i) {
                this.visited = i;
            }
        }.accept(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.with(123));
    }

    @Test
    public void testVisitStyle() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitStyle(final TextStyle style) {
                this.visited = style;
            }
        }.accept(SpreadsheetMetadataPropertyName.STYLE, SpreadsheetMetadata.NON_LOCALE_DEFAULTS.getOrFail(SpreadsheetMetadataPropertyName.STYLE));
    }

    @Test
    public void testVisitTextFormatPattern() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitTextFormatPattern(final SpreadsheetTextFormatPattern p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN, SpreadsheetPattern.parseTextFormatPattern("@"));
    }

    @Test
    public void testVisitTimeFormatPattern() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitTimeFormatPattern(final SpreadsheetTimeFormatPattern p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.TIME_FORMAT_PATTERN, SpreadsheetPattern.parseTimeFormatPattern("hh:mm"));
    }

    @Test
    public void testVisitTimeParsePatterns() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitTimeParsePatterns(final SpreadsheetTimeParsePatterns p) {
                this.visited = p;
            }
        }.accept(SpreadsheetMetadataPropertyName.TIME_PARSE_PATTERNS, SpreadsheetPattern.parseTimeParsePatterns("hh:mm;hh:mm:ss;hh:mm:ss.000"));
    }

    @Test
    public void testVisitTwoDigitYear() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitTwoDigitYear(final Integer i) {
                this.visited = i;
            }
        }.accept(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 32);
    }

    @Test
    public void testVisitValueSeparator() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitValueSeparator(final Character c) {
                this.visited = c;
            }
        }.accept(SpreadsheetMetadataPropertyName.VALUE_SEPARATOR, '.');
    }

    @Test
    public void testVisitViewportCell() {
        new TestSpreadsheetMetadataVisitor() {
            @Override
            protected void visitViewportCell(final SpreadsheetCellReference cell) {
                this.visited = cell;
            }
        }.accept(SpreadsheetMetadataPropertyName.VIEWPORT_CELL, SpreadsheetCellReference.parseCellReference("B99"));
    }

    private static <T> SpreadsheetMetadata metadata(final SpreadsheetMetadataPropertyName<T> propertyName, final T value) {
        return SpreadsheetMetadata.EMPTY.set(propertyName, value);
    }

    @Override
    public SpreadsheetMetadataVisitor createVisitor() {
        return new TestSpreadsheetMetadataVisitor();
    }

    static class TestSpreadsheetMetadataVisitor extends FakeSpreadsheetMetadataVisitor {

        <T> void accept(final SpreadsheetMetadataPropertyName<T> propertyName, final T value) {
            this.expected = value;

            final SpreadsheetMetadata metadata = metadata(propertyName, value);
            this.accept(metadata);
            assertEquals(this.expected, this.visited);

            new SpreadsheetMetadataVisitor() {
            }.accept(metadata);
        }

        private Object expected;
        Object visited;

        @Override
        protected Visiting startVisit(final SpreadsheetMetadata metadata) {
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final SpreadsheetMetadata metadata) {
        }

        @Override
        protected Visiting startVisit(final SpreadsheetMetadataPropertyName<?> property, final Object value) {
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final SpreadsheetMetadataPropertyName<?> property, final Object value) {
        }
    }

    private Color color() {
        return Color.parse("#123abc");
    }

    private LocalDateTime dateTime() {
        return LocalDateTime.of(2000, 1, 31, 12, 58, 59);
    }

    private EmailAddress emailAddress() {
        return EmailAddress.parse("user@example.com");
    }

    @Override
    public Class<SpreadsheetMetadataVisitor> type() {
        return SpreadsheetMetadataVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }
}
