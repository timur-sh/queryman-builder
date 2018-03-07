/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

/**
 * This class is representation of short constant.
 *
 * @author Timur Shaidullin
 */
public class ShortExpression extends PreparedExpression<Short> {
    public ShortExpression(Short constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    public Short getValue() {
        return value;
    }
}
