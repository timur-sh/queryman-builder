/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

/**
 * @author Timur Shaidullin
 */
public interface SelectFromManySteps extends
   SelectWhereFirstStep,
   SelectGroupByStep,
   SelectLimitStep,
   SelectOrderByStep {
}
