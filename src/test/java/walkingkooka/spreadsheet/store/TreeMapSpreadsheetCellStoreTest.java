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

package walkingkooka.spreadsheet.store;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class TreeMapSpreadsheetCellStoreTest extends SpreadsheetCellStoreTestCase<TreeMapSpreadsheetCellStore> {

    // maxColumnWidth...................................................................................................

    @Test
    public void testMaxColumnWidthWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createStore().maxColumnWidth(null));
    }

    @Test
    public void testMaxColumnWidthColumnWithoutCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        maxColumnWidthAndCheck(store, SpreadsheetColumnReference.parseColumn("A"), 0);
    }

    @Test
    public void testMaxColumnWidthWithCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithWidth("C3", 50.0));
        store.save(cellWithWidth("D4", 150.0));

        this.maxColumnWidthAndCheck(store, SpreadsheetColumnReference.parseColumn("C"), 50.0);
    }

    @Test
    public void testMaxColumnWidthWithCellsMissingWidth() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithWidth("C3", 50.0));
        store.save(cellWithWidth("C4", 0));

        this.maxColumnWidthAndCheck(store, SpreadsheetColumnReference.parseColumn("C"), 50.0);
    }

    @Test
    public void testMaxColumnWidthWithSeveralCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithWidth("C3", 50.0));
        store.save(cellWithWidth("C4", 40.0));
        store.save(cellWithWidth("C5", 99.0));
        store.save(cellWithWidth("D4", 150.0));

        this.maxColumnWidthAndCheck(store, SpreadsheetColumnReference.parseColumn("C"), 99.0);
    }

    private SpreadsheetCell cellWithWidth(final String cellReference,
                                          final double pixels) {
        SpreadsheetCell cell = SpreadsheetCell.with(SpreadsheetCellReference.parseCellReference(cellReference), SpreadsheetFormula.with("1+2"));
        if (pixels > 0) {
            cell = cell.setStyle(TextStyle.EMPTY
                    .set(TextStylePropertyName.WIDTH, Length.pixel(pixels)));
        }
        return cell;
    }

    private void maxColumnWidthAndCheck(final TreeMapSpreadsheetCellStore store,
                                        final SpreadsheetColumnReference column,
                                        final double expected) {
        assertEquals(expected,
                store.maxColumnWidth(column),
                () -> "maxColumnWidth of " + column + " store=" + store);
    }

    // maxRowHeight...................................................................................................

    @Test
    public void testMaxRowHeightWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createStore().maxRowHeight(null));
    }

    @Test
    public void testMaxRowHeightRowWithoutCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        maxRowHeightAndCheck(store, SpreadsheetRowReference.parseRow("9"), 0);
    }

    @Test
    public void testMaxRowHeightWithCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithHeight("C3", 50.0));
        store.save(cellWithHeight("D4", 150.0));

        this.maxRowHeightAndCheck(store, SpreadsheetRowReference.parseRow("3"), 50.0);
    }

    @Test
    public void testMaxRowHeightWithCellsMissingWidth() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithHeight("C3", 50.0));
        store.save(cellWithHeight("C4", 0));

        this.maxRowHeightAndCheck(store, SpreadsheetRowReference.parseRow("3"), 50.0);
    }

    @Test
    public void testMaxRowHeightWithSeveralCells() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(cellWithHeight("C3", 50.0));
        store.save(cellWithHeight("D3", 40.0));
        store.save(cellWithHeight("E3", 99.0));
        store.save(cellWithHeight("Z99", 150.0));

        this.maxRowHeightAndCheck(store, SpreadsheetRowReference.parseRow("3"), 99.0);
    }

    private SpreadsheetCell cellWithHeight(final String cellReference,
                                           final double pixels) {
        SpreadsheetCell cell = SpreadsheetCell.with(SpreadsheetCellReference.parseCellReference(cellReference), SpreadsheetFormula.with("1+2"));
        if (pixels > 0) {
            cell = cell.setStyle(TextStyle.EMPTY
                    .set(TextStylePropertyName.HEIGHT, Length.pixel(pixels)));
        }
        return cell;
    }

    private void maxRowHeightAndCheck(final TreeMapSpreadsheetCellStore store,
                                      final SpreadsheetRowReference row,
                                      final double expected) {
        assertEquals(expected,
                store.maxRowHeight(row),
                () -> "maxRowHeight of " + row + " store=" + store);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final TreeMapSpreadsheetCellStore store = this.createStore();
        store.save(SpreadsheetCell.with(SpreadsheetExpressionReference.parseCellReference("A1"), SpreadsheetFormula.with("1+2")));

        this.toStringAndCheck(store, "[A1=1+2]");
    }

    @Override
    public TreeMapSpreadsheetCellStore createStore() {
        return TreeMapSpreadsheetCellStore.create();
    }

    @Override
    public Class<TreeMapSpreadsheetCellStore> type() {
        return TreeMapSpreadsheetCellStore.class;
    }

    // TypeNameTesting..................................................................

    @Override
    public String typeNamePrefix() {
        return TreeMap.class.getSimpleName();
    }
}
