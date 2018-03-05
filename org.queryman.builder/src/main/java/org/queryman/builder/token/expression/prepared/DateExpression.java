/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.sql.Date;


/**
 * This class is representation of date constant.
 *
 * @author Timur Shaidullin
 */
public class DateExpression extends PreparedExpression<Date> {
    public DateExpression(Date constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    protected Date getValue() {
        return value;
    }
}
