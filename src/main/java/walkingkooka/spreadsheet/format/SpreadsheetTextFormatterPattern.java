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
 * Holds a valid {@link SpreadsheetTextFormatterPattern}.
 */
public final class SpreadsheetTextFormatterPattern implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<String> {
    /**
     * Creates a new {@link SpreadsheetTextFormatterPattern} after checking the value is valid.
     */
    public static SpreadsheetTextFormatterPattern with(final String value) {
        Objects.requireNonNull(value, "value");

        try {
            return new SpreadsheetTextFormatterPattern(value,
                    SpreadsheetFormatParsers.expression()
                            .orFailIfCursorNotEmpty(ParserReporters.basic())
                            .parse(TextCursors.charSequence(value), SpreadsheetFormatParserContexts.basic())
                            .map(SpreadsheetFormatExpressionParserToken.class::cast)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid pattern")));
        } catch (final ParserException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Private ctor use factory
     */
    private SpreadsheetTextFormatterPattern(final String value,
                                            final SpreadsheetFormatExpressionParserToken parserToken) {
        super();
        this.value = value;
        this.parserToken = parserToken;
    }

    // Value............................................................................................................

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    /**
     * Returns the {@link SpreadsheetFormatExpressionParserToken} representation of this pattern.
     */
    public SpreadsheetFormatExpressionParserToken parserToken() {
        return this.parserToken;
    }

    // cached to assist implementing a Visitor
    private final SpreadsheetFormatExpressionParserToken parserToken;

    // HashCodeEqualsDefined............................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SpreadsheetTextFormatterPattern &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final SpreadsheetTextFormatterPattern other) {
        return this.value.equals(other.value);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value;
    }

    // HasJsonNode......................................................................................................

    /**
     * Factory that creates a {@link SpreadsheetTextFormatterPattern} from a {@link JsonNode}.
     */
    static SpreadsheetTextFormatterPattern fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return SpreadsheetTextFormatterPattern.with(node.stringValueOrFail());
    }

    /**
     * Creates a {@link walkingkooka.tree.json.JsonStringNode}.
     */
    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.value);
    }

    static {
        HasJsonNode.register("spreadsheet-text-formatter-pattern",
                SpreadsheetTextFormatterPattern::fromJsonNode,
                SpreadsheetTextFormatterPattern.class);
    }
}
