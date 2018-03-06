/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Keyword;

import static org.queryman.builder.PostgreSQL.keyword;

/**
 * Collection of key words are used by Queryman to build SQL query.
 *
 * @author Timur Shaidullin
 */
public class Keywords {
    public static final Keyword SELECT          = new Keyword("SELECT");
    public static final Keyword SELECT_ALL      = new Keyword("SELECT ALL");
    public static final Keyword SELECT_DISTINCT = new Keyword("SELECT DISTINCT");

    public static final Keyword FROM = new Keyword("FROM");
    public static final Keyword ONLY = new Keyword("ONLY");

    public static final Keyword UNION          = new Keyword("UNION");
    public static final Keyword UNION_ALL      = new Keyword("UNION ALL");

    public static final Keyword INTERSECT          = new Keyword("INTERSECT");
    public static final Keyword INTERSECT_ALL      = new Keyword("INTERSECT ALL");

    public static final Keyword EXCEPT          = new Keyword("EXCEPT");
    public static final Keyword EXCEPT_ALL      = new Keyword("EXCEPT ALL");

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

    public static final Keyword TEMP      = keyword("TEMP");
    public static final Keyword TEMPORARY = keyword("TEMPORARY");

    public static final Keyword CREATE_SEQUENCE                    = keyword("CREATE SEQUENCE");
    public static final Keyword CREATE_TEMP_SEQUENCE               = keyword("CREATE TEMP SEQUENCE");
    public static final Keyword CREATE_TEMP_SEQUENCE_IF_NOT_EXISTS = keyword("CREATE TEMP SEQUENCE IF NOT EXISTS");
    public static final Keyword CREATE_SEQUENCE_IF_NOT_EXISTS      = keyword("CREATE SEQUENCE IF NOT EXISTS");


    public static final Keyword INCREMENT    = keyword("INCREMENT");
    public static final Keyword INCREMENT_BY = keyword("INCREMENT BY");
    public static final Keyword MINVALUE     = keyword("MINVALUE");
    public static final Keyword MAXVALUE     = keyword("MAXVALUE");
    public static final Keyword START        = keyword("START");
    public static final Keyword START_WITH   = keyword("START WITH");

    public static final Keyword WITH     = keyword("WITH");
    public static final Keyword CACHE    = keyword("CACHE");
    public static final Keyword CYCLE    = keyword("CYCLE");
    public static final Keyword NO_CYCLE = keyword("NO CYCLE");
    public static final Keyword OWNED_BY = keyword("OWNED BY");

    public static final Keyword IF_NOT_EXISTS = keyword("IF NOT EXISTS");

    public static final Keyword AS        = keyword("AS");
    public static final Keyword RETURNING = keyword("RETURNING");


    public static final Keyword DELETE_FROM      = keyword("DELETE FROM");
    public static final Keyword DELETE_FROM_ONLY = keyword("DELETE FROM ONLY");

    public static final Keyword UPDATE      = keyword("UPDATE");
    public static final Keyword UPDATE_ONLY = keyword("UPDATE ONLY");

    public static final Keyword INSERT_INTO             = keyword("INSERT INTO");
    public static final Keyword OVERRIDING_SYSTEM_VALUE = keyword("OVERRIDING SYSTEM VALUE");
    public static final Keyword OVERRIDING_USER_VALUE   = keyword("OVERRIDING USER VALUE");
    public static final Keyword DEFAULT_VALUES          = keyword("DEFAULT VALUES");
    public static final Keyword VALUES                  = keyword("VALUES");
    public static final Keyword ON_CONFLICT             = keyword("ON CONFLICT");
    public static final Keyword ON_CONSTRAINT           = keyword("ON CONSTRAINT");
    public static final Keyword DO_NOTHING              = keyword("DO NOTHING");
    public static final Keyword DO_UPDATE               = keyword("DO UPDATE");

    public static final Keyword SET = keyword("SET");

    public static final Keyword COLLATE = keyword("COLLATE");


    public static final Keyword WHERE_CURRENT_OF = keyword("WHERE CURRENT OF");
}
