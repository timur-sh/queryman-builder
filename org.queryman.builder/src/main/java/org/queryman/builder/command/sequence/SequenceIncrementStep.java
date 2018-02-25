/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.sequence;

/**
 * @author Timur Shaidullin
 */
public interface SequenceIncrementStep extends SequenceMinValueStep {
    /**
     * Specifies an increment value. It may be as positive as negative
     * @param increment value which will be added to the current sequence,
     *                 when the a new value will created.
     * @return the next step
     */
    SequenceMinValueStep increment(long increment);

    /**
     * Specifies an increment value. It may be as positive as negative
     * @param increment value which will be added to the current sequence,
     *                 when the a new value will created.
     * @return the next step
     *
     * @see #increment(long) the synonim of it.
     */
    SequenceMinValueStep incrementBy(long increment);
}
