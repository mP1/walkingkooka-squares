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

package walkingkooka.spreadsheet.engine;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.Range;
import walkingkooka.net.http.server.hateos.HateosResourceTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRange;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.map.FromJsonNodeContext;
import walkingkooka.tree.json.map.JsonNodeMappingTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetDeltaTestCase2<D extends SpreadsheetDelta<I>, I> extends SpreadsheetDeltaTestCase<D, I>
        implements HashCodeEqualsDefinedTesting<D>,
        JsonNodeMappingTesting<D>,
        HateosResourceTesting<D>,
        ToStringTesting<D> {

    final static Optional<SpreadsheetId> EMPTY_ID = Optional.empty();

    SpreadsheetDeltaTestCase2() {
        super();
        assertNotEquals(this.id(), this.differentId(), "id() vs differentId() must NOT be equal");
    }

    @Test
    public final void testWindowReadOnly() {
        final SpreadsheetDelta<I> delta = this.createSpreadsheetDelta()
                .setWindow(this.differentWindow());
        final List<SpreadsheetRange> window = delta.window();

        assertThrows(UnsupportedOperationException.class, () -> {
            window.add(SpreadsheetRange.parseRange("A1:A2"));
        });

        this.checkWindow(delta, this.differentWindow());
    }

    @Test
    public final void testCellsReadOnly() {
        final D delta = this.createSpreadsheetDelta();
        final Set<SpreadsheetCell> cells = delta.cells();

        assertThrows(UnsupportedOperationException.class, () -> {
            cells.add(this.a1());
        });

        this.checkCells(delta, this.cells());
    }

    @Test
    public final void testSetIdDifferent() {
        final D delta = this.createSpreadsheetDelta();

        final Optional<SpreadsheetCellReference> reference = Optional.of(SpreadsheetExpressionReference.parseCellReference("M99"));

        final SpreadsheetDelta<Optional<SpreadsheetCellReference>> different = delta.setId(reference);
        assertNotSame(delta, different);
        this.checkId(different, reference);
        this.checkCells(different, this.cells());
        this.checkWindow(different, this.window());

        this.checkId(delta);
        this.checkCells(delta);
        this.checkWindow(delta);
    }

    @Test
    public final void testSetRangeDifferent() {
        final D delta = this.createSpreadsheetDelta();

        final Range<SpreadsheetCellReference> range = SpreadsheetExpressionReference.parseRange("M98:M99").range();

        final SpreadsheetDelta<Range<SpreadsheetCellReference>> different = delta.setRange(range);
        assertNotSame(delta, different);
        this.checkId(different, range);
        this.checkCells(different, this.cells());
        this.checkWindow(different, this.window());

        this.checkId(delta);
        this.checkCells(delta);
        this.checkWindow(delta);
    }

    @Test
    public final void testSetCellsSame() {
        final D delta = this.createSpreadsheetDelta();
        assertSame(delta, delta.setCells(this.cells()));
    }

    @Test
    public final void testSetWindowsSame() {
        final D delta = this.createSpreadsheetDelta();
        assertSame(delta, delta.setWindow(this.window()));
    }

    @Test
    public final void testSetDifferentWindow() {
        final D delta = this.createSpreadsheetDelta();

        final List<SpreadsheetRange> window = this.window0("A1:Z9999");
        assertNotEquals(window, this.window());

        final SpreadsheetDelta<I> different = delta.setWindow(window);

        this.checkId(different);
        this.checkCells(different);
        this.checkWindow(different, window);

        this.checkId(delta);
        this.checkCells(delta);
        this.checkWindow(delta);
    }

    @Test
    public final void testSetDifferentWindowFilters() {
        this.setDifferentWindowFilters("B1:Z99", "Z999:Z9999");
    }

    @Test
    public final void testSetDifferentWindowFilters2() {
        this.setDifferentWindowFilters("A99:A100", "B1:Z99");
    }

    private void setDifferentWindowFilters(final String range1, final String range2) {
        final D delta = this.createSpreadsheetDelta();

        final List<SpreadsheetRange> window = this.window0(range1, range2);
        final SpreadsheetDelta<I> different = delta.setWindow(window);

        this.checkId(different);
        this.checkCells(different, Sets.of(this.b2(), this.c3()));
        this.checkWindow(different, window);

        this.checkId(delta);
        this.checkCells(delta, Sets.of(this.a1(), this.b2(), this.c3()));
        this.checkWindow(delta);
    }

    // equals...........................................................................................................

    @Test
    public final void testDifferentId() {
        final I id = this.differentId();
        assertNotEquals(id, this.id(), "different and id must be un equal");

        this.checkNotEquals(this.createSpreadsheetDelta(id, this.cells()));
    }

    @Test
    public final void testDifferentCells() {
        final Set<SpreadsheetCell> cells = this.differentCells();
        assertNotEquals(this.cells(), cells, "cells and differentCells must be un equal");

        this.checkNotEquals(this.createSpreadsheetDelta(this.id(), cells));
    }

    final D createSpreadsheetDelta() {
        return this.createSpreadsheetDelta(this.id(), this.cells());
    }

    abstract D createSpreadsheetDelta(final I id, final Set<SpreadsheetCell> cells);

    abstract I id();

    abstract I differentId();

    final void checkId(final SpreadsheetDelta<I> delta) {
        this.checkId(delta, this.id());
    }

    abstract List<SpreadsheetRange> window();

    final List<SpreadsheetRange> differentWindow() {
        return this.window0("A1:Z99");
    }

    final List<SpreadsheetRange> window0(final String... range) {
        return Arrays.stream(range)
                .map(SpreadsheetRange::parseRange)
                .collect(Collectors.toList());
    }

    final void checkWindow(final SpreadsheetDelta<I> delta) {
        this.checkWindow(delta, this.window());
    }

    // ClassTesting...............................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HashCodeDefinedTesting............................................................................................

    @Override
    public final D createObject() {
        return this.createSpreadsheetDelta();
    }

    // JsonNodeMappingTesting...........................................................................................

    @Override
    public final D createJsonNodeMappingValue() {
        return this.createSpreadsheetDelta();
    }

    @Override
    public final D fromJsonNode(final JsonNode jsonNode,
                                final FromJsonNodeContext context) {
        return Cast.to(SpreadsheetDelta.fromJsonNode(jsonNode, context));
    }

    // HateosResource...................................................................................................

    @Override
    public final D createHateosResource() {
        return this.createSpreadsheetDelta();
    }
}
