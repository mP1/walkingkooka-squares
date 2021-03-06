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

package walkingkooka.spreadsheet.reference;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.Range;
import walkingkooka.spreadsheet.parser.SpreadsheetColumnReferenceParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.spreadsheet.parser.SpreadsheetRowReferenceParserToken;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 * Captures the common features shared by a row or column.
 */
abstract public class SpreadsheetColumnOrRowReference extends SpreadsheetSelection implements Value<Integer> {

    /**
     * Creates a new {@link SpreadsheetColumn}
     */
    public static SpreadsheetColumnReference column(final int value, final SpreadsheetReferenceKind referenceKind) {
        return SpreadsheetColumnReference.with(value, referenceKind);
    }

    /**
     * Creates a new {@link SpreadsheetRowReference}
     */
    public static SpreadsheetRowReference row(final int value, final SpreadsheetReferenceKind referenceKind) {
        return SpreadsheetRowReference.with(value, referenceKind);
    }

    /**
     * Parsers a range of columns.
     */
    public static Range<SpreadsheetColumnReference> parseColumnRange(final String text) {
        return Range.parse(text, SpreadsheetParsers.RANGE_SEPARATOR.character(), SpreadsheetColumnReference::parseColumn);
    }

    /**
     * Parsers the text expecting a valid {@link SpreadsheetColumnReference} or fails.
     */
    public static SpreadsheetColumnReference parseColumn(final String text) {
        return parse0(text, COLUMN_PARSER, SpreadsheetColumnReferenceParserToken.class).value();
    }

    /**
     * Leverages the {@link SpreadsheetParsers#column()} combined with an error reporter.
     */
    private static final Parser<SpreadsheetParserContext> COLUMN_PARSER = SpreadsheetParsers.column().orReport(ParserReporters.basic());

    /**
     * Parsers a range of rows.
     */
    public static Range<SpreadsheetRowReference> parseRowRange(final String text) {
        return Range.parse(text, SpreadsheetParsers.RANGE_SEPARATOR.character(), SpreadsheetRowReference::parseRow);
    }

    /**
     * Parsers the text expecting a valid {@link SpreadsheetRowReference} or fails.
     */
    public static SpreadsheetRowReference parseRow(final String text) {
        return parse0(text, ROW_PARSER, SpreadsheetRowReferenceParserToken.class).value();
    }

    /**
     * Leverages the {@link SpreadsheetParsers#row()} combined with an error reporter.
     */
    private static final Parser<SpreadsheetParserContext> ROW_PARSER = SpreadsheetParsers.row().orReport(ParserReporters.basic());

    /**
     * Parsers the text expecting a valid {@link SpreadsheetRowReference} or fails.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static <T extends SpreadsheetParserToken> T parse0(final String text,
                                                       final Parser<SpreadsheetParserContext> parser,
                                                       final Class<T> type) {
        try {
            return parser.parse(TextCursors.charSequence(text), SpreadsheetReferenceSpreadsheetParserContext.INSTANCE)
                    .get()
                    .cast(type);
        } catch (final ParserException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    final static int CACHE_SIZE = 100;

    /**
     * Fills an array with what will become a cache of {@link SpreadsheetColumnOrRowReference}.
     */
    static <R extends SpreadsheetColumnOrRowReference> R[] fillCache(final IntFunction<R> reference, final R[] array) {
        for (int i = 0; i < CACHE_SIZE; i++) {
            array[i] = reference.apply(i);
        }

        return array;
    }

    static void checkReferenceKind(final SpreadsheetReferenceKind referenceKind) {
        Objects.requireNonNull(referenceKind, "referenceKind");
    }

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetColumnOrRowReference(final int value, final SpreadsheetReferenceKind referenceKind) {
        this.value = value;
        this.referenceKind = referenceKind;
    }

    /**
     * Only returns true if this is the first column or row.
     */
    public final boolean isFirst() {
        return this.value == 0;
    }

