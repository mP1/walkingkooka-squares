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

import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenVisitor;

/**
 * A {@link SpreadsheetFormatParserTokenVisitor} sub classed and used by {@link SpreadsheetFormatter3}.
 */
abstract class TextFormatterSpreadsheetFormatParserTokenVisitor extends SpreadsheetFormatParserTokenVisitor {

    /**
     * Package private to limit sub classing.
     */
    TextFormatterSpreadsheetFormatParserTokenVisitor() {
        super();
    }
}
