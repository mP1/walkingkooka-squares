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

package walkingkooka.spreadsheet.context.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.compare.Range;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.hateos.HateosHandler;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.context.SpreadsheetContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Map;
import java.util.Optional;

public abstract class SpreadsheetContextSpreadsheetMetadataStoreHateosHandlerTestCase<H extends SpreadsheetContextSpreadsheetMetadataStoreHateosHandler>
        extends SpreadsheetContextHateosHandlerTestCase2<H,
        SpreadsheetId,
        SpreadsheetMetadata,
        SpreadsheetMetadata> {

    // handle...........................................................................................................

    @Test
    public final void testHandleNullParametersFails() {
        this.handleFails(this.id(),
                Optional.empty(),
                null,
                NullPointerException.class);
    }

    // handleCollection.................................................................................................

    @Test
    public final void testHandleCollectionIdFails() {
        this.handleCollectionFails(this.collection(),
                this.collectionResource(),
                this.parameters(),
                UnsupportedOperationException.class);
    }

    // helpers..........................................................................................................

    @Override
    abstract H createHandler(final SpreadsheetContext context);

    @Override
    public final Optional<SpreadsheetId> id() {
        return Optional.of(this.spreadsheetId());
    }

    @Override
    public final Range<SpreadsheetId> collection() {
        return Range.singleton(this.spreadsheetId());
    }

    final SpreadsheetMetadata metadata() {
        return SpreadsheetMetadata.with(Map.of(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, this.spreadsheetId(),
                SpreadsheetMetadataPropertyName.CREATOR, EmailAddress.parse("user@example.com")));
    }

    final SpreadsheetId spreadsheetId() {
        return SpreadsheetId.with(0x1234);
    }

    @Override
    public final Optional<SpreadsheetMetadata> resource() {
        return Optional.empty();
    }

    @Override
    public final Optional<SpreadsheetMetadata> collectionResource() {
        return Optional.empty();
    }

    @Override
    public final Map<HttpRequestAttribute<?>, Object> parameters() {
        return HateosHandler.NO_PARAMETERS;
    }
}
