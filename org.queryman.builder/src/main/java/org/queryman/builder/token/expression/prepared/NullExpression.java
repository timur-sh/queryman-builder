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
 * This class is representation of null constant.
 *
 * @author Timur Shaidullin
 */
public class NullExpression extends PreparedExpression<Object> {
    public NullExpression(Object constant) {
        super("NULL");
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    protected Object getValue() {
        return null;
    }
}
