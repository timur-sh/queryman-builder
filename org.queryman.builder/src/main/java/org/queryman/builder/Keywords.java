/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Keyword;

/**
 * Collection of key words are used by Queryman to build SQL query.
 *
 * @author Timur Shaidullin
 */
public class Keywords {
    public static final Keyword SELECT             = new Keyword("SELECT");
    public static final Keyword SELECT_ALL         = new Keyword("SELECT ALL");
    public static final Keyword SELECT_DISTINCT    = new Keyword("SELECT DISTINCT");

    public static final Keyword FROM        = new Keyword("FROM");
    public static final Keyword ONLY        = new Keyword("ONLY");

    public static final Keyword UNION          = new Keyword("UNION");
    public static final Keyword UNION_ALL      = new Keyword("UNION ALL");
    public static final Keyword UNION_DISTINCT = new Keyword("UNION DISTINCT");

    public static final Keyword INTERSECT          = new Keyword("INTERSECT");
    public static final Keyword INTERSECT_ALL      = new Keyword("INTERSECT ALL");
    public static final Keyword INTERSECT_DISTINCT = new Keyword("INTERSECT DISTINCT");

    public static final Keyword EXCEPT          = new Keyword("EXCEPT");
    public static final Keyword EXCEPT_ALL      = new Keyword("EXCEPT ALL");
    public static final Keyword EXCEPT_DISTINCT = new Keyword("EXCEPT DISTINCT");

    public static final Keyword ON    = new Keyword("ON");
    public static final Keyword USING = new Keyword("USING");

    public static final Keyword JOIN         = new Keyword("JOIN");
    public static final Keyword INNER_JOIN   = new Keyword("INNER JOIN");
    public static final Keyword LEFT_JOIN    = new Keyword("LEFT JOIN");
    public static final Keyword RIGHT_JOIN   = new Keyword("RIGHT JOIN");
    public static final Keyword FULL_JOIN    = new Keyword("FULL JOIN");
    public static final Keyword CROSS_JOIN   = new Keyword("CROSS JOIN");
    public static final Keyword NATURAL_JOIN = new Keyword("NATURAL JOIN");

    public static final Keyword GROUP_BY = new Keyword("GROUP BY");

    public static final Keyword ORDER_BY = new Keyword("ORDER BY");

    public static final Keyword WHERE = new Keyword("WHERE");

    public static final Keyword HAVING = new Keyword("HAVING");

    public static final Keyword LIMIT = new Keyword("LIMIT");

    public static final Keyword OFFSET = new Keyword("OFFSET");

    public static final Keyword EXISTS = new Keyword("EXISTS");
    public static final Keyword ANY    = new Keyword("ANY");
    public static final Keyword SOME   = new Keyword("SOME");
    public static final Keyword ALL    = new Keyword("ALL");
}
