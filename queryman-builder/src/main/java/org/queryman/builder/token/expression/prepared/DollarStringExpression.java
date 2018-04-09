/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.util.Map;

/**
 * This is a representation of dollar string constant. Variable is surrounded
 * by single quotes.
 *
 * Example:
 * <code>
 * $$string variable is here$$
 * </code>
 *
 * @author Timur Shaidullin
 */
public class DollarStringExpression extends PreparedExpression<String> {
    /**
     * Specified tag name to surround a dollar string. If no tag is given, the empty
     * {@code ""} string will be used.
     *
     * Example:
     * With tag name {@code type}: <code>$type$ it contains any text $type$</code>
     * Without tag name: <code>$$ it contains any text $$</code>
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

    @Override
    public String getValue() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Map map) {
        map.put(map.size() + 1, this);
    }
}
