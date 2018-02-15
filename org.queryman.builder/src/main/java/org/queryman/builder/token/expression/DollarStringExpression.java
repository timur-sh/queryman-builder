/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.token.Expression;

/**
 * Represents dollar string asConstant. Variable is surrounded by single quotes:
 * Example:
 * <code>
 * $$string variable is here$$
 * </code>
 *
 * @author Timur Shaidullin
 */
public class DollarStringExpression extends Expression {
    /**
     * Tag name is used to surround dollar string. If no tag is given, the empty
     * {@code ""} string will be used.
     *
     * Example:
     * With tag name {@code type}: $type$ it contains any text $type$
     * Without tag name: $$ it contains any text $$
     */
    private String tagName = "";

    public DollarStringExpression(String constant, String tagName) {
        super(constant);
        this.tagName = tagName;
    }

    /**
     * @return a string surrounded by dollar singes string. e.g. $$string$$ or
     * $tag$string$tag$
     */
    @Override
    protected String prepareName() {
        if (isEmpty()) {
            return null;
        }

        String tag = "$" + tagName +"$";

        return tag + name + tag;
    }
}
