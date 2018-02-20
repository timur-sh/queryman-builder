/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;
import org.queryman.builder.utils.StringUtils;

import java.util.Arrays;
import java.util.Objects;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * @author Timur Shaidullin
 */
public class FuncExpression extends Expression {
    private Expression expression;

    public FuncExpression(String constant) {
        super(constant);
    }

    public FuncExpression(String name, Expression expression) {
        this(name);

        Objects.requireNonNull(expression);
        this.expression = expression;
    }

    public FuncExpression(String name, Expression... expression) {
        this(name);

        Objects.requireNonNull(expression);
        String[] expr = Arrays.stream(expression).map(Expression::getName).toArray(String[]::new);
        this.expression = asConstant(String.join(", ", expr));
    }

    @Override
    protected String prepareName() {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        String result = expression.getName();

        if (Objects.equals(name.toUpperCase(), "VALUES")) {
            if (StringUtils.isEmpty(outputName))
                return String.join("", name, result);
            else
                return "(" + name + result + ")";

        } else if (expression instanceof ListExpression || expression instanceof ListStringExpression)
            return String.join("", name, result);

        return String.join("", name, "(", result, ")");
    }
}
