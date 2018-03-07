/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

/**
 * This class is representation of double constant.
 *
 * @author Timur Shaidullin
 */
public class DoubleExpression extends PreparedExpression<Double> {
    public DoubleExpression(Double constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    public Double getValue() {
        return value;
    }
}
