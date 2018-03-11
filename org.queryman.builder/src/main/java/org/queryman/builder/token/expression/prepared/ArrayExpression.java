/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.util.Arrays;

import static org.queryman.builder.Queryman.asConstant;

/**
 * This is a ARRAY expressions. If you would use a prepared expression, see
 * {@link org.queryman.builder.token.PreparedExpression}
 * <p>
 * Commons use:
 * <code>
 *     // ARRAY[1, 2, 3 [,...]]
 *     Queryman.asArray(1, 2, 3);
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class ArrayExpression<T> extends PreparedExpression {

    /**
     * Contains variables for ARRAY expressions.
     */
    private T[] values;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public ArrayExpression(T... constants) {
        super(null);
        values = constants;
    }

    /**
     * @return an array of values e.g. ARRAY[1, 2 [,...]]
     */
    @Override
    protected String prepareName() {
        if (values == null)
            return "ARRAY[]";

        String[] result = Arrays.stream(values)
           .map(v -> asConstant(v).getName())
           .toArray(String[]::new);

        return "ARRAY[" + String.join(", ", result) + "]";
    }

    @Override
    public Object[] getValue() {
        return values;
    }
}
