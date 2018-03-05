/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * Represent a list of value expressions.
 * <p>
 * <code>
 * (1, 2, 3 [,...])
 * </code>
 *
 * @author Timur Shaidullin
 */
public class ListExpression<T> extends Expression {

    /**
     * Contains a variables for ARRAY and LIST expressions.
     */
    private T[] arr;

    private ListExpression(String constant) {
        super(constant);
    }

    @SafeVarargs
    public ListExpression(T... constants) {
        this("");
        arr = constants;
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
            result[i] = asConstant(arr[i]).getName();
        }

        return "(" + String.join(", ", result) + ")";
    }
}
