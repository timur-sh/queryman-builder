/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.impl;

import org.queryman.builder.Query;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractQuery implements Query {
    @Override
    public String getSQL() {
        return null;
    }
}
