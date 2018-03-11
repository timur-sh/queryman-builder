/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface SelectLockingOfTableStep extends SelectLockingWaitingStep {
    /**
     * Specifies tables, rows of those are locking.
     *
     * @param tables list of tables
     * @return a step to specify waiting mode
     *
     * @see #of(Expression...)
     */
    SelectLockingWaitingStep of(String... tables);

    /**
     * Specifies tables, rows of those are locking.
     *
     * @param tables list of tables
     * @return a step to specify waiting mode
     */
    SelectLockingWaitingStep of(Expression... tables);
}
