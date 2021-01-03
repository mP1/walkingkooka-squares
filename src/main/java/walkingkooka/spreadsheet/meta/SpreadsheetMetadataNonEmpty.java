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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link SpreadsheetMetadataNonEmpty} holds a non empty {@link Map} of {@link SpreadsheetMetadataPropertyName} and values.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class SpreadsheetMetadataNonEmpty extends SpreadsheetMetadata {

    /**
     * Factory that creates a {@link SpreadsheetMetadataNonEmpty} from a {@link SpreadsheetMetadataNonEmptyMap}.
     */
    static SpreadsheetMetadataNonEmpty with(final SpreadsheetMetadataNonEmptyMap value) {
        return new SpreadsheetMetadataNonEmpty(value, null);
    }

    private SpreadsheetMetadataNonEmpty(final SpreadsheetMetadataNonEmptyMap value,
                                        final SpreadsheetMetadata defaults) {
        super(defaults);
        this.value = value;
    }

    // Value..........................................................................................................

    @Override
    public Map<SpreadsheetMetadataPropertyName<?>, Object> value() {
        return this.value;
    }

    final SpreadsheetMetadataNonEmptyMap value;

    // setDefaults......................................................................................................

    /**
     * Checks that all property values are valid or general and not specific to a single spreadsheet.
     */
    void checkDefaultsValues() {
        final String invalid = this.value.keySet()
                .stream()
                .filter(SpreadsheetMetadataPropertyName::isInvalidGenericProperty)
                .map(Object::toString)
                .sorted()
                .collect(Collectors.joining(", "));
        if (false == invalid.isEmpty()) {
            throw new IllegalArgumentException("Defaults includes invalid default values: " + invalid);
        }
    }

    @Override
    SpreadsheetMetadata replaceDefaults(final SpreadsheetMetadata defaults) {
        return new SpreadsheetMetadataNonEmpty(this.value, defaults);
    }

    // get..............................................................................................................

    @Override
    <V> Optional<V> getIgnoringDefaults(final SpreadsheetMetadataPropertyName<V> propertyName) {
        return Optional.ofNullable(Cast.to(this.value.get(propertyName)));
    }

    // set..............................................................................................................

    @Override
    <V> SpreadsheetMetadata set0(final SpreadsheetMetadataPropertyName<V> propertyName, final V value) {
        SpreadsheetMetadataNonEmptyMap map = this.value;
        final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> values = Lists.array();

        int mode = 0; // new property added.

        for (Entry<SpreadsheetMetadataPropertyName<?>, Object> propertyAndValue : map.entries) {
            final SpreadsheetMetadataPropertyName<?> property = propertyAndValue.getKey();

            if (propertyName.equals(property)) {
                if (propertyAndValue.getValue().equals(value)) {
                    mode = 1; // no change
                    break;
                } else {
                    values.add(Maps.entry(property, value));
                    mode = 2; // replaced
                }
            } else {
                values.add(propertyAndValue);
            }
        }

        // replace didnt happen
        if (0 == mode) {
            values.add(Maps.entry(propertyName, value));
            SpreadsheetMetadataNonEmptyMapEntrySet.sort(values);
        }

        return 1 == mode ?
                this :
                this.setValues(values);
    }

    // remove...........................................................................................................

    @Override
    SpreadsheetMetadata remove0(final SpreadsheetMetadataPropertyName<?> propertyName) {
        final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> values = Lists.array();
        boolean removed = false;

        for (Entry<SpreadsheetMetadataPropertyName<?>, Object> propertyAndValue : this.value.entries) {
            final SpreadsheetMetadataPropertyName<?> property = propertyAndValue.getKey();
            if (propertyName.equals(property)) {
                removed = true;
            } else {
                values.add(propertyAndValue);
            }
        }

        return removed ?
                this.remove1(values) :
                this;
    }

    /**
     * Accepts a list after removing a property, special casing if the list is empty.
     */
    private SpreadsheetMetadata remove1(final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> list) {
        return list.isEmpty() ?
                SpreadsheetMetadata.EMPTY.setDefaults(this.defaults()) :
                this.setValues(list); // no need to sort after a delete
    }

    private SpreadsheetMetadata setValues(final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> values) {
        return new SpreadsheetMetadataNonEmpty(SpreadsheetMetadataNonEmptyMap.withSpreadsheetMetadataMapEntrySet(SpreadsheetMetadataNonEmptyMapEntrySet.withList(values)), this.defaults);
    }

    // getters..........................................................................................................

    @Override
    public Function<SpreadsheetColorName, Optional<Color>> nameToColor() {
        if (null == this.nameToColor) {
            this.nameToColor = this.nameToColor0();
        }
        return this.nameToColor;
    }

    /**
     * Cache function
     */
    private Function<SpreadsheetColorName, Optional<Color>> nameToColor;

    @Override
    public Function<Integer, Optional<Color>> numberToColor() {
        if (null == this.numberToColor) {
            this.numberToColor = this.numberToColor0();
        }
        return this.numberToColor;
    }

    /**
     * Cache function
     */
    private Function<Integer, Optional<Color>> numberToColor;

    @Override
    public Converter<ExpressionNumberConverterContext> converter() {
        if (null == this.converter) {
            this.converter = this.converter0();
        }
        return this.converter;
    }

    /**
     * Cached {@link Converter}.
     */
    private Converter<ExpressionNumberConverterContext> converter;

    @Override
    public ExpressionNumberConverterContext converterContext() {
        if (null == this.converterContext) {
            this.converterContext = this.converterContext0();
        }
        return this.converterContext;
    }

    /**
     * Cached {@link ExpressionNumberConverterContext}.
     */
    private ExpressionNumberConverterContext converterContext;
    
    @Override
    public DateTimeContext dateTimeContext() {
        if (null == this.dateTimeContext) {
            this.dateTimeContext = this.dateTimeContext0();
        }
        return this.dateTimeContext;
    }

    /**
     * Cached {@link DateTimeContext}.
     */
    private DateTimeContext dateTimeContext;

    @Override
    public DecimalNumberContext decimalNumberContext() {
        if (null == this.decimalNumberContext) {
            this.decimalNumberContext = this.decimalNumberContext0();
        }
        return this.decimalNumberContext;
    }

    /**
     * Cached {@link DecimalNumberContext}.
     */
    private DecimalNumberContext decimalNumberContext;

    @Override
    public ExpressionNumberContext expressionNumberContext() {
        if (null == this.expressionNumberContext) {
            this.expressionNumberContext = this.expressionNumberContext0();
        }
        return this.expressionNumberContext;
    }

    /**
     * Cached {@link ExpressionNumberContext}.
     */
    private ExpressionNumberContext expressionNumberContext;

    @Override
    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
        if (null == this.jsonNodeUnmarshallContext) {
            this.jsonNodeUnmarshallContext = this.jsonNodeUnmarshallContext0();
        }
        return this.jsonNodeUnmarshallContext;
    }

    /**
     * Cached {@link JsonNodeUnmarshallContext}.
     */
    private JsonNodeUnmarshallContext jsonNodeUnmarshallContext;

    @Override
    public MathContext mathContext() {
        if (null == this.mathContext) {
            this.mathContext = this.mathContext0();
        }
        return this.mathContext;
    }

    /**
     * Cached {@link MathContext}.
     */
    private MathContext mathContext;

    @Override
    public SpreadsheetFormatter formatter() {
        if (null == this.formatter) {
            this.formatter = this.formatter0();
        }
        return this.formatter;
    }

    /**
     * Cached {@link SpreadsheetFormatter}.
     */
    private SpreadsheetFormatter formatter;

    @Override
    public synchronized SpreadsheetFormatterContext formatterContext(final SpreadsheetFormatter defaultFormatter) {
        if (false == defaultFormatter.equals(this.defaultFormatter)) {
            this.formatterContext = this.formatterContext0(defaultFormatter);
            this.defaultFormatter = defaultFormatter;
        }
        return this.formatterContext;
    }

    /**
     * The default formatter used to create the {@link SpreadsheetFormatterContext}.
     */
    private SpreadsheetFormatter defaultFormatter;

    /**
     * Cached {@link SpreadsheetFormatter} which also uses the {@link #defaultFormatter} as the default formatter.
     */
    private SpreadsheetFormatterContext formatterContext;
    
    // SpreadsheetMetadataVisitor.......................................................................................

    @Override
    void accept(final SpreadsheetMetadataVisitor visitor) {
        this.value.accept(visitor);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof SpreadsheetMetadataNonEmpty;
    }

    @Override
    boolean equalsValues(final SpreadsheetMetadata other) {
        return this.value.equals(other.value());
    }

    @Override
    public final String toString() {
        return this.value.toString();
    }

    // JsonNodeContext..................................................................................................

    @Override
    JsonObject marshallProperties(final JsonNodeMarshallContext context) {
        return this.value.marshall(context)
                .objectOrFail();
    }
}
