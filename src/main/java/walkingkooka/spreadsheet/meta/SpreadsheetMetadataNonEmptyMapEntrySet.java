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

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A read only {@link Set} sorted view of textStyle that have had their values checked.
 */
final class SpreadsheetMetadataNonEmptyMapEntrySet extends AbstractSet<Entry<SpreadsheetMetadataPropertyName<?>, Object>> {

    static {
        Sets.registerImmutableType(SpreadsheetMetadataNonEmptyMapEntrySet.class);
    }

    /**
     * An empty {@link SpreadsheetMetadataNonEmptyMap}.
     */
    static final SpreadsheetMetadataNonEmptyMapEntrySet EMPTY = new SpreadsheetMetadataNonEmptyMapEntrySet(Lists.empty());

    /**
     * Factory that creates a {@link SpreadsheetMetadataNonEmptyMapEntrySet}.
     */
    static SpreadsheetMetadataNonEmptyMapEntrySet with(final Map<SpreadsheetMetadataPropertyName<?>, Object> entries) {
        final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> list = Lists.array();

        for (final Entry<SpreadsheetMetadataPropertyName<?>, Object> propertyAndValue : entries.entrySet()) {
            final SpreadsheetMetadataPropertyName<?> property = propertyAndValue.getKey();
            if (null == property) {
                throw new NullPointerException("Map contains null key got " + entries);
            }

            final Object value = propertyAndValue.getValue();
            property.checkValue(value);

            list.add(Maps.entry(property, value));
        }

        list.sort(SpreadsheetMetadataNonEmptyMapEntrySet::comparator);
        return list.isEmpty() ?
                EMPTY :
                withList(list);
    }

    /**
     * A {@link Comparator} that maybe used to sort all entries so they appear in alphabetical order.
     */
    private static int comparator(final Entry<SpreadsheetMetadataPropertyName<?>, Object> first,
                                  final Entry<SpreadsheetMetadataPropertyName<?>, Object> second) {
        return first.getKey().compareTo(second.getKey());
    }

    /**
     * Creates a new {@link SpreadsheetMetadataNonEmptyMapEntrySet} to hold the entries, assuming the {@link List} of entries has already been sorted.
     */
    static SpreadsheetMetadataNonEmptyMapEntrySet withList(final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> entries) {
        return new SpreadsheetMetadataNonEmptyMapEntrySet(entries);
    }

    private SpreadsheetMetadataNonEmptyMapEntrySet(final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Iterator<Entry<SpreadsheetMetadataPropertyName<?>, Object>> iterator() {
        return Iterators.readOnly(this.entries.iterator());
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    final List<Entry<SpreadsheetMetadataPropertyName<?>, Object>> entries;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return toString0("[", "]");
    }

    /**
     * This method supports the different surrounding characters of a {@link Map}.
     */
    String toString0(final String prefix, final String suffix) {
        return this.entries.stream()
                .map(SpreadsheetMetadataNonEmptyMapEntrySet::toStringEntry)
                .collect(Collectors.joining(", ", prefix, suffix));
    }

    private static String toStringEntry(final Entry<SpreadsheetMetadataPropertyName<?>, Object> entry) {
        return entry.getKey() + "=" + CharSequences.quoteIfChars(entry.getValue());
    }

    // SpreadsheetMetadataVisitor.......................................................................................

    void accept(final SpreadsheetMetadataVisitor visitor) {
        this.entries
                .forEach(visitor::acceptPropertyAndValue);
    }

    // JsonNodeContext..................................................................................................

    /**
     * Recreates this {@link SpreadsheetMetadataNonEmptyMapEntrySet} from the json object.
     */
    static SpreadsheetMetadataNonEmptyMapEntrySet fromJson(final JsonNode json,
                                                           final JsonNodeUnmarshallContext context) {
        final Map<SpreadsheetMetadataPropertyName<?>, Object> properties = Maps.ordered();

        for (final JsonNode child : json.children()) {
            final SpreadsheetMetadataPropertyName<?> name = SpreadsheetMetadataPropertyName.unmarshallName(child);
            properties.put(name, name.unmarshall(child, context));
        }

        return with(properties);
    }

    /**
     * Creates a json object using the keys and values from the entries in this {@link Set}.
     */
    JsonNode toJson(final JsonNodeMarshallContext context) {
        final List<JsonNode> json = Lists.array();

        for (Entry<SpreadsheetMetadataPropertyName<?>, Object> propertyAndValue : this.entries) {
            final SpreadsheetMetadataPropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = context.marshall(propertyAndValue.getValue());

            json.add(value.setName(propertyName.jsonPropertyName));
        }

        return JsonNode.object()
                .setChildren(json);
    }
}
