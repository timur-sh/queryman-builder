/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * This is a ARRAY expressions. If you would use a prepared expression, see
 * {@link PreparedExpression}
 *
 * Commons use:
 * <code>
 *     // ARRAY[1, 2, 3 [,...]]
 *     PostgreSQL.asArray(1, 2, 3);
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class BytesExpression extends ArrayExpression<Byte> {

    /**
     * Contains variables for ARRAY expressions.
     */
    private Byte[] values;

    public BytesExpression(Byte[] constants) {
        super(constants);
        values = constants;
    }

    @Override
    public Byte[] getValue() {
        return values;
    }
}
