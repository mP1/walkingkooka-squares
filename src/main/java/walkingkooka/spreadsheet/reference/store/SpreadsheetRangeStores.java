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

package walkingkooka.spreadsheet.reference.store;

import walkingkooka.reflect.PublicStaticHelper;

/**
 * Contains many factory methods for a variety of {@link SpreadsheetRangeStore} implementations.
 */
public final class SpreadsheetRangeStores implements PublicStaticHelper {

    /**
     * {@see FakeSpreadsheetRangeStore}
     */
    public static <V> SpreadsheetRangeStore<V> fake() {
        return new FakeSpreadsheetRangeStore<>();
    }

    /**
     * {@see ReadOnlySpreadsheetRangeStore}
     */
    public static <V> SpreadsheetRangeStore<V> readOnly(final SpreadsheetRangeStore<V> store) {
        return ReadOnlySpreadsheetRangeStore.with(store);
    }

    /**
     * {@see TreeMapSpreadsheetRangeStore}
     */
    public static <V> SpreadsheetRangeStore<V> treeMap() {
        return TreeMapSpreadsheetRangeStore.create();
    }

    /**
     * Stop creation
     */
    private SpreadsheetRangeStores() {
        throw new UnsupportedOperationException();
    }
}
