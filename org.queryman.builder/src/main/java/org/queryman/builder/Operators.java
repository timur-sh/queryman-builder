/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.token.Operator;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Timur Shaidullin
 */
public class Operators {
    public static final Operator AND     = new Operator("AND").setPosition(0);
    public static final Operator NOT     = new Operator("NOT").setPosition(0);
    public static final Operator AND_NOT = new Operator("AND NOT").setPosition(0);
    public static final Operator OR      = new Operator("OR").setPosition(0);
    public static final Operator OR_NOT  = new Operator("OR NOT").setPosition(0);

    public static final Operator BETWEEN     = new Operator("BETWEEN");
    public static final Operator NOT_BETWEEN = new Operator("NOT BETWEEN");

    public static final Operator IS_DISTINCT_FROM     = new Operator("IS DISTINCT FROM");
    public static final Operator IS_NOT_DISTINCT_FROM = new Operator("IS NOT DISTINCT FROM");

    public static final Operator BETWEEN_SYMMETRIC     = new Operator("BETWEEN SYMMETRIC");
    public static final Operator NOT_BETWEEN_SYMMETRIC = new Operator("NOT BETWEEN SYMMETRIC");

    public static final Operator IS     = new Operator("IS");
    public static final Operator IS_NOT = new Operator("IS NOT");

    public static final Operator ISNULL     = new Operator("ISNULL");
    public static final Operator NOTNULL = new Operator("NOTNULL");

    public static final Operator LT    = new Operator("<");
    public static final Operator LTE   = new Operator("<=");
    public static final Operator GT    = new Operator(">");
    public static final Operator GTE   = new Operator(">=");
    public static final Operator EQUAL = new Operator("=");
    public static final Operator NE    = new Operator("!=");
    public static final Operator NE2   = new Operator("<>");

    public static final Operator map(String operator) {
        Map<String, Operator> operators = Map.ofEntries(
           entry(AND.getName(), AND),
           entry(NOT.getName(), NOT),
           entry(AND_NOT.getName(), AND_NOT),
           entry(OR.getName(), OR),
           entry(OR_NOT.getName(), OR_NOT),

           entry(IS.getName(), IS),
           entry(IS_NOT.getName(), IS_NOT),
           entry(ISNULL.getName(), ISNULL),
           entry(NOTNULL.getName(), NOTNULL),

           entry(BETWEEN.getName(), BETWEEN),
           entry(NOT_BETWEEN.getName(), NOT_BETWEEN),
           entry(IS_DISTINCT_FROM.getName(), IS_DISTINCT_FROM),
           entry(IS_NOT_DISTINCT_FROM.getName(), IS_NOT_DISTINCT_FROM),
           entry(BETWEEN_SYMMETRIC.getName(), BETWEEN_SYMMETRIC),
           entry(NOT_BETWEEN_SYMMETRIC.getName(), NOT_BETWEEN_SYMMETRIC),

           entry(LT.getName(), LT),
           entry(LTE.getName(), LTE),
           entry(GT.getName(), GT),
           entry(EQUAL.getName(), EQUAL),
           entry(NE.getName(), NE),
           entry(NE2.getName(), NE2)
        );

        return operators.getOrDefault(operator.toUpperCase(), new Operator(operator));
    }
}
