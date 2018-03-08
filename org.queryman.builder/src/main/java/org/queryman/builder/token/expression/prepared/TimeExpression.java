/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.sql.Time;


/**
 * This class is representation of time constant.
 *
 * @author Timur Shaidullin
 */
public class TimeExpression extends PreparedExpression<Time> {
    public TimeExpression(Time constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return String.format("'%s'", name);
    }

    @Override
    public Time getValue() {
        return value;
    }
}
