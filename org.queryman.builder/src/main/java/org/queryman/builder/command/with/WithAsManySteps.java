/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

/**
 * @author Timur Shaidullin
 */
public interface WithAsManySteps extends SelectFirstStep {
    WithAsStep with(String name, String... columns);
}
