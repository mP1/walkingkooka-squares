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

package walkingkooka.spreadsheet.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.hateos.HateosHandlerTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellReference;
import walkingkooka.spreadsheet.SpreadsheetDelta;
import walkingkooka.spreadsheet.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetRange;
import walkingkooka.spreadsheet.engine.FakeSpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContext;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SpreadsheetEngineCopyCellsHateosHandlerTest extends
        SpreadsheetEngineHateosHandlerTestCase2<SpreadsheetEngineCopyCellsHateosHandler, SpreadsheetCellReference, SpreadsheetDelta, SpreadsheetDelta>
        implements HateosHandlerTesting<SpreadsheetEngineCopyCellsHateosHandler,
        SpreadsheetCellReference,
        SpreadsheetDelta,
        SpreadsheetDelta> {

    @Test
    public void testHandleUnsupported() {
        this.handleUnsupported(this.createHandler());
    }

    @Test
    public void testCopy() {
        this.handleCollectionAndCheck(this.collection(),
                this.resource(),
                this.parameters(),
                Optional.of(this.delta()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createHandler(), SpreadsheetEngine.class.getSimpleName() + ".copyCells");
    }

    @Override
    SpreadsheetEngineCopyCellsHateosHandler createHandler(final SpreadsheetEngine engine,
                                                          final Supplier<SpreadsheetEngineContext> context) {
        return SpreadsheetEngineCopyCellsHateosHandler.with(engine, context);
    }

    @Override
    SpreadsheetEngine engine() {
        return new FakeSpreadsheetEngine() {

            @Override
            public SpreadsheetId id() {
                return spreadsheetId();
            }

            @Override
            public SpreadsheetDelta copyCells(final Collection<SpreadsheetCell> from,
                                              final SpreadsheetRange to,
                                              final SpreadsheetEngineContext context) {
                assertEquals(resource().get().cells(), from, "from");
                assertEquals(SpreadsheetExpressionReference.parseRange(TO), to, "to");
                return delta();
            }
        };
    }

    @Override
    SpreadsheetEngineContext engineContext() {
        return null;
    }

    @Override
    public Map<HttpRequestAttribute<?>, Object> parameters() {
        return Maps.of(SpreadsheetEngineCopyCellsHateosHandler.TO, Lists.of(TO));
    }

    private final static String TO = "E1:F2";

    @Override
    public SpreadsheetCellReference id() {
        return SpreadsheetExpressionReference.parseCellReference("A1");
    }

    @Override
    public Range<SpreadsheetCellReference> collection() {
        return SpreadsheetCellReference.parseCellReferenceRange("C1:D2");
    }

    @Override
    public Optional<SpreadsheetDelta> resource() {
        return Optional.of(this.delta());
    }

    @Override
    public Class<SpreadsheetEngineCopyCellsHateosHandler> type() {
        return SpreadsheetEngineCopyCellsHateosHandler.class;
    }
}
