/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.Select;

/**
 * The {@code SELECT} statement. This {@code class} must be used as a start
 * point of select's query.
 *
 * @author Timur Shaidullin
 */
public interface SelectInitialStep extends Query {
    SelectFromStep select();

    Select selectDistinct();

    Select selectAll();
}
