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

import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.hateos.HateosHandler;
import walkingkooka.spreadsheet.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class that decorates requests performing parameter checks and a few other extras.
 */
abstract class SpreadsheetEngineHateosHandler2<I extends Comparable<I>> extends SpreadsheetEngineHateosHandler implements HateosHandler<I,
        SpreadsheetDelta,
        SpreadsheetDelta> {

    SpreadsheetEngineHateosHandler2(final SpreadsheetEngine engine,
                                    final SpreadsheetEngineContext context) {
        super(engine, context);
    }

    @Override
    public final Optional<SpreadsheetDelta> handle(final I id,
                                                   final Optional<SpreadsheetDelta> delta,
                                                   final Map<HttpRequestAttribute<?>, Object> parameters) {
        Objects.requireNonNull(id, this.id());
        final SpreadsheetDelta delta2 = checkSpreadsheetDelta(delta);
        checkParameters(parameters);

        return Optional.of(this.handle0(id, delta2, parameters)
                .setWindow(delta2.window()));
    }

    abstract String id();

    abstract SpreadsheetDelta handle0(final I id,
                                      final SpreadsheetDelta delta,
                                      final Map<HttpRequestAttribute<?>, Object> parameters);

    @Override
    public final Optional<SpreadsheetDelta> handleCollection(final Range<I> range,
                                                             final Optional<SpreadsheetDelta> delta,
                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
        checkRange(range);
        final SpreadsheetDelta delta2 = checkSpreadsheetDelta(delta);
        checkParameters(parameters);

        return Optional.of(this.handleCollection0(range, delta2, parameters)
                .setWindow(delta2.window()));
    }

    abstract void checkRange(final Range<I> range);

    abstract SpreadsheetDelta handleCollection0(final Range<I> range,
                                                final SpreadsheetDelta delta,
                                                final Map<HttpRequestAttribute<?>, Object> parameters);

    final SpreadsheetDelta checkSpreadsheetDelta(final Optional<SpreadsheetDelta> delta) {
        Objects.requireNonNull(delta, "delta");
        return delta.orElseThrow(() -> new IllegalArgumentException("Required " + SpreadsheetDelta.class.getSimpleName() + " missing."));
    }
}
