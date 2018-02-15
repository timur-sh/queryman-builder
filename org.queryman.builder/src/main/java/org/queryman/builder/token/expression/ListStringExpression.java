/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

/**
 * Represent a list of string expressions.
 * <p>
 * <code>
 * ('one', 'two', 'three', [,...])
 * </code>
 *
 * @author Timur Shaidullin
 */
public class ListStringExpression<T> extends Expression {

    /**
     * Contains a variables for ARRAY and LIST expressions.
     */
    private T[] arr;

    private ListStringExpression(String constant) {
        super(constant);
    }

    @SafeVarargs
    public ListStringExpression(T... constants) {
        this("");
        arr = constants;
    }

    /**
     * @return a list of string names. e.g. ('1', '2' [,...])
     */
    @Override
    protected String prepareName() {
        if (arr == null)
            return "()";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = toPostgresqlString(String.valueOf(arr[i]));
        }

        return "(" + String.join(", ", result) + ")";
    }
}
