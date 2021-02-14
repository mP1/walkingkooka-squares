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

package walkingkooka.spreadsheet.meta.store;

import org.junit.jupiter.api.Test;

import java.util.TreeMap;

public final class TreeMapSpreadsheetMetadataStoreTest extends SpreadsheetMetadataStoreTestCase<TreeMapSpreadsheetMetadataStore> {

    @Test
    public void testToString() {
        final TreeMapSpreadsheetMetadataStore store = this.createStore();
        store.save(this.metadata(1, "user1@example.com"));
        store.save(this.metadata(2, "user2@example.com"));

        this.toStringAndCheck(store, "[{spreadsheet-id=1, creator=user1@example.com, create-date-time=1999-12-31T12:58:59, locale=en_AU, modified-by=modified@example.com, modified-date-time=2000-01-02T12:58:59}, {spreadsheet-id=2, creator=user2@example.com, create-date-time=1999-12-31T12:58:59, locale=en_AU, modified-by=modified@example.com, modified-date-time=2000-01-02T12:58:59}]");
    }

    @Override
    public TreeMapSpreadsheetMetadataStore createStore() {
        return TreeMapSpreadsheetMetadataStore.create();
    }

    @Override
    public Class<TreeMapSpreadsheetMetadataStore> type() {
        return TreeMapSpreadsheetMetadataStore.class;
    }

    // TypeNameTesting..................................................................

    @Override
    public String typeNamePrefix() {
        return TreeMap.class.getSimpleName();
    }
}
