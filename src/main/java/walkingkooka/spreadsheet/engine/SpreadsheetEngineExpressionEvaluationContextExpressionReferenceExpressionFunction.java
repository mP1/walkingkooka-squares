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

package walkingkooka.spreadsheet.engine;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.store.SpreadsheetLabelStore;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationException;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Function} which may be passed to {@link walkingkooka.tree.expression.ExpressionEvaluationContexts#basic}
 * and acts as a bridge resolving {@link ExpressionReference} to a {@link Expression}.
 */
final class SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunction implements Function<ExpressionReference, Optional<Expression>> {

    /**
     * Factory that creates a new {@link SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunction}
     */
    static SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunction with(final SpreadsheetEngine engine,
                                                                                                  final SpreadsheetLabelStore labelStore,
                                                                                                  final SpreadsheetEngineContext context) {
        Objects.requireNonNull(engine, "engine");
        Objects.requireNonNull(labelStore, "labelStore");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunction(engine, labelStore, context);
    }

    /**
     * Private ctor.
     */
    private SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunction(final SpreadsheetEngine engine,
                                                                                              final SpreadsheetLabelStore labelStore,
                                                                                              final SpreadsheetEngineContext context) {
        this.engine = engine;
        this.labelStore = labelStore;
        this.context = context;
    }

    @Override
    public Optional<Expression> apply(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        final SpreadsheetCellReference cellReference = SpreadsheetEngineExpressionEvaluationContextExpressionReferenceExpressionFunctionSpreadsheetExpressionReferenceVisitor.reference(reference, this.labelStore);

        final SpreadsheetDelta delta = this.engine.loadCell(cellReference,
                SpreadsheetEngineEvaluation.COMPUTE_IF_NECESSARY,
                this.context);

        final SpreadsheetCell cell = delta.cells()
                .stream()
                .filter(c -> c.reference().equalsIgnoreReferenceKind(cellReference))
                .findFirst()
                .orElseThrow(() -> new ExpressionEvaluationException("Unknown cell reference " + reference));
        final SpreadsheetFormula formula = cell.formula();
        final Optional<SpreadsheetError> error = formula.error();
        if (error.isPresent()) {
            throw new ExpressionEvaluationException(error.get().value());
        }

        return formula.expression();
    }

    private final SpreadsheetEngine engine;
    private final SpreadsheetLabelStore labelStore;
    private final SpreadsheetEngineContext context;

    @Override
    public String toString() {
        return this.engine.toString();
    }
}
