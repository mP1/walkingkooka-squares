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
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.naming.Name;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetCoordinates;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePatterns;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeParsePatterns;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRange;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.FontFamilyName;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.TextStyle;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The {@link Name} of metadata property.
 */
public abstract class SpreadsheetMetadataPropertyName<T> implements Name, Comparable<SpreadsheetMetadataPropertyName<?>> {

    // constants

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    /**
     * A read only cache of already prepared {@link SpreadsheetMetadataPropertyName names}..
     */
    final static Map<String, SpreadsheetMetadataPropertyName<?>> CONSTANTS = Maps.sorted(SpreadsheetMetadataPropertyName.CASE_SENSITIVITY.comparator());

    /**
     * Registers a new {@link SpreadsheetMetadataPropertyName}.
     */
    private static <T> SpreadsheetMetadataPropertyName<T> registerConstant(final SpreadsheetMetadataPropertyName<T> constant) {
        SpreadsheetMetadataPropertyName.CONSTANTS.put(constant.name, constant);
        return constant;
    }

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>creator {@link EmailAddress}</code>
     */
    public final static SpreadsheetMetadataPropertyName<EmailAddress> CREATOR = registerConstant(SpreadsheetMetadataPropertyNameCreator.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>creation {@link LocalDateTime}</code>
     */
    public final static SpreadsheetMetadataPropertyName<LocalDateTime> CREATE_DATE_TIME = registerConstant(SpreadsheetMetadataPropertyNameCreateDateTime.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>currency {@link String}</code>
     */
    public final static SpreadsheetMetadataPropertyName<String> CURRENCY_SYMBOL = registerConstant(SpreadsheetMetadataPropertyNameCurrencySymbol.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>date-format-pattern {@link String}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetDateFormatPattern> DATE_FORMAT_PATTERN = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetDateFormatPattern.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>date-parse-patterns {@link String}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetDateParsePatterns> DATE_PARSE_PATTERNS = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>date-time-offset {@link Long}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Long> DATETIME_OFFSET = registerConstant(SpreadsheetMetadataPropertyNameDateTimeOffset.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>date-time-format-pattern {@link String}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetDateTimeFormatPattern> DATETIME_FORMAT_PATTERN = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetDateTimeFormatPattern.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>date-time-parse-patterns</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetDateTimeParsePatterns> DATETIME_PARSE_PATTERNS = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetDateTimeParsePatterns.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>decimal-separator {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Character> DECIMAL_SEPARATOR = registerConstant(SpreadsheetMetadataPropertyNameDecimalSeparator.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>edit-cell</code> {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference}.
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetCellReference> EDIT_CELL = registerConstant(SpreadsheetMetadataPropertyNameEditCell.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>edit-range</code> {@link SpreadsheetRange}.
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetRange> EDIT_RANGE = registerConstant(SpreadsheetMetadataPropertyNameEditRange.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>exponent-symbol {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<String> EXPONENT_SYMBOL = registerConstant(SpreadsheetMetadataPropertyNameExponentSymbol.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>expression-number-kind {@link walkingkooka.tree.expression.ExpressionNumberKind}</code>
     */
    public final static SpreadsheetMetadataPropertyName<ExpressionNumberKind> EXPRESSION_NUMBER_KIND = registerConstant(SpreadsheetMetadataPropertyNameExpressionNumberKind.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>grouping-separator {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Character> GROUPING_SEPARATOR = registerConstant(SpreadsheetMetadataPropertyNameGroupingSymbol.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>{@link Locale}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Locale> LOCALE = registerConstant(SpreadsheetMetadataPropertyNameLocale.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>last modified by {@link EmailAddress}</code>
     */
    public final static SpreadsheetMetadataPropertyName<EmailAddress> MODIFIED_BY = registerConstant(SpreadsheetMetadataPropertyNameModifiedBy.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>modified {@link LocalDateTime}</code>
     */
    public final static SpreadsheetMetadataPropertyName<LocalDateTime> MODIFIED_DATE_TIME = registerConstant(SpreadsheetMetadataPropertyNameModifiedDateTime.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>negative-sign {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Character> NEGATIVE_SIGN = registerConstant(SpreadsheetMetadataPropertyNameNegativeSign.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>number-format-pattern</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetNumberFormatPattern> NUMBER_FORMAT_PATTERN = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetNumberFormatPattern.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>number-parse-pattern</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetNumberParsePatterns> NUMBER_PARSE_PATTERNS = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetNumberParsePatterns.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>percentage-symbol {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Character> PERCENTAGE_SYMBOL = registerConstant(SpreadsheetMetadataPropertyNamePercentageSymbol.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>positive-sign {@link Character}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Character> POSITIVE_SIGN = registerConstant(SpreadsheetMetadataPropertyNamePositiveSign.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>rounding-mode {@link RoundingMode}</code>
     */
    public final static SpreadsheetMetadataPropertyName<RoundingMode> ROUNDING_MODE = registerConstant(SpreadsheetMetadataPropertyNameRoundingMode.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>precision {@link Integer}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Integer> PRECISION = registerConstant(SpreadsheetMetadataPropertyNamePrecision.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>spreadsheet-id {@link SpreadsheetId}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetId> SPREADSHEET_ID = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetId.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>spreadsheet-name {@link SpreadsheetName}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetName> SPREADSHEET_NAME = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetName.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>style {@link walkingkooka.tree.text.TextStyle}</code>
     */
    public final static SpreadsheetMetadataPropertyName<TextStyle> STYLE = registerConstant(SpreadsheetMetadataPropertyNameStyle.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>text-format-pattern {@link SpreadsheetFormatPattern}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetTextFormatPattern> TEXT_FORMAT_PATTERN = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetTextFormatPattern.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>time-format-pattern {@link SpreadsheetFormatPattern}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetTimeFormatPattern> TIME_FORMAT_PATTERN = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetTimeFormatPattern.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>time-parse-patterns</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetTimeParsePatterns> TIME_PARSE_PATTERNS = registerConstant(SpreadsheetMetadataPropertyNameSpreadsheetTimeParsePatterns.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>two-digit-year {@link SpreadsheetFormatPattern}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Integer> TWO_DIGIT_YEAR = registerConstant(SpreadsheetMetadataPropertyNameTwoYearDigit.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>viewport-cell {@link SpreadsheetCoordinates}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetCellReference> VIEWPORT_CELL = registerConstant(SpreadsheetMetadataPropertyNameViewportCell.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>viewport-coordinates {@link SpreadsheetCoordinates}</code>
     */
    public final static SpreadsheetMetadataPropertyName<SpreadsheetCoordinates> VIEWPORT_COORDINATES = registerConstant(SpreadsheetMetadataPropertyNameViewportCoordinates.instance());

    /**
     * A {@link SpreadsheetMetadataPropertyName} holding the <code>width {@link Integer}</code>
     */
    public final static SpreadsheetMetadataPropertyName<Integer> WIDTH = registerConstant(SpreadsheetMetadataPropertyNameWidth.instance());

    /**
     * Factory that assumes a valid {@link SpreadsheetMetadataPropertyName} or fails.
     */
    public static SpreadsheetMetadataPropertyName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        SpreadsheetMetadataPropertyName propertyName = CONSTANTS.get(name);
        if (null == propertyName) {
            if (false == name.startsWith(COLOR_PREFIX) || name.length() == COLOR_PREFIX.length()) {
                throw new IllegalArgumentException("Unknown metadata property name " + CharSequences.quoteAndEscape(name));
            }

            final String after = name.substring(COLOR_PREFIX.length());

            // name dash color is a numbered color, named dash letter is a named color
            try {
                if (Character.isLetter(after.charAt(0))) {
                    propertyName = namedColor(SpreadsheetColorName.with(after));
                } else {
                    propertyName = numberedColor(Integer.parseInt(after));
                }
            } catch (final RuntimeException cause) {
                throw new IllegalArgumentException("Invalid metadata property name " + CharSequences.quoteAndEscape(name), cause);
            }
        }
        return propertyName;
    }

    final static String COLOR_PREFIX = "color-";

    /**
     * Retrieves a {@link SpreadsheetMetadataPropertyName} for a {@link SpreadsheetColorName named} {@link Color}.
     */
    public static SpreadsheetMetadataPropertyName<Color> namedColor(final SpreadsheetColorName name) {
        return SpreadsheetMetadataPropertyNameNamedColor.withColorName(name);
    }

    /**
     * Retrieves a {@link SpreadsheetMetadataPropertyName} for a numbered {@link Color}.
     */
    public static SpreadsheetMetadataPropertyName<Color> numberedColor(final int number) {
        return SpreadsheetMetadataPropertyNameNumberedColor.withNumber(number);
    }

    /**
     * Package private constructor use factory.
     */
    SpreadsheetMetadataPropertyName(final String name) {
        super();
        this.name = name;
        this.jsonPropertyName = JsonPropertyName.with(name);
    }

    @Override
    public final String value() {
        return this.name;
    }

    final String name;

    final JsonPropertyName jsonPropertyName;

    /**
     * Validates the value, returning the value that will be saved.
     */
    @SuppressWarnings("UnusedReturnValue")
    public T checkValue(final Object value) {
        if (null == value) {
            throw new SpreadsheetMetadataPropertyValueException("Missing value", this, value);
        }

        return this.checkValue0(value);
    }

    abstract T checkValue0(final Object value);

    /**
     * Checks the type of the given value and throws a {@link SpreadsheetMetadataPropertyValueException} if this test fails.
     */
    final T checkValueType(final Object value,
                           final Predicate<Object> typeChecker) {
        if (!typeChecker.test(value)) {
            throw this.spreadsheetMetadataPropertyValueException(value);
        }
        return Cast.to(value);
    }

    /**
     * Creates a {@link SpreadsheetMetadataPropertyValueException} used to report an invalid value.
     */
    final SpreadsheetMetadataPropertyValueException spreadsheetMetadataPropertyValueException(final Object value) {
        return new SpreadsheetMetadataPropertyValueException("Expected " + this.expected(),
                this,
                value);
    }

    /**
     * Prpvides the actual text with the exception message.
     */
    abstract String expected();

    /**
     * Defaults must not include a spreadsheet-id, email address or timestamp.
     */
    final boolean isInvalidGenericProperty() {
        return this instanceof SpreadsheetMetadataPropertyNameDateTimeOffset ||
                this instanceof SpreadsheetMetadataPropertyNameEmailAddress ||
                this instanceof SpreadsheetMetadataPropertyNameLocalDateTime ||
                this instanceof SpreadsheetMetadataPropertyNameSpreadsheetId;
    }

    // loadFromLocale...................................................................................................

    /**
     * Some properties support providing a value for the given Locale for the parent {@link SpreadsheetMetadata} to be updated.
     */
    abstract Optional<T> extractLocaleValue(final Locale locale);

    /**
     * Factory that fetches a {@link DateFormat}, casts to a {@link SimpleDateFormat} and parses the pattern.
     */
    final Optional<T> extractLocaleSimpleDateFormat(final Locale locale,
                                                    final Function<Locale, DateFormat> localeToDateFormat,
                                                    final Function<String, T> patternParser) {
        return Optional.of(patternParser.apply(simpleDateFormatPattern(localeToDateFormat.apply(locale))));
    }

    /**
     * Casts and grabs the {@link SimpleDateFormat#toPattern()} and then converts the pattern using {@link SpreadsheetMetadataPropertyNameSimpleDateFormatPatternVisitor}.
     */
    final static String simpleDateFormatPattern(final DateFormat dateFormat) {
        final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) dateFormat;
        return SpreadsheetMetadataPropertyNameSimpleDateFormatPatternVisitor.pattern(simpleDateFormat.toPattern());
    }

    /**
     * This method is only called by {@link SpreadsheetMetadataPropertyNameSpreadsheetDateParsePatterns} and
     * {@link SpreadsheetMetadataPropertyNameSpreadsheetTimeParsePatterns}. It creates a pattern with FULL, LONG, MEDIUM and SHORT and
     * then calls the factory.
     */
    final Optional<T> dateFormatThenParsePattern(final Locale locale,
                                                 final BiFunction<Integer, Locale, String> pattern,
                                                 final Function<String, T> parser) {
        final String full = pattern.apply(DateFormat.FULL, locale).trim();
        final String longPattern = pattern.apply(DateFormat.LONG, locale).trim();
        final String medium = pattern.apply(DateFormat.MEDIUM, locale).trim();
        final String shortPattern = pattern.apply(DateFormat.SHORT, locale).trim();

        return Optional.of(parser.apply(full + ";" + longPattern + ";" + medium + ";" + shortPattern));
    }

    /**
     * This makes an assumption that a {@link DecimalFormat} pattern will only use characters that are also equal in
     * functionality and meaning within a spreadsheet number format.
     */
    static String decimalFormatPattern(final NumberFormat numberFormat) {
        final DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        return decimalFormat.toPattern();
    }

    // SpreadsheetMetadataVisitor.......................................................................................

    /**
     * Dispatches to the appropriate {@link SpreadsheetMetadataVisitor} visit method.
     */
    abstract void accept(final T value, final SpreadsheetMetadataVisitor visitor);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.caseSensitivity().hash(this.name);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof SpreadsheetMetadataPropertyName &&
                        this.equals0((SpreadsheetMetadataPropertyName) other);
    }

    private boolean equals0(final SpreadsheetMetadataPropertyName other) {
        return this.caseSensitivity().equals(this.name, other.name);
    }

    @Override
    public final String toString() {
        return this.value();
    }

    // HasCaseSensitivity...............................................................................................

    /**
     * Used during hashing and equality checks.
     */
    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final SpreadsheetMetadataPropertyName<?> other) {
        return this.caseSensitivity().comparator().compare(this.name, other.name);
    }

    // Json...........................................................................................................

    final T unmarshall(final JsonNode node,
                       final JsonNodeUnmarshallContext context) {
        return context.unmarshall(node, this.type());
    }

    abstract Class<T> type();

    /**
     * Factory that retrieves a {@link SpreadsheetMetadataPropertyName} from a {@link JsonNode#name()}.
     */
    static SpreadsheetMetadataPropertyName<?> unmarshallName(final JsonNode node) {
        return with(node.name().value());
    }

    /*
     * Force class initialization of the following types which will ensure they also {@link walkingkooka.tree.json.marshall.JsonNodeContext#register(String, BiFunction, BiFunction, Class, Class[])}
     */
    static {
        Color.BLACK.alpha();
        EmailAddress.tryParse("user@example.com");
        ExpressionNumberKind.DEFAULT.name();
        FontFamilyName.with("MS Sans Serif");
        FontSize.with(1);
        SpreadsheetPattern.parseNumberFormatPattern(" ");
        //noinspection ResultOfMethodCallIgnored
        SpreadsheetId.with(0);
    }
}
