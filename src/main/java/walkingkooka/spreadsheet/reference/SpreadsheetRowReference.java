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

package walkingkooka.spreadsheet.reference;

import walkingkooka.Cast;

/**
 * Represents a row reference
 */
public final class SpreadsheetRowReference extends SpreadsheetColumnOrRowReference<SpreadsheetRowReference> {

    // https://support.office.com/en-us/article/excel-specifications-and-limits-1672b34d-7043-467e-8e27-269d656771c3
    public final static int MAX = 1_048_576;
    public final static int RADIX = 10;

    /**
     * Factory that creates a new row.
     */
    static SpreadsheetRowReference with(final int value, final SpreadsheetReferenceKind referenceKind) {
        checkValue(value);
        checkReferenceKind(referenceKind);

        return value < CACHE_SIZE ?
                referenceKind.rowFromCache(value) :
                new SpreadsheetRowReference(value, referenceKind);
    }

    static final SpreadsheetRowReference[] ABSOLUTE = fillCache(i -> new SpreadsheetRowReference(i, SpreadsheetReferenceKind.ABSOLUTE),
            new SpreadsheetRowReference[CACHE_SIZE]);
    static final SpreadsheetRowReference[] RELATIVE = fillCache(i -> new SpreadsheetRowReference(i, SpreadsheetReferenceKind.RELATIVE),
            new SpreadsheetRowReference[CACHE_SIZE]);

    private static String invalidRowValue(final int value) {
        return "Invalid column value " + value + " expected between 0 and " + MAX;
    }

    private SpreadsheetRowReference(final int value, final SpreadsheetReferenceKind referenceKind) {
        super(value, referenceKind);
    }

    /**
     * Would be setter that returns a {@link SpreadsheetRowReference} with the given {@link SpreadsheetReferenceKind}
     * creating a new instance if necessary.
     */
    @Override
    public SpreadsheetRowReference setReferenceKind(final SpreadsheetReferenceKind referenceKind) {
        checkReferenceKind(referenceKind);

        return this.setReferenceKind0(referenceKind);
    }

    @Override
    SpreadsheetRowReference replaceReferenceKind(final SpreadsheetReferenceKind referenceKind) {
        return new SpreadsheetRowReference(this.value, referenceKind);
    }

    @Override
    public SpreadsheetRowReference add(final int value) {
        return Cast.to(this.add0(value));
    }

    /**
     * Would be setter that returns a {@link SpreadsheetRowReference} with the given value creating a new
     * instance if it is different.
     */
    public SpreadsheetRowReference setValue(final int value) {
        checkValue(value);
        return this.value == value ?
                this :
                new SpreadsheetRowReference(value, this.referenceKind());
    }

    private static void checkValue(final int value) {
        if (value < 0 || value >= MAX) {
            throw new IllegalArgumentException(invalidRowValue(value));
        }
    }

    /**
     * Creates a {@link SpreadsheetCellReference} from this row and the given column.
     */
    public SpreadsheetCellReference setColumn(final SpreadsheetColumnReference column) {
        return column.setRow(this);
    }

    public String hateosLinkId() {
        return String.valueOf(this.value + 1);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetRowReference;
    }

    /**
     * Returns true if the values ignoring the {@link SpreadsheetReferenceKind}.
     */
    @Override
    public boolean equalsIgnoreReferenceKind(final SpreadsheetRowReference other) {
        return this.equalsIgnoreReferenceKind0(other);
    }

    @Override
    public String toString() {
        // in text form columns start at 1 but internally are zero based.
        return this.referenceKind().prefix() + (this.value + 1);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final SpreadsheetRowReference other) {
        checkOther(other);
        return this.value - other.value;
    }
}
