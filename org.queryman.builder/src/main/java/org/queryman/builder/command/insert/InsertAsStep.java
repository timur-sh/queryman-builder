/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

/**
 * INSERT INTO .. AS step.
 *
 * @author Timur Shaidullin
 */
public interface InsertAsStep extends InsertColumnsStep {
    /**
     * Sets a substitute name for target table.
     *
     * @param alias alias of target table
     * @return insert columns step
     */
    InsertColumnsStep as(String alias);
}