    /**
     * Only returns true if this is the last column or row.
     */
    public final boolean isLast() {
        return this.value == this.max();
    }

    /**
     * Adds a delta to the value and returns an instance with the result.
     */
    abstract SpreadsheetColumnOrRowReference add(final int value);

    final SpreadsheetColumnOrRowReference add0(final int value) {
        return 0 == value ?
                this :
                this.setValue(this.value + value);
    }

    /**
     * A saturated adds of delta to the value and returns an instance with the result.
     */
    abstract SpreadsheetColumnOrRowReference addSaturated(final int value);

    final SpreadsheetColumnOrRowReference addSaturated0(final int value) {
        return this.setValue(
                Math.min(
                        Math.max(this.value + value, 0),
                        this.max()
                )
        );
    }

    /**
     * Returns the maximum valid value, this is used during {@link #addSaturated(int)}
     */
    abstract int max();

    abstract SpreadsheetColumnOrRowReference setValue(final int value);

    @Override
    public final Integer value() {
        return this.value;
    }

    final int value;

    public final SpreadsheetReferenceKind referenceKind() {
        return this.referenceKind;
    }

    abstract SpreadsheetColumnOrRowReference setReferenceKind(final SpreadsheetReferenceKind referenceKind);

    final SpreadsheetColumnOrRowReference setReferenceKind0(final SpreadsheetReferenceKind referenceKind) {
        checkReferenceKind(referenceKind);

        return this.referenceKind == referenceKind ?
                this :
                this.replaceReferenceKind(referenceKind);
    }

    private final SpreadsheetReferenceKind referenceKind;

    /**
     * Unconditionally creates a new {@link SpreadsheetColumnOrRowReference} with the given {@link SpreadsheetReferenceKind}.
     */
    abstract SpreadsheetColumnOrRowReference replaceReferenceKind(final SpreadsheetReferenceKind referenceKind);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.value, this.referenceKind);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(Object other);

    private boolean equals0(final SpreadsheetColumnOrRowReference other) {
        return this.equalsValue(other) &&
                this.referenceKind == other.referenceKind;
    }

    final boolean equalsIgnoreReferenceKind0(final SpreadsheetColumnOrRowReference other) {
        return this == other ||
                (null != other && this.equalsValue(Cast.to(other)));
    }

    final boolean equalsValue(final SpreadsheetColumnOrRowReference other) {
        return this.value == other.value;
    }

    @Override
    abstract public String toString();

    static void checkOther(final SpreadsheetColumnOrRowReference other) {
        Objects.requireNonNull(other, "other");
    }

    // JsonNodeContext..................................................................................................

    /**
     * Expects a {@link JsonString} and returns a {@link SpreadsheetColumnReference}.
     */
    static SpreadsheetColumnReference unmarshallColumn(final JsonNode from,
                                                       final JsonNodeUnmarshallContext context) {
        return SpreadsheetColumnOrRowReference.parseColumn(from.stringOrFail());
    }

    /**
     * Expects a {@link JsonString} and returns a {@link SpreadsheetRowReference}.
     */
    static SpreadsheetRowReference unmarshallRow(final JsonNode from,
                                                 final JsonNodeUnmarshallContext context) {
        return SpreadsheetColumnOrRowReference.parseRow(from.stringOrFail());
    }

    final JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static {
        register(
                SpreadsheetColumnReference::unmarshallColumn,
                SpreadsheetColumnReference::marshall,
                SpreadsheetColumnReference.class
        );

        //noinspection StaticInitializerReferencesSubClass
        register(
                SpreadsheetRowReference::unmarshallRow,
                SpreadsheetRowReference::marshall,
                SpreadsheetRowReference.class
        );
    }

    private static <RR extends SpreadsheetColumnOrRowReference> void register(
            final BiFunction<JsonNode, JsonNodeUnmarshallContext, RR> from,
            final BiFunction<RR, JsonNodeMarshallContext, JsonNode> to,
            final Class<RR> type) {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(type),
                from,
                to,
                type
        );
    }
}
