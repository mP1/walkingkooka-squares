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

package walkingkooka.spreadsheet;

import walkingkooka.spreadsheet.parser.SpreadsheetCellReference;
import walkingkooka.spreadsheet.store.label.SpreadsheetLabelStore;
import walkingkooka.spreadsheet.store.range.SpreadsheetRangeStore;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.type.PublicStaticHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class SpreadsheetBiConsumers implements PublicStaticHelper {

    /**
     * {@see ExpressionReferenceSpreadsheetCellReferencesBiConsumer}
     */
    public static BiConsumer<ExpressionReference, Consumer<SpreadsheetCellReference>> expressionReferenceSpreadsheetCellReferences(final SpreadsheetLabelStore labelStore,
                                                                                                                                   final SpreadsheetRangeStore<SpreadsheetCellReference> rangeToCellStore) {
        return ExpressionReferenceSpreadsheetCellReferencesBiConsumer.with(labelStore, rangeToCellStore);
    }

    private SpreadsheetBiConsumers() {
        throw new UnsupportedOperationException();
    }
}
