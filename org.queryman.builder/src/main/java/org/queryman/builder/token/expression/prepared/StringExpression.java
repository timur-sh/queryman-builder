/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

/**
 * This class is representation of string constant. The variable is surrounded
 * by single quotes.
 *
 * Example:
 * <code>
 * 'string variable is here'
 * 'string variable''s here'
 * </code>
 *
 * @author Timur Shaidullin
 */
public class StringExpression extends PreparedExpression<String> {
    public StringExpression(String constant) {
        super(constant);
    }

    @Override
    public String getValue() {
        return name;
    }

    /**
     * @return a string surrounded by single quote string. e.g. 'string'
     */
    @Override
    protected String prepareName() {
        if (isEmpty()) {
            return null;
        }

        return toPostgreSQLString(name);
    }
}
