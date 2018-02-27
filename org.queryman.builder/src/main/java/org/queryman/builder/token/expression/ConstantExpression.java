/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.PreparedExpression;

/**
 * Represent a default expression. It is not surrounded by quotes. It
 * is printed as is.
 * <p>
 * <code>
 * table_name
 * $n1
 * LIST[1]
 * 234.11
 * .50
 * .2E+1
 * </code>
 *
 * @author Timur Shaidullin
 */
public class ConstantExpression extends PreparedExpression {
    public ConstantExpression(String constant) {
        super(constant);
    }

    public ConstantExpression(Number constant) {
        this(constant.toString());
    }

    @Override
    protected String prepareName() {
        return name == null ? "NULL" : name;
    }
}
