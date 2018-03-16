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

/**
 * @author Timur Shaidullin
 */
public class FuncExpression extends PreparedExpression {
    private Expression[] expressions;

    public FuncExpression(String constant) {
        super(constant);
    }

//    public FuncExpression(String name, Expression expression) {
//        this(name, new Expression[] {expression});
//    }

    public FuncExpression(String name, Expression... expressions) {
        this(name);

        Objects.requireNonNull(expressions);
//        String[] expr = Arrays.stream(expression).map(Expression::getName).toArray(String[]::new);
        this.expressions = expressions;
    }

    @Override
    protected String prepareName() {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        StringBuilder result          = new StringBuilder();
        boolean       needParentheses = true;

        for (Expression exp : expressions) {
            if (
               (exp instanceof SubQueryExpression || exp instanceof ListExpression)
                  && expressions.length == 1
               ) {
                needParentheses = false;
            }

            result.append(exp.getName());
        }

        if (Objects.equals(name.toUpperCase(), "VALUES")) {
            if (StringUtils.isEmpty(outputName))
                return String.join("", name, result);
            else
                return "(" + name + result + ")";
        }

        return needParentheses ? name + "(" + result + ")" : name + result;
    }

    @Override
    public String getPlaceholder() {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        StringBuilder result = buildExpression(true);

        if (Objects.equals(name.toUpperCase(), "VALUES")) {
            if (StringUtils.isEmpty(outputName))
                return String.join("", name, result);
            else
                return "(" + name + result + ")";

        }

        String builtExpression = needParentheses ? name + "(" + result + ")" : name + result;

        return builtExpression + getCastExpression();
    }

    private String buildExpression(boolean isPrepared) {
        boolean needParentheses = Arrays.stream(expressions)
           .filter(v -> v instanceof SubQueryExpression || v instanceof ListExpression)
           .count() == 1;

        String[] expr = Arrays.stream(expressions)
           .map(v -> {
               if (isPrepared)
                   return ((PreparedExpression) v).getPlaceholder();
               return  v.getName();
           })
           .toArray(String[]::new);

        String result = String.join(", ", expr);


        if (Objects.equals(name.toUpperCase(), "VALUES")) {
            if (StringUtils.isEmpty(outputName))
                return String.join("", name, result);
            else
                return "(" + name + result + ")";

        }

        String builtExpression = needParentheses ? name + "(" + result + ")" : name + result;

        return builtExpression + getCastExpression();
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
        Arrays.stream(expressions)
           .filter(v -> v instanceof PreparedExpression)
           .forEach(p -> ((PreparedExpression) p).bind(map));
    }
}
