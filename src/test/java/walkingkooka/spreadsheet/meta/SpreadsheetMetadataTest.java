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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextJustify;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;
import walkingkooka.tree.text.WordWrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetMetadataTest implements ClassTesting2<SpreadsheetMetadata>,
        HashCodeEqualsDefinedTesting2<SpreadsheetMetadata>,
        JsonNodeMarshallingTesting<SpreadsheetMetadata>,
        ToStringTesting<SpreadsheetMetadata> {

    @Test
    public void testMaxNumberColorConstant() {
        assertEquals(SpreadsheetMetadataPropertyNameNumberedColor.MAX_NUMBER, SpreadsheetMetadata.MAX_NUMBER_COLOR);
    }

    @Test
    public void testSwappablePropertiesConstants() {
        assertArrayEquals(
                SpreadsheetMetadataPropertyName.CONSTANTS.values()
                        .stream()
                        .filter(c -> c instanceof SpreadsheetMetadataPropertyNameCharacter)
                        .toArray(),
                SpreadsheetMetadata.SWAPPABLE_PROPERTIES
        );
    }

    @Test
    public void testSetSelectionAbsoluteCell() {
        this.setAndCheck(
                SpreadsheetMetadataPropertyName.SELECTION,
                SpreadsheetCellReference.parseCellReference("$B$99")
        );
    }

    @Test
    public void testSetSelectionLabel() {
        this.setAndCheck(
                SpreadsheetMetadataPropertyName.SELECTION,
                SpreadsheetCellReference.labelName("Label123")
        );
    }

    @Test
    public void testSetViewportCellAbsolute() {
        this.setAndCheck(
                SpreadsheetMetadataPropertyName.VIEWPORT_CELL,
                SpreadsheetCellReference.parseCellReference("$B$99")
        );
    }

    private <S extends SpreadsheetSelection> void setAndCheck(final SpreadsheetMetadataPropertyName<S> property,
                                                              final S value) {
        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
                .set(property, value);
        assertEquals(value.toRelative(), metadata.getOrFail(property));
    }

    // NON_LOCALE_DEFAULTS..............................................................................................

    @Test
    public void testNonLocaleDefaults() {
        final SpreadsheetMetadata nonLocaleDefaults = SpreadsheetMetadata.NON_LOCALE_DEFAULTS;

        assertNotEquals(SpreadsheetMetadata.EMPTY, nonLocaleDefaults);
        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.DATETIME_OFFSET));

        final Length<?> borderWidth = Length.pixel(1.0);
        final Color borderColor = Color.BLACK;
        final BorderStyle borderStyle = BorderStyle.SOLID;
        final Length<?> none = Length.none();

        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BORDER_LEFT_WIDTH, borderWidth)
                .set(TextStylePropertyName.BORDER_LEFT_COLOR, borderColor)
                .set(TextStylePropertyName.BORDER_LEFT_STYLE, borderStyle)

                .set(TextStylePropertyName.BORDER_TOP_WIDTH, borderWidth)
                .set(TextStylePropertyName.BORDER_TOP_COLOR, borderColor)
                .set(TextStylePropertyName.BORDER_TOP_STYLE, borderStyle)

                .set(TextStylePropertyName.BORDER_RIGHT_WIDTH, borderWidth)
                .set(TextStylePropertyName.BORDER_RIGHT_COLOR, borderColor)
                .set(TextStylePropertyName.BORDER_RIGHT_STYLE, borderStyle)

                .set(TextStylePropertyName.BORDER_BOTTOM_WIDTH, borderWidth)
                .set(TextStylePropertyName.BORDER_BOTTOM_COLOR, borderColor)
                .set(TextStylePropertyName.BORDER_BOTTOM_STYLE, borderStyle)

                .set(TextStylePropertyName.MARGIN_LEFT, none)
                .set(TextStylePropertyName.MARGIN_TOP, none)
                .set(TextStylePropertyName.MARGIN_BOTTOM, none)
                .set(TextStylePropertyName.MARGIN_RIGHT, none)

                .set(TextStylePropertyName.PADDING_LEFT, none)
                .set(TextStylePropertyName.PADDING_TOP, none)
                .set(TextStylePropertyName.PADDING_BOTTOM, none)
                .set(TextStylePropertyName.PADDING_RIGHT, none)

                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.WHITE)
                .set(TextStylePropertyName.COLOR, Color.BLACK)

                .set(TextStylePropertyName.FONT_FAMILY, FontFamily.with("MS Sans Serif"))
                .set(TextStylePropertyName.FONT_SIZE, FontSize.with(11))
                .set(TextStylePropertyName.FONT_STYLE, FontStyle.NORMAL)
                .set(TextStylePropertyName.FONT_VARIANT, FontVariant.NORMAL)
                .set(TextStylePropertyName.HYPHENS, Hyphens.NONE)
                .set(TextStylePropertyName.TEXT_ALIGN, TextAlign.LEFT)
                .set(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.NONE)
                .set(TextStylePropertyName.VERTICAL_ALIGN, VerticalAlign.TOP)
                .set(TextStylePropertyName.WORD_BREAK, WordBreak.NORMAL)
                .set(TextStylePropertyName.WORD_WRAP, WordWrap.NORMAL)

                .set(TextStylePropertyName.HEIGHT, Length.pixel(30.0))
                .set(TextStylePropertyName.WIDTH, Length.pixel(100.0));
        assertEquals(Optional.of(style), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.STYLE));

        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH));
        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.DEFAULT_YEAR));
        assertNotEquals(Optional.of(ExpressionNumberKind.DEFAULT), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND));
        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.PRECISION));
        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.ROUNDING_MODE));
        assertNotEquals(Optional.empty(), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR));

        assertEquals(Optional.of(SpreadsheetCellReference.parseCellReference("A1")), nonLocaleDefaults.get(SpreadsheetMetadataPropertyName.VIEWPORT_CELL));
    }

    // loadFromLocale...................................................................................................

    @Test
    public void testLoadFromLocaleNullFails() {
        assertThrows(SpreadsheetMetadataPropertyValueException.class, () -> SpreadsheetMetadata.EMPTY.loadFromLocale());
    }

    @Test
    public void testLoadFromLocale() {
        assertEquals(SpreadsheetMetadata.EMPTY
                        .set(SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL, "¤")
                        .set(SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN, SpreadsheetPattern.parseDateFormatPattern("dddd, mmmm d, yyyy"))
                        .set(SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERNS, SpreadsheetPattern.parseDateParsePatterns("dddd, mmmm d, yyyy;dddd, mmmm d, yy;dddd, mmmm d;mmmm d, yyyy;mmmm d, yy;mmmm d;mmm d, yyyy;mmm d, yy;mmm d;m/d/yy;m/d/yyyy;m/d"))
                        .set(SpreadsheetMetadataPropertyName.DATETIME_FORMAT_PATTERN, SpreadsheetPattern.parseDateTimeFormatPattern("dddd, mmmm d, yyyy \\a\\t h:mm:ss AM/PM"))
                        .set(SpreadsheetMetadataPropertyName.DATETIME_PARSE_PATTERNS, SpreadsheetPattern.parseDateTimeParsePatterns("dddd, mmmm d, yyyy \\a\\t h:mm:ss AM/PM;dddd, mmmm d, yy \\a\\t h:mm:ss AM/PM;dddd, mmmm d, yy \\a\\t h:mm:ss;dddd, mmmm d, yy \\a\\t h:mm AM/PM;dddd, mmmm d, yyyy \\a\\t h:mm:ss.0 AM/PM;dddd, mmmm d, yyyy \\a\\t h:mm:ss.0;dddd, mmmm d, yyyy \\a\\t h:mm:ss;dddd, mmmm d, yyyy \\a\\t h:mm AM/PM;dddd, mmmm d, yyyy \\a\\t h:mm;dddd, mmmm d, yyyy, h:mm:ss AM/PM;dddd, mmmm d, yy, h:mm:ss AM/PM;dddd, mmmm d, yy, h:mm:ss;dddd, mmmm d, yy, h:mm AM/PM;dddd, mmmm d, yyyy, h:mm:ss.0 AM/PM;dddd, mmmm d, yyyy, h:mm:ss.0;dddd, mmmm d, yyyy, h:mm:ss;dddd, mmmm d, yyyy, h:mm AM/PM;dddd, mmmm d, yyyy, h:mm;dddd, mmmm d, yy, h:mm;mmmm d, yyyy \\a\\t h:mm:ss AM/PM;mmmm d, yy \\a\\t h:mm:ss AM/PM;mmmm d, yy \\a\\t h:mm:ss;mmmm d, yy \\a\\t h:mm AM/PM;mmmm d, yyyy \\a\\t h:mm:ss.0 AM/PM;mmmm d, yyyy \\a\\t h:mm:ss.0;mmmm d, yyyy \\a\\t h:mm:ss;mmmm d, yyyy \\a\\t h:mm AM/PM;mmmm d, yyyy \\a\\t h:mm;mmmm d, yyyy, h:mm:ss AM/PM;mmmm d, yy, h:mm:ss AM/PM;mmmm d, yy, h:mm:ss;mmmm d, yy, h:mm AM/PM;mmmm d, yyyy, h:mm:ss.0 AM/PM;mmmm d, yyyy, h:mm:ss.0;mmmm d, yyyy, h:mm:ss;mmmm d, yyyy, h:mm AM/PM;mmmm d, yyyy, h:mm;mmmm d, yy, h:mm;mmm d, yyyy, h:mm:ss AM/PM;mmm d, yy, h:mm:ss AM/PM;mmm d, yy, h:mm:ss;mmm d, yy, h:mm AM/PM;mmm d, yyyy, h:mm:ss.0 AM/PM;mmm d, yyyy, h:mm:ss.0;mmm d, yyyy, h:mm:ss;mmm d, yyyy, h:mm AM/PM;mmm d, yyyy, h:mm;mmm d, yy, h:mm;m/d/yy, h:mm:ss AM/PM;m/d/yy, h:mm:ss;m/d/yy, h:mm AM/PM;m/d/yyyy, h:mm:ss AM/PM;m/d/yyyy, h:mm:ss.0 AM/PM;m/d/yyyy, h:mm:ss.0;m/d/yyyy, h:mm:ss;m/d/yyyy, h:mm AM/PM;m/d/yy, h:mm:ss.0;m/d/yy, h:mm;m/d/yyyy, h:mm"))
                        .set(SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR, '.')
                        .set(SpreadsheetMetadataPropertyName.EXPONENT_SYMBOL, "E")
                        .set(SpreadsheetMetadataPropertyName.GROUPING_SEPARATOR, ',')
                        .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.ENGLISH)
                        .set(SpreadsheetMetadataPropertyName.NEGATIVE_SIGN, '-')
                        .set(SpreadsheetMetadataPropertyName.NUMBER_FORMAT_PATTERN, SpreadsheetPattern.parseNumberFormatPattern("#,##0.###"))
                        .set(SpreadsheetMetadataPropertyName.NUMBER_PARSE_PATTERNS, SpreadsheetPattern.parseNumberParsePatterns("#,##0.###;#,##0"))
                        .set(SpreadsheetMetadataPropertyName.PERCENTAGE_SYMBOL, '%')
                        .set(SpreadsheetMetadataPropertyName.POSITIVE_SIGN, '+')
                        .set(SpreadsheetMetadataPropertyName.TIME_FORMAT_PATTERN, SpreadsheetPattern.parseTimeFormatPattern("h:mm:ss AM/PM"))
                        .set(SpreadsheetMetadataPropertyName.TIME_PARSE_PATTERNS, SpreadsheetPattern.parseTimeParsePatterns("h:mm:ss AM/PM;h:mm:ss;h:mm:ss.0;h:mm AM/PM;h:mm"))
                        .set(SpreadsheetMetadataPropertyName.VALUE_SEPARATOR, ','),
                SpreadsheetMetadata.EMPTY.set(SpreadsheetMetadataPropertyName.LOCALE, Locale.ENGLISH).loadFromLocale());
    }

    // converter........................................................................................................

    @Test
    public void testNonLocaleDefaultsConverter() {
        SpreadsheetMetadata.NON_LOCALE_DEFAULTS
                .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.forLanguageTag("EN-AU"))
                .loadFromLocale()
                .set(SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN, SpreadsheetPattern.parseTextFormatPattern("@"))
                .converter();
    }

    // HasJsonNodeMarshallContext.......................................................................................

    @Test
    public void testJsonNodeMarshallContext() {
        final SpreadsheetMetadata metadata = this.createObject();
        final JsonNodeMarshallContext marshallContext = metadata.jsonNodeMarshallContext();
        final JsonNodeMarshallContext marshallContext2 = JsonNodeMarshallContexts.basic();

        final BigDecimal bigDecimal = BigDecimal.valueOf(1.25);
        assertEquals(marshallContext.marshall(bigDecimal), marshallContext2.marshall(bigDecimal), () -> "" + bigDecimal);

        final LocalDateTime localDateTime = LocalDateTime.now();

        assertEquals(marshallContext.marshall(localDateTime), marshallContext2.marshall(localDateTime), () -> "" + localDateTime);

        assertEquals(marshallContext.marshall(metadata), marshallContext2.marshall(metadata), () -> "" + metadata);
    }

    // json.............................................................................................................

    @Test
    public void testJsonSelectionCellReference() {
        this.marshallRoundTripSelectionAndCheck(SpreadsheetSelection.parseCellReference("Z99"));
    }

    @Test
    public void testJsonSelectionColumn() {
        this.marshallRoundTripSelectionAndCheck(SpreadsheetColumnReference.parseColumn("B"));
    }

    @Test
    public void testJsonSelectionLabel() {
        this.marshallRoundTripSelectionAndCheck(SpreadsheetSelection.labelName("Label123"));
    }

    @Test
    public void testJsonSelectionRange() {
        this.marshallRoundTripSelectionAndCheck(SpreadsheetSelection.parseRange("A1:B2"));
    }

    @Test
    public void testJsonSelectionRow() {
        this.marshallRoundTripSelectionAndCheck(SpreadsheetColumnReference.parseRow("123"));
    }

    private void marshallRoundTripSelectionAndCheck(final SpreadsheetSelection selection) {
        this.marshallRoundTripTwiceAndCheck(SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.SELECTION,
                selection
        ));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetMetadata.EMPTY
                        .set(this.property1(), this.value1())
                        .set(this.property2(), this.value2()),
                "{\n" +
                        "  \"create-date-time\": \"2000-01-02T12:58:59\",\n" +
                        "  \"creator\": \"user@example.com\"\n" +
                        "}"
        );
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(SpreadsheetMetadata.EMPTY,
                SpreadsheetMetadata.unmarshall(JsonNode.object(), this.unmarshallContext()));
    }

    @Override
    public SpreadsheetMetadata createObject() {
        return this.metadata();
    }

    private SpreadsheetMetadata metadata() {
        return SpreadsheetMetadataNonEmpty.with(
                Maps.of(this.property1(), this.value1()),
                null
        );
    }

    @SuppressWarnings("SameReturnValue")
    private SpreadsheetMetadataPropertyName<LocalDateTime> property1() {
        return SpreadsheetMetadataPropertyName.CREATE_DATE_TIME;
    }

    private LocalDateTime value1() {
        return LocalDateTime.of(2000, 1, 2, 12, 58, 59);
    }

    @SuppressWarnings("SameReturnValue")
    private SpreadsheetMetadataPropertyName<EmailAddress> property2() {
        return SpreadsheetMetadataPropertyName.CREATOR;
    }

    private EmailAddress value2() {
        return EmailAddress.parse("user@example.com");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetMetadata> type() {
        return SpreadsheetMetadata.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public SpreadsheetMetadata unmarshall(final JsonNode from,
                                          final JsonNodeUnmarshallContext context) {
        return SpreadsheetMetadata.unmarshall(from, context);
    }

    @Override
    public SpreadsheetMetadata createJsonNodeMappingValue() {
        return this.createObject();
    }
}
