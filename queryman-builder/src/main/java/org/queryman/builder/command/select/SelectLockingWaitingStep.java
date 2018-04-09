/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

/**
 * @author Timur Shaidullin
 */
public interface SelectLockingWaitingStep extends SelectLockingManySteps {
    /**
     * Specifies NOWAIT mode, meaning the SELECT statement will report an error,
     * if rows are locked, rather than will wait for rows.
     *
     * @return select final step
     */
    SelectLockingManySteps noWait();

    /**
     * Specifies SKIP LOCKED mode, meaning the SELECT statement will omit the
     * locked rows.
     *
     * @return select final step
     */
    SelectLockingManySteps skipLocked();
}
