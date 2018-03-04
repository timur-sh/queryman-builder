/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * This is a ARRAY expressions. If you would use a prepared expression, see
 * {@link org.queryman.builder.token.PreparedExpression}
 *
 * Commons use:
 * <code>
 *     // ARRAY[1, 2, 3 [,...]]
 *     PostgreSQL.asArray(1, 2, 3);
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class ArrayExpression<T> extends Expression {

    /**
     * Contains variables for ARRAY expressions.
     */
    private T[] values;

    @SafeVarargs
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

        String [] result = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = asConstant(values[i]).getName();
        }



        return "ARRAY[" + String.join(", ", result) + "]";
    }
}
