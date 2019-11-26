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

package walkingkooka.spreadsheet.engine;

import walkingkooka.Either;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.test.Fake;
import walkingkooka.tree.expression.Expression;

import java.util.Objects;
import java.util.Optional;

public class FakeSpreadsheetEngineContext implements SpreadsheetEngineContext, Fake {

    @Override
    public SpreadsheetParserToken parseFormula(final String formula) {
        Objects.requireNonNull(formula, "formula");
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(final Expression node) {
        Objects.requireNonNull(node, "node");
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Either<T, String> convert(Object value, Class<T> target) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(target, "target");
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatter parsePattern(final String pattern) {
        Objects.requireNonNull(pattern, "pattern");
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatter defaultSpreadsheetFormatter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetText> format(final Object value,
                                            final SpreadsheetFormatter formatter) {
        throw new UnsupportedOperationException();
    }
}
