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

package walkingkooka.spreadsheet.meta;

import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;

import java.util.Locale;
import java.util.Optional;

final class SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns extends SpreadsheetMetadataPropertyName<SpreadsheetDateParsePatterns> {

    /**
     * Singleton
     */
    final static SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns instance() {
        return new SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns();
    }

    /**
     * Private constructor use singleton.
     */
    private SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns() {
        super("date-parse-patterns");
    }

    @Override
    SpreadsheetDateParsePatterns checkValue0(final Object value) {
        return this.checkValueType(value,
                v -> v instanceof SpreadsheetDateParsePatterns);
    }

    @Override
    String expected() {
        return "Date parse patterns";
    }

    @Override
    void accept(final SpreadsheetDateParsePatterns value,
                final SpreadsheetMetadataVisitor visitor) {
        visitor.visitDateParsePatterns(value);
    }

    @Override
    Optional<SpreadsheetDateParsePatterns> extractLocaleValue(final Locale locale) {
        return Optional.of(
                SpreadsheetPattern.dateParsePatternsLocale(locale)
        );
    }

    @Override
    Class<SpreadsheetDateParsePatterns> type() {
        return SpreadsheetDateParsePatterns.class;
    }

    @Override
    String compareToName() {
        return this.value();
    }
}
