/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.Query;

/**
 * WITH .. (columns, column) AS .. step.
 *
 * @author Timur Shaidullin
 */
public interface WithAsStep {
    /**
     * Specifies sub-queries
     *
     * @param queries list of queries
     * @return select first step
     */
    SelectFirstStep as(Query... queries);
}
