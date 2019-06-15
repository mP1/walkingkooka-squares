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

import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;

public final class BasicSpreadsheetParserContextTest implements ClassTesting2<BasicSpreadsheetParserContext>,
        SpreadsheetParserContextTesting<BasicSpreadsheetParserContext> {

    private final static String CURRENCY = "$$";
    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'X';
    private final static char GROUPING = 'G';
    private final static char MINUS = 'M';
    private final static char PERCENTAGE = 'R';
    private final static char PLUS = 'P';
    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Override
    public BasicSpreadsheetParserContext createContext() {
        return BasicSpreadsheetParserContext.with(DecimalNumberContexts.basic(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS, MATH_CONTEXT));
    }

    @Override
    public String currencySymbol() {
        return CURRENCY;
    }

    @Override
    public char decimalPoint() {
        return DECIMAL;
    }

    @Override
    public char exponentSymbol() {
        return EXPONENT;
    }

    @Override
    public char groupingSeparator() {
        return GROUPING;
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char minusSign() {
        return MINUS;
    }

    @Override
    public char percentageSymbol() {
        return PERCENTAGE;
    }

    @Override
    public char plusSign() {
        return PLUS;
    }

    @Override
    public Class<BasicSpreadsheetParserContext> type() {
        return BasicSpreadsheetParserContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
