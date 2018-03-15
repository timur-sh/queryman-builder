/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;
import org.queryman.builder.token.PreparedExpression;
import org.queryman.builder.utils.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.queryman.builder.Queryman.asName;

/**
 * @author Timur Shaidullin
 */
public class FuncExpression extends PreparedExpression {
    private Expression expression;

    public FuncExpression(String constant) {
        super(constant);
    }

    public FuncExpression(String name, Expression expression) {
        this(name);
        this.expression = expression;
    }

    public FuncExpression(String name, Expression... expression) {
        this(name);

        Objects.requireNonNull(expression);
        String[] expr = Arrays.stream(expression).map(Expression::getName).toArray(String[]::new);
        this.expression = asName(String.join(", ", expr));
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

        } else if (expression instanceof SubQueryExpression) {
            return String.join("", name, result);

        } else if (expression instanceof ListExpression)
            return String.join("", name, result);

        return String.join("", name, "(", result, ")");
    }

    @Override
    public Object getValue() {
        // Method must not be called because it contains list of other
        // expressions those must be bound using #bind(Map) method
        throw new IllegalStateException("Method must not be called");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Map map) {

//        Arrays.stream(arr)
//           .filter(v -> v instanceof PreparedExpression)
//           .forEach(v -> {
//               map.put(map.size() + 1, v);
//           });
    }
}
