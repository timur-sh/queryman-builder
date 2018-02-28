/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.create_sequence;

/**
 * Specifies how many values are be preallocated and stored in the memory.
 *
 * @author Timur Shaidullin
 */
public interface SequenceCacheStep extends SequenceCycleStep {
    /**
     * Specifies how many values are be preallocated and stored in the memory.
     *
     * @param cache cached values
     * @return the next step
     */
    SequenceCycleStep cache(long cache);
}
