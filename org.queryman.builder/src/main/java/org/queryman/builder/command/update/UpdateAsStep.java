/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

/**
 * UPDATE .. AS .. clause.
 *
 * @author Timur Shaidullin
 */
public interface UpdateAsStep extends UpdateSetStep {
    /**
     * A substitute name for the target table.
     *
     * @param alias alias
     * @return update using step
     */
    UpdateSetStep as(String alias);
}
