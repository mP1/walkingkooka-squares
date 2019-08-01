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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDateParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserToken;

public final class SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitorTest extends SpreadsheetPatternsSpreadsheetFormatParserTokenVisitorTestCase<SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor,
        SpreadsheetFormatDateParserToken> {

    @Test
    public void testToString() {
        final SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor visitor = new SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor();

        final SpreadsheetFormatDateParserToken token = SpreadsheetFormatParserToken.date(Lists.of(
                SpreadsheetFormatParserToken.day("d", "d"),
                SpreadsheetFormatParserToken.monthOrMinute("m", "m"),
                SpreadsheetFormatParserToken.year("yyyy", "yyyy")
        ), "dmyyyy");

        visitor.accept(token);

        this.toStringAndCheck(visitor, "dmyyyy");
    }

    @Override
    public SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor createVisitor() {
        return new SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor();
    }

    @Override
    public Class<SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor> type() {
        return SpreadsheetDatePatternsSpreadsheetFormatParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetDatePatterns.class.getSimpleName();
    }
}
