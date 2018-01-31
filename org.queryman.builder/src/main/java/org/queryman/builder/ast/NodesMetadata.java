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
    public static final NodeMetadata SELECT             = new NodeMetadata("select");
    public static final NodeMetadata SELECT_DISTINCT    = new NodeMetadata("select distinct");
    public static final NodeMetadata SELECT_DISTINCT_ON = new NodeMetadata("select distinct on");

    public static final NodeMetadata FROM = new NodeMetadata("from");

    public static final NodeMetadata GROUP_BY = new NodeMetadata("group by");

    public static final NodeMetadata ORDER_BY = new NodeMetadata("order by");

    public static final NodeMetadata WHERE = new NodeMetadata("where");

    public static final NodeMetadata LIMIT = new NodeMetadata("limit");


    public static final NodeMetadata EMPTY_GROUPED = new NodeMetadata("", 0, true);
    public static final NodeMetadata EMPTY         = new NodeMetadata("");

    public static final NodeMetadata AND = new NodeMetadata("and");
    public static final NodeMetadata OR  = new NodeMetadata("or");


}
