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

import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDateTimeParserToken;

import java.time.LocalDateTime;

/**
 * Holds a valid {@link SpreadsheetDateTimeFormatPattern}.
 */
public final class SpreadsheetDateTimeFormatPattern extends SpreadsheetFormatPattern<SpreadsheetFormatDateTimeParserToken> {

    /**
     * Factory that creates a {@link SpreadsheetDateTimeFormatPattern} from the given token.
     */
    static SpreadsheetDateTimeFormatPattern with(final SpreadsheetFormatDateTimeParserToken token) {
        SpreadsheetDateTimeFormatPatternSpreadsheetFormatParserTokenVisitor.with()
                .startAccept(token);

        return new SpreadsheetDateTimeFormatPattern(token);
    }

    /**
     * Private ctor use factory
     */
    private SpreadsheetDateTimeFormatPattern(final SpreadsheetFormatDateTimeParserToken token) {
        super(token);
    }

    // HasSpreadsheetFormatter..........................................................................................

    /**
     * Factory that lazily creates a {@link SpreadsheetFormatter}
     */
    @Override
    SpreadsheetFormatter createFormatter() {
        return SpreadsheetFormatters.dateTime(this.value, v -> v instanceof LocalDateTime);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof SpreadsheetDateTimeFormatPattern;
    }
}
