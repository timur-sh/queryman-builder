/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

/**
 * Represent an array of constant expressions.
 * <code>
 *     ARRAY[1, 2, 3 [,...]]
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class ArrayExpression<T> extends Expression {

    /**
     * Contains a variables for ARRAY and LIST expressions.
     */
    private T[] arr;

    private ArrayExpression(String constant) {
        super(constant);
    }

    @SafeVarargs
    public ArrayExpression(T... constants) {
        this("");
        arr = constants;
    }

    /**
     * @return an array of values e.g. ARRAY[1, 2 [,...]]
     */
    @Override
    protected String prepareName() {
        if (arr == null)
            return "ARRAY[]";

        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = String.valueOf(arr[i]);
        }

        return "ARRAY[" + String.join(", ", result) + "]";
    }
}
