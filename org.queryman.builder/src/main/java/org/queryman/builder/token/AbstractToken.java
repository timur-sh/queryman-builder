/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

import org.queryman.builder.utils.StringUtils;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractToken implements Token {
    protected final String name;

    protected AbstractToken(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNonEmpty() {
        return !StringUtils.isEmpty(name);
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
