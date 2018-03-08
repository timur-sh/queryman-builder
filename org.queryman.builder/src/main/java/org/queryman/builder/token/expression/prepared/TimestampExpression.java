/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.sql.Timestamp;


/**
 * This class is representation of timestamp constant.
 *
 * @author Timur Shaidullin
 */
public class TimestampExpression extends PreparedExpression<Timestamp> {
    public TimestampExpression(Timestamp constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return String.format("'%s'", name);
    }

    @Override
    public Timestamp getValue() {
        return value;
    }
}
