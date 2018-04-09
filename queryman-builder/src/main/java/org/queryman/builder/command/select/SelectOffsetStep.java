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
public interface SelectOffsetStep extends SelectLockingStep {
    /**
     * Specifies the offset start.
     *
     * @param offset offset start
     * @return select locking step
     *
     * @see #offset(Expression)
     */
    SelectLockingStep offset(long offset);

    /**
     * Specifies the offset start.
     *
     * @param offset offset start
     * @return select locking step
     */
    SelectLockingStep offset(Expression offset);
}
