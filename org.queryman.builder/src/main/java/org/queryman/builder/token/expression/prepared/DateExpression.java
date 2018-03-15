/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.sql.Date;
import java.util.Map;


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
        return String.format("'%s'", name);
    }

    @Override
    public Date getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Map map) {
        map.put(map.size() + 1, this);
    }
}
