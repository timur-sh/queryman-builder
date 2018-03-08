/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import java.sql.Connection;

/**
 * Any prepared expression behaves itself as a simple string in case, when
 * it is used in SQL string.
 *
 * Another behavior of it is distinct from the above, this is a prepared statement.
 * The value of a expression is replaced by placeholder <code>?</code>, then
 * the value of the expression is invoked included {@link java.sql.PreparedStatement}
 * using one of <code>setXXX</code> methods.
 *
 * @see org.queryman.builder.Query#buildPreparedStatement(Connection)
 *
 * @author Timur Shaidullin
 */
public abstract class PreparedExpression<T> extends Expression {
    protected T value;

    public PreparedExpression(T constant) {
        super(String.valueOf(constant));

        value = constant;
    }

    /**
     * Returns a placeholder to use in SQL string.
     *
     * @return a placeholder
     */
    public String getPlaceholder() {
        return "?" + getCastExpression();
    }

    /**
     * @return value of prepared expression
     */
    public abstract T getValue();
}