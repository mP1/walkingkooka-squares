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

package walkingkooka.spreadsheet.format.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class SpreadsheetNumberParsePatternsComponentCurrencyTest extends SpreadsheetNumberParsePatternsComponentTestCase2<SpreadsheetNumberParsePatternsComponentCurrency> {

    @Test
    public void testIncomplete() {
        this.parseFails(CURRENCY.substring(0, 1));
    }

    @Test
    public void testIncomplete2() {
        this.parseFails(CURRENCY.substring(0, 1) + "!");
    }

    @Test
    public void testIncomplete3() {
        this.parseFails(CURRENCY.substring(0, 2));
    }

    @Test
    public void testIncomplete4() {
        this.parseFails(CURRENCY.substring(0, 2) + "!");
    }

    @Test
    public void testToken1() {
        assertEquals(CURRENCY.toUpperCase(), CURRENCY);

        this.parseAndCheck2(
                CURRENCY,
                ""
        );
    }

    @Test
    public void testToken2() {
        assertEquals(CURRENCY.toUpperCase(), CURRENCY);

        this.parseAndCheck2(
                CURRENCY,
                "!"
        );
    }

    @Test
    public void testToken3() {
        assertNotEquals(CURRENCY.toLowerCase(), CURRENCY);

        this.parseAndCheck2(
                CURRENCY.toLowerCase(),
                ""
        );
    }

    @Test
    public void testToken4() {
        assertNotEquals(CURRENCY.toLowerCase(), CURRENCY);

        this.parseAndCheck2(
                CURRENCY.toLowerCase(),
                "!"
        );
    }

    @Test
    public void testCaseUnimportant() {
        this.parseAndCheck2(
                CURRENCY.toLowerCase().substring(0, 1) + CURRENCY.toUpperCase().substring(1),
                ""
        );
    }

    final void parseAndCheck2(final String text,
                              final String textAfter) {
        this.parseAndCheck2(
                text,
                textAfter,
                NEXT_CALLED,
                SpreadsheetParserToken.currencySymbol(text, text)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createComponent(), "$");
    }

    @Override
    SpreadsheetNumberParsePatternsComponentCurrency createComponent() {
        return SpreadsheetNumberParsePatternsComponentCurrency.INSTANCE;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetNumberParsePatternsComponentCurrency> type() {
        return SpreadsheetNumberParsePatternsComponentCurrency.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return "Currency";
    }
}
