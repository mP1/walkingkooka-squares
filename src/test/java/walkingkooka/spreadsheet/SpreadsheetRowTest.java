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

package walkingkooka.spreadsheet;

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetReferenceKind;
import walkingkooka.spreadsheet.parser.SpreadsheetRowReference;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetRowTest implements ClassTesting2<SpreadsheetRow>,
        ComparableTesting<SpreadsheetRow>,
        HasJsonNodeTesting<SpreadsheetRow>,
        ToStringTesting<SpreadsheetRow> {


    private final static int ROW = 20;
    private final static SpreadsheetRowReference REFERENCE = reference(ROW);

    @Test
    public void testWithNullReferenceFails() {
        assertThrows(NullPointerException.class, () -> {
            SpreadsheetRow.with(null);
        });
    }

    @Test
    public void testWith() {
        final SpreadsheetRow row = this.createRow();

        this.checkReference(row);
    }

    // SetReference.....................................................................................................

    @Test
    public void testSetReferenceNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRow().setReference(null);
        });
    }

    @Test
    public void testSetReferenceSame() {
        final SpreadsheetRow row = this.createRow();
        assertSame(row, row.setReference(row.reference()));
    }

    @Test
    public void testSetReferenceDifferent() {
        final SpreadsheetRow row = this.createRow();
        final SpreadsheetRowReference differentReference = differentReference();
        final SpreadsheetRow different = row.setReference(differentReference);
        assertNotSame(row, different);

        this.checkReference(different, differentReference);

        this.checkReference(row);
    }

    // equals .............................................................................................

    @Test
    public void testCompareDifferentRow() {
        this.compareToAndCheckLess(this.createComparable(ROW + 999));
    }

    // HasJsonNode...............................................................................................

    // HasJsonNode.fromJsonNode.......................................................................................

    @Test
    public void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeNullFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public void testFromJsonNodeNumberFails() {
        this.fromJsonNodeFails(JsonNode.number(12));
    }

    @Test
    public void testFromJsonNodeArrayFails() {
        this.fromJsonNodeFails(JsonNode.array());
    }

    @Test
    public void testFromJsonNodeStringFails() {
        this.fromJsonNodeFails(JsonNode.string("fails"));
    }

    @Test
    public void testFromJsonNodeObjectEmptyFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }


    // HasJsonNode .toJsonNode.........................................................................
    @Test
    public void testJsonNode() {
        final SpreadsheetRowReference reference = reference(ROW);
        this.toJsonNodeAndCheck(reference, reference.toJsonNode());
    }

    // toString...............................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(SpreadsheetRow.with(REFERENCE), "$21");
    }

    private SpreadsheetRow createRow() {
        return this.createComparable();
    }

    @Override
    public SpreadsheetRow createComparable() {
        return this.createComparable(ROW);
    }

    private SpreadsheetRow createComparable(final int row) {
        return SpreadsheetRow.with(reference(row));
    }

    private static SpreadsheetRowReference differentReference() {
        return reference(999);
    }

    private static SpreadsheetRowReference reference(final int row) {
        return SpreadsheetReferenceKind.ABSOLUTE.row(row);
    }

    private void checkReference(final SpreadsheetRow row) {
        this.checkReference(row, REFERENCE);
    }

    private void checkReference(final SpreadsheetRow row, final SpreadsheetRowReference reference) {
        assertEquals(reference, row.reference(), "reference");
    }

    @Override
    public Class<SpreadsheetRow> type() {
        return SpreadsheetRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean compareAndEqualsMatch() {
        return true;
    }

    // HasJsonNodeTesting............................................................

    @Override
    public SpreadsheetRow createHasJsonNode() {
        return this.createObject();
    }

    @Override
    public SpreadsheetRow fromJsonNode(final JsonNode jsonNode) {
        return SpreadsheetRow.fromJsonNode(jsonNode);
    }
}
