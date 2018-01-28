/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import static org.queryman.builder.ast.NodesMetadata.AND;
import static org.queryman.builder.ast.NodesMetadata.OR;

/**
 * @author Timur Shaidullin
 */
final class CommandUtils {
    static Where stubWhere() {
        return new Where();
    }

    static Where where(String left, String operator, String right) {
        return new Where(left, operator, right);
    }

    static Where orWhere(String left, String operator, String right) {
        return new Where(OR, left, operator, right);
    }

    static Where andWhere(String left, String operator, String right) {
        return new Where(AND, left, operator, right);
    }
}
