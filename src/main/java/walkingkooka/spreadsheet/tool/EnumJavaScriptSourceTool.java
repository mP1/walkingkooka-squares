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

package walkingkooka.spreadsheet.tool;

import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.text.TextStylePropertyName;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class EnumJavaScriptSourceTool {

    public static void main(final String[] args) throws Exception {
        final Path reactSrc = Paths.get("../walkingkooka-spreadsheet-react/src/");
        generateTextStylePropertyNames(Paths.get(reactSrc.toString(), "text"));
    }

    private static void generateTextStylePropertyNames(final Path dest) throws Exception {
        for (final TextStylePropertyName<?> property : TextStylePropertyName.values()) {
            final Optional<Class<Enum<?>>> maybeEnumType = property.enumType();
            if (maybeEnumType.isPresent()) {
                generateEnums(maybeEnumType.get(), dest);
            }
        }
    }

    private static void generateEnums(final Class<Enum<?>> enumType,
                                      final Path dest) throws Exception {
        try (final Writer writer = new FileWriter(sourceFilePath(dest, enumType).toFile())) {
            final IndentingPrinter printer = Printers.writer(writer, LineEnding.SYSTEM)
                    .indenting(Indentation.with("  "));
            generateSource(
                    enumType,
                    printer
            );
            printer.flush();
        }
    }

    private static Path sourceFilePath(final Path parent,
                                       final Class<?> type) {
        return Paths.get(parent.toString(), type.getSimpleName() + ".js");
    }

    private static void generateSource(final Class<Enum<?>> enumClass,
                                       final IndentingPrinter printer) throws Exception {
        final String enumTypeName = enumClass.getSimpleName();

        imports(printer);
        typeNameConstant(enumClass, printer);

        // export default class RoundingMode extends SystemEnum {
        printer.println("export default class " + enumTypeName + " extends SystemEnum {");
        printer.println();
        printer.indent();
        {
            constants(enumClass, printer);
            values(enumClass, printer);
            valueOf(enumClass, printer);
            fromJson(enumClass, printer);
            constructor(printer);
            typeName(printer);
        }
        printer.outdent();
        printer.println("}");

        printer.println();
        register(enumClass, printer);
    }

    /**
     * <pre>
     * import SystemEnum from "../SystemEnum.js";
     * import SystemObject from "../SystemObject.js";
     * </pre>
     */
    private static void imports(IndentingPrinter printer) {
        printer.println("import SystemEnum from \"../SystemEnum.js\";");
        printer.println("import SystemObject from \"../SystemObject.js\";");
        printer.println();
    }

    /**
     * <pre>
     * const TYPE_NAME = "rounding-mode";
     * </pre>
     */
    private static void typeNameConstant(final Class<Enum<?>> enumType, final IndentingPrinter printer) {
        printer.println("const TYPE_NAME = \"" + toKebabCase(enumType.getSimpleName()) + "\";");
        printer.println();
    }

    private static void constants(final Class<Enum<?>> enumClass,
                                  final IndentingPrinter printer) throws Exception {
        final String enumTypeName = enumClass.getSimpleName();

        for (final Enum<?> value : values(enumClass)) {
            // static UP = new RoundingMode("UP", "Up");
            printer.println("static " + value.name() + " = new " + enumTypeName + "(\"" + value.name() + "\");");
        }
        printer.println();
    }

    /**
     * <pre>
     * static values() {
     *     return [
     *     ExpressionNumberKind.BIG_DECIMAL,
     *             ExpressionNumberKind.DOUBLE,
     *     ];
     * }
     * </pre>
     */
    private static void values(final Class<Enum<?>> enumClass,
                               final IndentingPrinter printer) throws Exception {
        printer.println("static values() {");

        printer.indent();
        {
            printer.println("return [");
            printer.indent();
            {
                final String enumTypeName = enumClass.getSimpleName();

                final List<Enum<?>> enumValues = values(enumClass);
                int comma = enumValues.size() - 1;
                for (final Enum<?> enumValue : enumValues) {
                    printer.println(enumTypeName + "." + enumValue.name() + (--comma >= 0 ? "," : ""));
                }
            }
            printer.outdent();
            printer.println("];");
        }
        printer.outdent();
        printer.println("}");

        printer.println();
    }

    /**
     * <pre>
     * static valueOf(name) {
     *     return SystemEnum.valueOf(name, ExpressionNumberKind.values());
     * }
     * </pre>
     */
    private static void valueOf(final Class<Enum<?>> enumClass,
                                final IndentingPrinter printer) {
        printer.println("static valueOf(name) {");

        printer.indent();
        {
            printer.println("return SystemEnum.valueOf(name, " + enumClass.getSimpleName() + ".values());");
        }
        printer.outdent();
        printer.println("}");

        printer.println();
    }


    /**
     * <pre>
     * static fromJson(name) {
     *     return RoundingMode.of(name);
     * }
     * </pre>
     */
    private static void fromJson(final Class<Enum<?>> enumClass,
                                 final IndentingPrinter printer) {
        printer.println("static fromJson(name) {");
        printer.indent();
        {
            printer.println("return " + enumClass.getSimpleName() + ".valueOf(name);");
        }
        printer.outdent();
        printer.println("}");

        printer.println();
    }


    /**
     * <pre>
     *     constructor(name, label) {
     *         super(name, label);
     *     }
     * </pre>
     */
    private static void constructor(final IndentingPrinter printer) {
        printer.println("constructor(name) {");
        printer.indent();
        {
            printer.println("super(name);");
        }
        printer.outdent();
        printer.println("}");

        printer.println();
    }

    /**
     * <pre>
     *   typeName() {
     *     return TYPE_NAME;
     *   }
     * </pre>
     */
    private static void typeName(final IndentingPrinter printer) {
        printer.println("typeName() {");
        printer.indent();
        {
            printer.println("return TYPE_NAME;");
        }
        printer.outdent();
        printer.println("}");

        printer.println();
    }

    /**
     * <pre>
     * SystemObject.register(TYPE_NAME, ExpressionNumberKind.fromJson);
     * </pre>
     */
    private static void register(final Class<Enum<?>> enumClass,
                                 final IndentingPrinter printer) {
        printer.println("SystemObject.register(TYPE_NAME, " + enumClass.getSimpleName() + ".fromJson);");
    }

    private static CharSequence toKebabCase(final String name) {
        final StringBuilder b = new StringBuilder();

        for (final char c : name.toCharArray()) {
            if (b.length() > 0 && Character.isUpperCase(c)) {
                b.append('-');
            }
            b.append(Character.toLowerCase(c));
        }

        return b;
    }

    private static CharSequence capitalCase(final String name) {
        final StringBuilder b = new StringBuilder();

        char previous = '_';
        for (char c : name.toCharArray()) {
            b.append(
                    '_' == previous ?
                            Character.toUpperCase(c) :
                            Character.toLowerCase(c)
            );
            previous = c;
        }

        return b;
    }

    private static List<Enum<?>> values(final Class<Enum<?>> enumClass) throws Exception {
        final Enum<?>[] values = (Enum<?>[]) enumClass.getMethod("values")
                .invoke(null);
        return Arrays.asList(values);
    }
}
