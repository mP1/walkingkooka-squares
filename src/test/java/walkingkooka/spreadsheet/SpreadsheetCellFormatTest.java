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

package walkingkooka.spreadsheet;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFormatTest implements ClassTesting2<SpreadsheetCellFormat>,
        HashCodeEqualsDefinedTesting2<SpreadsheetCellFormat>,
        JsonNodeMarshallingTesting<SpreadsheetCellFormat>,
        ToStringTesting<SpreadsheetCellFormat> {

    private final static String PATTERN = "abc123";
    private final static Optional<SpreadsheetFormatter> FORMATTER = Optional.of(SpreadsheetFormatters.fake());

    @Test
    public void testWithNullPatternFails() {
        assertThrows(NullPointerException.class, () -> SpreadsheetCellFormat.with(null));
    }

    @Test
    public void testWithEmptyPattern() {
        final String pattern = "";
        final SpreadsheetCellFormat format = SpreadsheetCellFormat.with(pattern);
        this.check(format, pattern, SpreadsheetCellFormat.NO_FORMATTER);
    }

    @Test
    public void testWith() {
        final SpreadsheetCellFormat format = SpreadsheetCellFormat.with(PATTERN);
        this.check(format, PATTERN, SpreadsheetCellFormat.NO_FORMATTER);
    }

    // setPattern...........................................................

    @Test
    public void testSetPatternNullFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().setPattern(null));
    }

    @Test
    public void testSetPatternSame() {
        final SpreadsheetCellFormat format = this.createObject();
        assertSame(format, format.setPattern(PATTERN));
    }

    @Test
    public void testSetPatternDifferentClearsFormatter() {
        final String differentPattern = "different";
        final SpreadsheetCellFormat format = this.createObject();
        final SpreadsheetCellFormat different = format.setPattern(differentPattern);
        assertNotSame(format, different);
        this.check(different, differentPattern, SpreadsheetCellFormat.NO_FORMATTER);
    }

    // setFormatter...........................................................

    @SuppressWarnings("OptionalAssignedToNull")
    @Test
    public void testSetFormatterNullFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().setFormatter(null));
    }

    @Test
    public void testSetFormatterSame() {
        final SpreadsheetCellFormat format = this.createObject();
        assertSame(format, format.setFormatter(FORMATTER));
    }

    @Test
    public void testSetFormatterDifferent() {
        this.setFormatterDifferentAndCheck(Optional.of(SpreadsheetFormatters.general()));
    }

    @Test
    public void testSetFormatterDifferentWithout() {
        this.setFormatterDifferentAndCheck(SpreadsheetCellFormat.NO_FORMATTER);
    }

    private void setFormatterDifferentAndCheck(final Optional<SpreadsheetFormatter> differentFormatter) {
        final SpreadsheetCellFormat format = this.createObject();
        final SpreadsheetCellFormat different = format.setFormatter(differentFormatter);
        assertNotSame(format, different);
        this.check(different, PATTERN, differentFormatter);
    }

    private void check(final SpreadsheetCellFormat format,
                       final String pattern,
                       final Optional<SpreadsheetFormatter> formatter) {
        assertEquals(pattern, format.pattern(), "pattern");
        assertEquals(formatter, format.formatter(), "formatter");
    }

    // equals..................................................................................

    @Test
    public void testEqualsBothNoFormatter() {
        this.checkEqualsAndHashCode(this.withoutFormatter(), this.withoutFormatter());
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(SpreadsheetCellFormat.with("different").setFormatter(FORMATTER));
    }

    @Test
    public void testEqualsDifferentFormatter() {
        this.checkNotEquals(SpreadsheetCellFormat.with(PATTERN).setFormatter(Optional.of(SpreadsheetFormatters.general())));
    }

    @Test
    public void testEqualstDifferentNoFormatter() {
        this.checkNotEquals(this.withoutFormatter());
    }

    // JsonNodeMarshallingTesting.......................................................................................

    @Test
    public void testJsonNodeUnmarshallString() {
        this.unmarshallAndCheck(JsonNode.string(PATTERN),
                SpreadsheetCellFormat.with(PATTERN));
    }

    @Test
    public void testJsonNodeMarshall() {
        this.marshallAndCheck(this.createObject(), JsonNode.string(PATTERN));
    }

    @Test
    public void testJsonNodeMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    // toString.........................................................................................................

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(),
                CharSequences.quote(PATTERN) + " " + FORMATTER.get());
    }

    @Test
    public void testToStringWithoutFormatter() {
        assertEquals(CharSequences.quote(PATTERN).toString(),
                SpreadsheetCellFormat.with(PATTERN).toString());
    }

    @Override
    public SpreadsheetCellFormat createObject() {
        return SpreadsheetCellFormat.with(PATTERN, FORMATTER);
    }

    private SpreadsheetCellFormat withoutFormatter() {
        return SpreadsheetCellFormat.with(PATTERN);
    }

    @Override
    public Class<SpreadsheetCellFormat> type() {
        return SpreadsheetCellFormat.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public SpreadsheetCellFormat createJsonNodeMappingValue() {
        return SpreadsheetCellFormat.with(PATTERN);
    }

    @Override
    public SpreadsheetCellFormat unmarshall(final JsonNode jsonNode,
                                            final JsonNodeUnmarshallContext context) {
        return SpreadsheetCellFormat.unmarshall(jsonNode, context);
    }
}
