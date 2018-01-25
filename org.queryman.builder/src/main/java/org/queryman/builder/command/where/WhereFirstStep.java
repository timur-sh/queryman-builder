/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.where;

/**
 * @author Timur Shaidullin
 */
public interface WhereFirstStep {
    WhereFinalStep where(String leftValue, String operator, String rightValue);

    WhereFinalStep andWhere(String leftValue, String operator, String rightValue);

    WhereFinalStep orWhere(String leftValue, String operator, String rightValue);
}
