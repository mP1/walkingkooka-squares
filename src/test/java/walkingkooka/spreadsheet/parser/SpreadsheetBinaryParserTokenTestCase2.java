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

package walkingkooka.spreadsheet.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.expression.Expression;

public abstract class SpreadsheetBinaryParserTokenTestCase2<T extends SpreadsheetBinaryParserToken<T>> extends SpreadsheetBinaryParserTokenTestCase<T> {

    @Test
    public final void testToExpression() {
        this.toExpressionAndCheck(this.expressionNode(this.expressionNumberExpression1(), this.expressionNumberExpression2()));
    }

    abstract Expression expressionNode(final Expression left, final Expression right);

    @Override
    final SpreadsheetParserToken leftToken() {
        return this.number1();
    }

    @Override
    final SpreadsheetParserToken rightToken() {
        return this.number2();
    }
}
