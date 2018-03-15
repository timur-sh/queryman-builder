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
 * This class is representation of long constant.
 *
 * @author Timur Shaidullin
 */
public class LongExpression extends PreparedExpression<Long> {
    public LongExpression(Long constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return name;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Map map) {
        map.put(map.size() + 1, this);
    }
}
