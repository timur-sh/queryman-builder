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
    public static final Operator AND     = new Operator("AND");
    public static final Operator NOT     = new Operator("NOT");
    public static final Operator AND_NOT = new Operator("AND NOT");
    public static final Operator OR      = new Operator("OR");
    public static final Operator OR_NOT  = new Operator("OR NOT");

    public static final Operator LT  = new Operator("<");
    public static final Operator LTE  = new Operator("<=");
    public static final Operator GT  = new Operator(">");
    public static final Operator GTE  = new Operator(">=");
    public static final Operator EQUAL  = new Operator("=");
    public static final Operator NE  = new Operator("!=");
    public static final Operator NE2  = new Operator("<>");

    public static final Operator map(String operator) {
        Map<String, Operator> operators = Map.ofEntries(
           entry("AND", AND),
           entry("NOT", NOT),
           entry("AND NOT", AND_NOT),
           entry("OR", OR),
           entry("OR NOT", OR_NOT),
           entry("<", LT),
           entry("<=", LTE),
           entry(">", GT),
           entry(">=", GTE),
           entry("=", EQUAL),
           entry("!=", NE),
           entry("<>", NE2)
        );

        return operators.getOrDefault(operator.toUpperCase(), new Operator(operator));
    }
}
