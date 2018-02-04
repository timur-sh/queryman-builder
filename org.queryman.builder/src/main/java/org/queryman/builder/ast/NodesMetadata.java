/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.Keywords;
import org.queryman.builder.Operators;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.token.Keyword;

import static org.queryman.builder.PostgreSQL.keyword;

/**
 * List of all configured {@link Node Metadata} objects.
 *
 * @author Timur Shaidullin
 */
public final class NodesMetadata {
    public static final NodeMetadata SELECT             = new NodeMetadata(Keywords.SELECT);
    public static final NodeMetadata SELECT_DISTINCT    = new NodeMetadata(new Keyword("SELECT DISTINCT"));
    public static final NodeMetadata SELECT_DISTINCT_ON = new NodeMetadata(new Keyword("SELECT DISTINCT ON"));

    public static final NodeMetadata FROM = new NodeMetadata(Keywords.FROM);

    public static final NodeMetadata GROUP_BY = new NodeMetadata(Keywords.GROUP_BY);

    public static final NodeMetadata ORDER_BY = new NodeMetadata(Keywords.ORDER_BY);

    public static final NodeMetadata WHERE = new NodeMetadata(Keywords.WHERE);

    public static final NodeMetadata LIMIT = new NodeMetadata(Keywords.LIMIT);
    public static final NodeMetadata OFFSET = new NodeMetadata(Keywords.OFFSET);


    public static final NodeMetadata EMPTY_GROUPED = new NodeMetadata(keyword(""), 0, true);
    public static final NodeMetadata EMPTY         = new NodeMetadata(keyword(""));

    public static final NodeMetadata AND = new NodeMetadata(Operators.AND);
    public static final NodeMetadata AND_NOT = new NodeMetadata(Operators.AND_NOT);
    public static final NodeMetadata OR = new NodeMetadata(Operators.OR);
    public static final NodeMetadata OR_NOT = new NodeMetadata(Operators.OR_NOT);


}
