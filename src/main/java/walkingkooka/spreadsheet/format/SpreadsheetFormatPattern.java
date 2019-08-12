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

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatExpressionParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserContexts;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParsers;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;

/**
 * Holds a valid {@link SpreadsheetFormatPattern}.
 */
public final class SpreadsheetFormatPattern implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<SpreadsheetFormatExpressionParserToken> {
    /**
     * Creates a new {@link SpreadsheetFormatPattern} after checking the value is valid.
     */
    public static SpreadsheetFormatPattern parse(final String value) {
        Objects.requireNonNull(value, "value");

        try {
            return new SpreadsheetFormatPattern(SpreadsheetFormatParsers.expression()
                            .orFailIfCursorNotEmpty(ParserReporters.basic())
                            .parse(TextCursors.charSequence(value), SpreadsheetFormatParserContexts.basic())
                            .map(SpreadsheetFormatExpressionParserToken.class::cast)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid pattern")));
        } catch (final ParserException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    public static SpreadsheetFormatPattern with(final SpreadsheetFormatExpressionParserToken value) {
        Objects.requireNonNull(value, "value");

        return new SpreadsheetFormatPattern(value);
    }

    /**
     * Private ctor use factory
     */
    private SpreadsheetFormatPattern(final SpreadsheetFormatExpressionParserToken value) {
        super();
        this.value = value;
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetFormatExpressionParserToken value() {
        return this.value;
    }

    private final SpreadsheetFormatExpressionParserToken value;

    // HashCodeEqualsDefined............................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SpreadsheetFormatPattern &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final SpreadsheetFormatPattern other) {
        return this.value.equals(other.value);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value.text();
    }

    // HasJsonNode......................................................................................................

    /**
     * Factory that creates a {@link SpreadsheetFormatPattern} from a {@link JsonNode}.
     */
    static SpreadsheetFormatPattern fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return SpreadsheetFormatPattern.parse(node.stringValueOrFail());
    }

    /**
     * Creates a {@link walkingkooka.tree.json.JsonStringNode}.
     */
    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.value.text());
    }

    static {
        HasJsonNode.register("spreadsheet-text-formatter-pattern",
                SpreadsheetFormatPattern::fromJsonNode,
                SpreadsheetFormatPattern.class);
    }
}