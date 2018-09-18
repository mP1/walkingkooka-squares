package walkingkooka.spreadsheet;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.store.cell.SpreadsheetCellStore;
import walkingkooka.spreadsheet.store.cell.SpreadsheetCellStores;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetCellReference;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetColumnReference;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetReferenceKind;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetRowReference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SpreadsheetRangeTest extends PublicClassTestCase<SpreadsheetRange> {

    private final static int COLUMN1 = 10;
    private final static int ROW1 = 11;
    private final static int COLUMN2 = 20;
    private final static int ROW2 = 21;

    @Test(expected = NullPointerException.class)
    public void testWithNullBeginFails() {
        SpreadsheetRange.with(null, this.cell());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullEndFails() {
        SpreadsheetRange.with(this.cell(), null);
    }

    @Test
    public void testWith() {
        final SpreadsheetCellReference begin =this.cell(1, 2);
        final SpreadsheetCellReference end =this.cell(3, 4);

        final SpreadsheetRange range = SpreadsheetRange.with(begin, end);
        assertSame("begin", begin, range.begin());
        assertSame("end", end, range.end());
    }

    @Test
    public void testWith2() {
        final int column1 = 99;
        final int row1 = 2;
        final int column2 = 3;
        final int row2 = 4;

        final SpreadsheetRange range = this.range(column1, row1, column2, row2);
        this.check(range, column2, row1, column1, row2, 99-3, 4-2);
    }

    @Test
    public void testWith3() {
        final int column1 = 1;
        final int row1 = 99;
        final int column2 = 3;
        final int row2 = 4;

        final SpreadsheetRange range = this.range(column1, row1, column2, row2);
        this.check(range, column1, row2, column2, row1, 3-1, 99-4);
    }

    @Test
    public void testWith4() {
        final int column1 = 88;
        final int row1 = 99;
        final int column2 = 3;
        final int row2 = 4;

        final SpreadsheetRange range = this.range(column1, row1, column2, row2);
        this.check(range, column2, row2, column1, row1, 88-3, 99-4);
    }

    // stream.................................................................................................
    
    @Test
    public void testColumnStream() {
        final SpreadsheetRange range = this.range(5, 10, 8, 10);

        this.checkStream(range,
                range.columnStream(),
                this.column(5), this.column(6), this.column(7));
    }

    @Test
    public void testColumnStreamFilterAndMapAndCollect() {
        final SpreadsheetRange range = this.range(5, 10, 8, 10);
        this.checkStream(range,
                range.columnStream()
                        .map( c -> c.value())
                        .filter(c -> c >= 6),
                6, 7);
    }

    @Test
    public void testRowStream() {
        final SpreadsheetRange range = this.range(10, 5, 10, 8);

        this.checkStream(range,
                range.rowStream(),
                this.row(5), this.row(6), this.row(7));
    }

    @Test
    public void testRowStreamFilterAndMapAndCollect() {
        final SpreadsheetRange range = this.range(5, 10, 8, 20);
        this.checkStream(range,
                range.rowStream()
                        .map( r -> r.value())
                        .filter(r -> r < 13),
                10, 11, 12);
    }

    @Test
    public void testCellStream() {
        final SpreadsheetRange range = this.range(3, 7, 6, 11);

        this.checkStream(
                range,
                range.cellStream(),
                this.cell(3, 7), this.cell(4, 7), this.cell(5, 7),
                this.cell(3, 8), this.cell(4, 8), this.cell(5, 8),
                this.cell(3, 9), this.cell(4, 9), this.cell(5, 9),
                this.cell(3, 10), this.cell(4, 10), this.cell(5, 10));
    }

    @Test
    public void testCellStreamFilterAndMapAndCollect() {
        final SpreadsheetRange range = this.range(5, 10, 8, 20);
        this.checkStream(range,
                range.cellStream()
                        .filter(cell -> cell.column().value() == 5 && cell.row().value() < 13),
                this.cell(5, 10), this.cell(5, 11), this.cell(5, 12));
    }

    private <T> void checkStream(final SpreadsheetRange range, final Stream<?> stream, final Object...expected){
        final List<Object> actual = stream.collect(Collectors.toList());
        assertEquals(range.toString(), Lists.of(expected), actual);
    }

    // clear....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testClearWithNullStoreFails() {
        this.range().clear(null);
    }

    @Test
    public void testClear() {
        final SpreadsheetCellStore store = SpreadsheetCellStores.basic();

        final SpreadsheetCell a = spreadsheetCell(1, 1);
        final SpreadsheetCell b = spreadsheetCell(2, 2);
        final SpreadsheetCell c = spreadsheetCell(3, 10);
        final SpreadsheetCell d = spreadsheetCell(4, 14);
        final SpreadsheetCell e = spreadsheetCell(5, 15);

        store.save(a);
        store.save(b);
        store.save(c);
        store.save(d);
        store.save(e);

        this.range(2, 2, 4, 11).clear(store);

        assertEquals("store record count", 3, store.count()); // a,d,e
        assertEquals(Optional.empty(), store.load(b.reference()));
        assertEquals(Optional.empty(), store.load(c.reference()));
    }

    private SpreadsheetCell spreadsheetCell(final int column, final int row) {
        return SpreadsheetCell.with(this.cell(column, row), SpreadsheetFormula.with(column + "+" + row));
    }

    //helper.................................................................................................

    private SpreadsheetRange range() {
        return this.range(this.begin(), this.end());
    }

    private SpreadsheetCellReference begin() {
        return this.cell(COLUMN1, ROW1);
    }

    private SpreadsheetCellReference end() {
        return this.cell(COLUMN2, ROW2);
    }

    private SpreadsheetRange range(final int column1, final int row1, final int column2, final int row2) {
        return SpreadsheetRange.with(this.cell(column1, row1), this.cell(column2, row2));
    }

    private SpreadsheetRange range(final SpreadsheetCellReference begin, final SpreadsheetCellReference end) {
        return SpreadsheetRange.with(begin, end);
    }

    private SpreadsheetCellReference cell() {
        return this.cell(99, 88);
    }

    private SpreadsheetCellReference cell(final int column, final int row) {
        return this.column(column)
                .setRow(this.row(row));
    }
    
    private SpreadsheetColumnReference column(final int column) {
        return SpreadsheetReferenceKind.ABSOLUTE.column(column);
    }

    private SpreadsheetRowReference row(final int row) {
        return SpreadsheetReferenceKind.ABSOLUTE.row(row);
    }
    
    private void check(final SpreadsheetRange range,
                       final int column1,
                       final int row1,
                       final int column2,
                       final int row2,
                       final int width,
                       final int height) {
        this.checkBegin(range, column1, row1);
        this.checkEnd(range, column2, row2);
        this.checkWidth(range, width);
        this.checkHeight(range, height);
    }

    private void checkBegin(final SpreadsheetRange range, final int column, final int row) {
        this.checkBegin(range, this.cell(column, row));
    }

    private void checkBegin(final SpreadsheetRange range, final SpreadsheetCellReference begin) {
        assertEquals("range begin=" + range, begin, range.begin());
    }

    private void checkEnd(final SpreadsheetRange range, final int column, final int row) {
        this.checkEnd(range, this.cell(column, row));
    }

    private void checkEnd(final SpreadsheetRange range, final SpreadsheetCellReference end) {
        assertEquals("range end="+ range, end, range.end());
    }

    private void checkWidth(final SpreadsheetRange range, final int width) {
        assertEquals("range width="+ range, width, range.width());
    }

    private void checkHeight(final SpreadsheetRange range, final int height) {
        assertEquals("range height="+ range, height, range.height());
    }

    @Override
    protected Class<SpreadsheetRange> type() {
        return SpreadsheetRange.class;
    }
}
