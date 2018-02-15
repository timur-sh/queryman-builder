/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

/**
 * Represent an array of string expressions.
 * <code>
 *     ARRAY['one', 'two', 'three', [,...]]
 * </code>
 *
 * @author Timur Shaidullin
 */
public class ArrayStringExpression<T> extends Expression {

    /**
     * Contains a variables for ARRAY and LIST expressions.
     */
    private T[] arr;

    private ArrayStringExpression(String constant) {
        super(constant);
    }

    @SafeVarargs
    public ArrayStringExpression(T... constants) {
        this("");
        arr = constants;
    }

    /**
     * @return an array of strings. e.g. ARRAY['1', '2' [,...]]
     */
    @Override
    protected String prepareName() {
        if (arr == null)
            return "ARRAY[]";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = toPostgresqlString(String.valueOf(arr[i]));
        }

        return "ARRAY[" + String.join(", ", result) + "]";
    }
}
