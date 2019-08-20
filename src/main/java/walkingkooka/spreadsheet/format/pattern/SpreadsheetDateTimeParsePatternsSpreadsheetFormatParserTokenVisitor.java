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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatAmPmParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatCurrencyParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDateParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDateTimeParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDigitParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatDigitSpaceParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatExponentSymbolParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatNumberParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatPercentParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatTextParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatThousandsParserToken;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatTimeParserToken;
import walkingkooka.visit.Visiting;

import java.util.List;

final class SpreadsheetDateTimeParsePatternsSpreadsheetFormatParserTokenVisitor extends SpreadsheetParsePatternsSpreadsheetFormatParserTokenVisitor<SpreadsheetFormatDateTimeParserToken> {

    static SpreadsheetDateTimeParsePatternsSpreadsheetFormatParserTokenVisitor with() {
        return new SpreadsheetDateTimeParsePatternsSpreadsheetFormatParserTokenVisitor();
    }

    SpreadsheetDateTimeParsePatternsSpreadsheetFormatParserTokenVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final SpreadsheetFormatDateParserToken token) {
        return this.failInvalid(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetFormatDateTimeParserToken token) {
        this.ampm = false;
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatDateTimeParserToken token) {
        this.addToken(token);
        this.ampms.add(this.ampm);
    }

    final List<Boolean> ampms = Lists.array();

    @Override
    protected Visiting startVisit(final SpreadsheetFormatNumberParserToken token) {
        return this.failInvalid(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetFormatTextParserToken token) {
        return this.failInvalid(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetFormatTimeParserToken token) {
        return this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatAmPmParserToken token) {
        this.ampm = true;
    }

    /**
     * When true 12 hour patterns must be used.
     */
    boolean ampm = false;

    @Override
    protected void visit(final SpreadsheetFormatCurrencyParserToken token) {
        this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatDigitParserToken token) {
        this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatDigitSpaceParserToken token) {
        this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatExponentSymbolParserToken token) {
        this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatPercentParserToken token) {
        this.failInvalid(token);
    }

    @Override
    protected void visit(final SpreadsheetFormatThousandsParserToken token) {
        this.failInvalid(token);
    }

    @Override
    void text(final String text) {
        // nop
    }
}
