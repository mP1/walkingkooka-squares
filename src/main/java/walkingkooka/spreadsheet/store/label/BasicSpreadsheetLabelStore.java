package walkingkooka.spreadsheet.store.label;

import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetLabelMapping;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetLabelName;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetLabelStore} that uses a {@link Map}.
 */
final class BasicSpreadsheetLabelStore implements SpreadsheetLabelStore {

    /**
     * Factory that creates a new {@link BasicSpreadsheetLabelStore}
     */
    static BasicSpreadsheetLabelStore create() {
        return new BasicSpreadsheetLabelStore();
    }

    /**
     * Private ctor.
     */
    private BasicSpreadsheetLabelStore() {
        super();
    }

    @Override
    public final Optional<SpreadsheetLabelMapping> load(final SpreadsheetLabelName label) {
        Objects.requireNonNull(label, "labels");
        return Optional.ofNullable(this.mappings.get(label));
    }

    @Override
    public final void save(final SpreadsheetLabelMapping mapping) {
        Objects.requireNonNull(mapping, "mapping");

        this.mappings.put(mapping.label(), mapping);
    }

    @Override
    public final void delete(final SpreadsheetLabelName label) {
        Objects.requireNonNull(label, "label");
        this.mappings.remove(label);
    }

    @Override
    public Collection<SpreadsheetLabelMapping> all() {
        return Collections.unmodifiableCollection(this.mappings.values());
    }

    /**
     * All mappings present in this spreadsheet
     */
    private final Map<SpreadsheetLabelName, SpreadsheetLabelMapping> mappings = Maps.sorted();

    @Override
    public String toString() {
        return this.mappings.toString();
    }
}
