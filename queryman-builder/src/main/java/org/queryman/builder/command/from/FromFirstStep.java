/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.from;

/**
 * @author Timur Shaidullin
 */
public interface FromFirstStep extends FromTablesampleStep {
    /**
     * Specify an alias.
     */
    FromTablesampleStep as(String alias);
}
