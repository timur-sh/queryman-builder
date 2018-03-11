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
public interface SelectLockingStep extends SelectFinalStep {
    /**
     * Specifies a locking mode name FOR UPDATE.
     *
     * @return a step to specify locking table or tables
     */
    SelectLockingOfTableStep forUpdate();

    /**
     * Specifies a locking mode name FOR NO KEY UPDATE.
     *
     * @return a step to specify locking table or tables
     */
    SelectLockingOfTableStep forNoKeyUpdate();

    /**
     * Specifies a locking mode name FOR SHARE.
     *
     * @return a step to specify locking table or tables
     */
    SelectLockingOfTableStep forShare();

    /**
     * Specifies a locking mode name FOR KEY SHARE.
     *
     * @return a step to specify locking table or tables
     */
    SelectLockingOfTableStep forKeyShare();
}
