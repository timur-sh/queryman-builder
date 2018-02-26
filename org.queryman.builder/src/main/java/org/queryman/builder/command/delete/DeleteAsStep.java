/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.delete;

/**
 * DELETE .. AS .. clause.
 *
 * @author Timur Shaidullin
 */
public interface DeleteAsStep extends DeleteUsingStep {
    /**
     * A substitute name for the target table.
     *
     * @param alias alias
     * @return delete using step
     */
    DeleteUsingStep as(String alias);
}
