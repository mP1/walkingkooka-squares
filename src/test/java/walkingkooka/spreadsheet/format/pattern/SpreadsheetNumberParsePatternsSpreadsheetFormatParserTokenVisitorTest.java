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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatNumberParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserToken;

public final class SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitorTest extends SpreadsheetParsePatternsSpreadsheetFormatParserTokenVisitorTestCase<SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor,
        SpreadsheetFormatNumberParserToken> {

    @Test
    public void testToString() {
        final SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor visitor = new SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor();

        final SpreadsheetFormatNumberParserToken token = SpreadsheetFormatParserToken.number(Lists.of(
                SpreadsheetFormatParserToken.digitZero("0", "0"),
                SpreadsheetFormatParserToken.decimalPoint(".", "."),
                SpreadsheetFormatParserToken.digitZero("0", "0")
        ), "?0.0");

        visitor.accept(token);

        this.toStringAndCheck(visitor, "?0.0");
    }

    @Override
    public SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor createVisitor() {
        return new SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor();
    }

    @Override
    public Class<SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor> type() {
        return SpreadsheetNumberParsePatternsSpreadsheetFormatParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetNumberParsePatterns.class.getSimpleName();
    }
}
