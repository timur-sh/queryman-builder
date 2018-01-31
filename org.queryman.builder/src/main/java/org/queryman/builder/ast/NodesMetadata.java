/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * List of all configured {@link Node Metadata} objects.
 *
 * @author Timur Shaidullin
 */
public final class NodesMetadata {
    public static final NodeMetadata SELECT             = new NodeMetadata("SELECT");
    public static final NodeMetadata SELECT_DISTINCT    = new NodeMetadata("SELECT DISTINCT");
    public static final NodeMetadata SELECT_DISTINCT_ON = new NodeMetadata("SELECT DISTINCT ON");

    public static final NodeMetadata FROM = new NodeMetadata("FROM");

    public static final NodeMetadata GROUP_BY = new NodeMetadata("GROUP BY");

    public static final NodeMetadata ORDER_BY = new NodeMetadata("ORDER BY");

    public static final NodeMetadata WHERE = new NodeMetadata("WHERE");

    public static final NodeMetadata LIMIT = new NodeMetadata("LIMIT");
    public static final NodeMetadata OFFSET = new NodeMetadata("OFFSET");


    public static final NodeMetadata EMPTY_GROUPED = new NodeMetadata("", 0, true);
    public static final NodeMetadata EMPTY         = new NodeMetadata("");

    public static final NodeMetadata AND = new NodeMetadata("AND");
    public static final NodeMetadata AND_NOT = new NodeMetadata("AND NOT");
    public static final NodeMetadata OR  = new NodeMetadata("OR");
    public static final NodeMetadata OR_NOT  = new NodeMetadata("OR NOT");


}
