/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;
import org.queryman.builder.token.PreparedExpression;

import java.util.Arrays;

import static org.queryman.builder.utils.ArrayUtils.toExpressions;

/**
 * Represent a list of value expressions.
 * <p>
 * <code>
 * (1, 2, 3 [,...])
 * </code>
 *
 * @author Timur Shaidullin
 */
public class ListExpression<T> extends PreparedExpression {

    /**
     * Contains a variables for ARRAY and LIST expressions.
     */
    private Expression[] arr;

    private ListExpression(String constant) {
        super(constant);
    }

    @SafeVarargs
    public ListExpression(T... constants) {
        this("");
        arr = toExpressions(constants);
    }

    /**
     * @return a list of names e.g. (1, 2 [,...])
     */
    @Override
    protected String prepareName() {
        if (arr == null)
            return "()";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i].getName();
        }

        return "(" + String.join(", ", result) + ")";
    }

    @Override
    public String getPlaceholder() {
        if (arr == null)
            return "()";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] instanceof PreparedExpression)
                result[i] = ((PreparedExpression)arr[i]).getPlaceholder();
            else
                result[i] = arr[i].getName();
        }

        return "(" + String.join(", ", result) + ")";
    }

    @Override
    public PreparedExpression[] getValue() {
        return Arrays.stream(arr)
           .filter(v -> v instanceof PreparedExpression)
           .toArray(PreparedExpression[]::new);
    }
}
