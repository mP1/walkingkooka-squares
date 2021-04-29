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

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparableTesting2;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class SpreadsheetCellReferenceOrLabelNameTestCase<R extends SpreadsheetCellReferenceOrLabelName<R>> extends SpreadsheetExpressionReferenceTestCase<R>
        implements ComparableTesting2<R> {

    SpreadsheetCellReferenceOrLabelNameTestCase() {
        super();
    }

    // SpreadsheetViewport.........................................................................................

    @Test
    public void testViewport() {
        final double width = 100.5;
        final double height = 20.5;
        final R reference = this.createReference();

        final SpreadsheetViewport viewport = reference.viewport(width, height);

        assertEquals(reference.toRelative(), viewport.reference(), "reference");
        assertEquals(width, viewport.width(), "width");
        assertEquals(height, viewport.height(), "height");
    }
}