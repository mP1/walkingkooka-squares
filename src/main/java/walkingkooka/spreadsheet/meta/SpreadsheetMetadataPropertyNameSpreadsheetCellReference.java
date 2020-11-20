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

import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;

import java.util.Locale;
import java.util.Optional;

abstract class SpreadsheetMetadataPropertyNameSpreadsheetCellReference extends SpreadsheetMetadataPropertyName<SpreadsheetCellReference> {

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetMetadataPropertyNameSpreadsheetCellReference(final String name) {
        super(name);
    }

    /**
     * After checking the type force the {@link SpreadsheetCellReference#toRelative()}
     */
    @Override
    final SpreadsheetCellReference checkValue0(final Object value) {
        return this.checkValueType(value,
                v -> v instanceof SpreadsheetCellReference)
                .toRelative();
    }

    @Override
    final String expected() {
        return SpreadsheetCellReference.class.getSimpleName();
    }

    @Override
    final Optional<SpreadsheetCellReference> extractLocaleValue(final Locale locale) {
        return Optional.empty();
    }

    @Override
    final Class<SpreadsheetCellReference> type() {
        return SpreadsheetCellReference.class;
    }
}
