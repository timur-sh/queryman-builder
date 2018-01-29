/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.impl.ConditionsImpl;
import org.queryman.builder.command.where.Conditions;

/**
 * @author Timur Shaidullin
 */
public class Statements {
    public static Conditions condition(String leftValue, String operator, String rightValue) {
        return new ConditionsImpl(leftValue, operator, rightValue);
    }
}
