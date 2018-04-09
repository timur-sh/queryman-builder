/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

/**
 * INSERT INTO .. OVERRIDING {SYSTEM | USER} VALUE.. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertOverridingStep extends InsertValuesStep {
    /**
     * Specifies a possible override IDENTITY column value by user-provided
     * value.
     *
     * @return insert values step
     */
    InsertValuesStep overridingSystemValue();

    /**
     * @return insert values step
     */
    InsertValuesStep overridingUserValue();
}
