/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.Keywords;
import org.queryman.builder.Operators;

import static org.queryman.builder.PostgreSQL.keyword;

/**
 * List of all configured {@link Node Metadata} objects.
 *
 * @author Timur Shaidullin
 */
public final class NodesMetadata {
    public static final NodeMetadata SELECT          = new NodeMetadata(Keywords.SELECT);
    public static final NodeMetadata SELECT_ALL      = new NodeMetadata(Keywords.SELECT_ALL);
    public static final NodeMetadata SELECT_DISTINCT = new NodeMetadata(Keywords.SELECT_DISTINCT);

    public static final NodeMetadata FROM = new NodeMetadata(Keywords.FROM);
    public static final NodeMetadata ONLY = new NodeMetadata(Keywords.ONLY);

    public static final NodeMetadata ON           = new NodeMetadata(Keywords.ON);
    public static final NodeMetadata USING        = new NodeMetadata(Keywords.USING);
    public static final NodeMetadata JOIN         = new NodeMetadata(Keywords.JOIN);
    public static final NodeMetadata INNER_JOIN   = new NodeMetadata(Keywords.INNER_JOIN);
    public static final NodeMetadata LEFT_JOIN    = new NodeMetadata(Keywords.LEFT_JOIN);
    public static final NodeMetadata RIGHT_JOIN   = new NodeMetadata(Keywords.RIGHT_JOIN);
    public static final NodeMetadata FULL_JOIN    = new NodeMetadata(Keywords.FULL_JOIN);
    public static final NodeMetadata CROSS_JOIN   = new NodeMetadata(Keywords.CROSS_JOIN);
    public static final NodeMetadata NATURAL_JOIN = new NodeMetadata(Keywords.NATURAL_JOIN);

    public static final NodeMetadata GROUP_BY = new NodeMetadata(Keywords.GROUP_BY);

    public static final NodeMetadata ORDER_BY = new NodeMetadata(Keywords.ORDER_BY);

    public static final NodeMetadata WHERE = new NodeMetadata(Keywords.WHERE);

    public static final NodeMetadata HAVING = new NodeMetadata(Keywords.HAVING);

    public static final NodeMetadata LIMIT  = new NodeMetadata(Keywords.LIMIT);
    public static final NodeMetadata OFFSET = new NodeMetadata(Keywords.OFFSET);

    public static final NodeMetadata EXISTS = new NodeMetadata(Keywords.EXISTS);
    public static final NodeMetadata ANY    = new NodeMetadata(Keywords.ANY);
    public static final NodeMetadata SOME   = new NodeMetadata(Keywords.SOME);
    public static final NodeMetadata ALL    = new NodeMetadata(Keywords.ALL);

    public static final NodeMetadata WHERE_CURRENT_OF = new NodeMetadata(Keywords.WHERE_CURRENT_OF);
    public static final NodeMetadata RETURNING        = new NodeMetadata(Keywords.RETURNING);
    public static final NodeMetadata AS               = new NodeMetadata(Keywords.AS);

    public static final NodeMetadata EMPTY_GROUPED = new NodeMetadata(keyword(""), 0, true);
    public static final NodeMetadata EMPTY         = new NodeMetadata(keyword(""));

    public static final NodeMetadata BETWEEN = new NodeMetadata(Operators.BETWEEN);

    public static final NodeMetadata AND     = new NodeMetadata(Operators.AND);
    public static final NodeMetadata AND_NOT = new NodeMetadata(Operators.AND_NOT);
    public static final NodeMetadata OR      = new NodeMetadata(Operators.OR);
    public static final NodeMetadata OR_NOT  = new NodeMetadata(Operators.OR_NOT);


    public static final NodeMetadata NULLS  = new NodeMetadata(keyword("NULLS"));
}
