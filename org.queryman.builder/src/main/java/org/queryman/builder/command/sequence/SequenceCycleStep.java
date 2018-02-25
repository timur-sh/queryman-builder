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
public interface SequenceCycleStep extends SequenceOwnerStep {
    /**
     * The sequence wraps around when it achieves the maxvalue or minvalue.
     *
     * @return the next step
     */
    SequenceOwnerStep cycle();

    /**
     * When sequence is reached to maximum or minimum value, the attempt to call
     * <code>nextval</code> will return an error.
     * @return the next step
     */
    SequenceOwnerStep noCycle();
}
