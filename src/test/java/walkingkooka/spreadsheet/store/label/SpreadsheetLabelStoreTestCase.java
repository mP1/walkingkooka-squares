package walkingkooka.spreadsheet.store.label;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.store.StoreTesting;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetCellReference;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetLabelName;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetReferenceKind;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class SpreadsheetLabelStoreTestCase<S extends SpreadsheetLabelStore> implements SpreadsheetLabelStoreTesting<S> {

    final static SpreadsheetCellReference REFERENCE = SpreadsheetReferenceKind.ABSOLUTE.column(1).setRow(SpreadsheetReferenceKind.ABSOLUTE.row(2));
    final static SpreadsheetLabelName LABEL = SpreadsheetLabelName.with("label123");

    @Test
    public final void testLoadUnknownFails() {
        this.loadFailCheck(LABEL);
    }

    @Test
    public final void testSaveAndLoad() {
        final S store = this.createStore();

        final SpreadsheetLabelMapping mapping = SpreadsheetLabelMapping.with(LABEL, REFERENCE);
        store.save(mapping);

        assertSame(mapping, store.loadOrFail(LABEL));
    }

    @Test
    public final void testSaveDeleteLoad() {
        final S store = this.createStore();

        final SpreadsheetLabelMapping mapping = SpreadsheetLabelMapping.with(LABEL, REFERENCE);
        store.save(mapping);
        store.delete(mapping.label());

        this.loadFailCheck(store, LABEL);
    }

    @Test
    public final void testCount() {
        final S store = this.createStore();

        final SpreadsheetLabelMapping a = this.mapping("a", 1, 2);
        final SpreadsheetLabelMapping b = this.mapping("b", 4, 8);
        final SpreadsheetLabelMapping c = this.mapping("c", 88, 99);

        store.save(a);
        store.save(b);
        store.save(c);

        this.countAndCheck(store, 3);
    }

    @Test
    public final void testAll() {
        final S store = this.createStore();

        final SpreadsheetLabelMapping a = this.mapping("a", 1, 2);
        final SpreadsheetLabelMapping b = this.mapping("b", 4, 8);
        final SpreadsheetLabelMapping c = this.mapping("c", 88, 99);

        store.save(a);
        store.save(b);
        store.save(c);

        this.allAndCheck(store, a, b, c);
    }

    protected final void allAndCheck(final S store, final SpreadsheetLabelMapping... mappings) {
        final List<SpreadsheetLabelMapping> all = Lists.array();
        all.addAll(store.all());
        assertEquals(Lists.of(mappings), all, () -> "all labels for " + mappings);
    }

    private SpreadsheetCellReference cell(final int column, final int row) {
        return SpreadsheetReferenceKind.ABSOLUTE.column(column)
                .setRow(SpreadsheetReferenceKind.ABSOLUTE.row(row));
    }

    private SpreadsheetLabelMapping mapping(final String label, final int column, final int row) {
        return SpreadsheetLabelMapping.with(SpreadsheetLabelName.with(label), cell(column, row));
    }
}
