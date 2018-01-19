/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.command.select.Select;
import org.queryman.builder.command.select.SelectInitialStep;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl implements SelectInitialStep, Select {
    @Override
    public Select select() {
        return null;
    }

    @Override
    public Select selectDistinct() {
        return null;
    }

    @Override
    public Select selectAll() {
        return null;
    }

    @Override
    public Select selectOn() {
        return null;
    }

    @Override
    public String getSQL() {
        return null;
    }
}
