/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.impl.WhereGroupImpl;
import org.queryman.builder.command.where.WhereGroup;

/**
 * @author Timur Shaidullin
 */
public class Statements {
    public static WhereGroup where(String leftValue, String operator, String rightValue) {
        return new WhereGroupImpl(leftValue, operator, rightValue);
    }
}
