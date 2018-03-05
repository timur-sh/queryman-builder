/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.token.expression.prepared.DollarStringExpression;
import org.queryman.builder.token.expression.prepared.StringExpression;

/**
 * This is a marker class that denotes a prepared constant and must be extended
 * by all constant classes like numeric, string, date etc.
 *
 * @see org.queryman.builder.token.expression.ConstantExpression
 * @see DollarStringExpression
 * @see StringExpression
 *
 * @author Timur Shaidullin
 */
public abstract class PreparedExpression<T> extends Expression {
    protected T value;

    public PreparedExpression(T constant) {
        super(String.valueOf(constant));

        value = constant;
    }

    protected abstract T getValue();
}