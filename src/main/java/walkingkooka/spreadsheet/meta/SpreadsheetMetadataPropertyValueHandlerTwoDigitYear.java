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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.map.FromJsonNodeContext;
import walkingkooka.tree.json.map.ToJsonNodeContext;

/**
 * A {@link SpreadsheetMetadataPropertyValueHandler} that only allows values between 0 and 99.
 */
final class SpreadsheetMetadataPropertyValueHandlerTwoDigitYear extends SpreadsheetMetadataPropertyValueHandler<Integer> {

    /**
     * A singleton
     */
    static final SpreadsheetMetadataPropertyValueHandlerTwoDigitYear INSTANCE = new SpreadsheetMetadataPropertyValueHandlerTwoDigitYear();

    /**
     * Private ctor use singleton
     */
    private SpreadsheetMetadataPropertyValueHandlerTwoDigitYear() {
        super();
    }

    @Override
    void check0(final Object value, final SpreadsheetMetadataPropertyName<?> name) {
        final int integer = this.checkType(value, Integer.class, name);
        if (integer < 0 || integer > 99) {
            throw new SpreadsheetMetadataPropertyValueException("Expected value between 0 and 99 but got " + integer, name, value);
        }
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return Integer.class.getSimpleName();
    }

    @Override
    public String toString() {
        return "TwoDigitYear";
    }

    // JsonNodeContext..................................................................................................

    @Override
    Integer fromJsonNode(final JsonNode node,
                         final SpreadsheetMetadataPropertyName<?> name,
                         final FromJsonNodeContext context) {
        return context.fromJsonNode(node, Integer.class);
    }

    @Override
    JsonNode toJsonNode(final Integer value,
                        final ToJsonNodeContext context) {
        return context.toJsonNode(value);
    }
}
