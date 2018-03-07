/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.math.BigDecimal;


/**
 * This class is representation of big decimal constant.
 *
 * @author Timur Shaidullin
 */
public class BigDecimalExpression extends PreparedExpression<BigDecimal> {
    public BigDecimalExpression(BigDecimal constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
