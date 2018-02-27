/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * This class represents a prepared parameter. It may be used to detach a
 * user provided parameter from SQL query.
 *
 * @see org.queryman.builder.token.expression.ConstantExpression
 * @see org.queryman.builder.token.expression.DollarStringExpression
 * @see org.queryman.builder.token.expression.StringExpression
 *
 * @author Timur Shaidullin
 */
public abstract class PreparedExpression extends Expression {
    private boolean prepared;

    public PreparedExpression(String constant) {
        super(constant);
    }

    public boolean isPrepared() {
        return prepared;
    }

    public PreparedExpression setPrepared(boolean prepare) {
        prepared = prepare;
        return this;
    }
}