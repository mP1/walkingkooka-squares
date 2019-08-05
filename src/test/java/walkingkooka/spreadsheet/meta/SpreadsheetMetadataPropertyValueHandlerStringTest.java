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

package walkingkooka.spreadsheet.meta;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;

public final class SpreadsheetMetadataPropertyValueHandlerStringTest extends SpreadsheetMetadataPropertyValueHandlerTestCase3<SpreadsheetMetadataPropertyValueHandlerString, String> {

    @Test
    public void testFromJsonNode() {
        final String string = this.propertyValue();
        this.fromJsonNodeAndCheck(JsonNode.string(string), string);
    }

    @Test
    public void testToJsonNode() {
        final String string = this.propertyValue();
        this.toJsonNodeAndCheck(string, JsonNode.string(string));
    }

    @Override
    SpreadsheetMetadataPropertyValueHandlerString handler() {
        return SpreadsheetMetadataPropertyValueHandlerString.INSTANCE;
    }

    @Override
    SpreadsheetMetadataPropertyName<String> propertyName() {
        return SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL;
    }

    @Override
    String propertyValue() {
        return "$AUD";
    }

    @Override
    String propertyValueType() {
        return String.class.getSimpleName();
    }

    @Override
    public Class<SpreadsheetMetadataPropertyValueHandlerString> type() {
        return SpreadsheetMetadataPropertyValueHandlerString.class;
    }
}