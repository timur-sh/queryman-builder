/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;


/**
 * This class is representation of null constant.
 *
 * @author Timur Shaidullin
 */
public class NullExpression extends Expression {
    public NullExpression(Object constant) {
        super("NULL");
    }

    @Override
    protected String prepareName() {
        return name;
    }

}
