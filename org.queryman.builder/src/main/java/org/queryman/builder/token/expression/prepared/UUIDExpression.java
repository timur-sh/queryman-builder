/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression.prepared;

import org.queryman.builder.token.PreparedExpression;

import java.util.UUID;

/**
 * This class is representation of uuid constant.
 *
 * @author Timur Shaidullin
 */
public class UUIDExpression extends PreparedExpression<UUID> {
    public UUIDExpression(UUID constant) {
        super(constant);
    }

    @Override
    protected String prepareName() {
        return String.format("'%s'", name);
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
