/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Expression;

import java.sql.PreparedStatement;
import java.util.Map;

/**
 * @author Timur Shaidullin
 */
class PrepareStatementUtil {
    static PreparedStatement bind(PreparedStatement statement, Map<Integer, Expression> params) {

        // bind parameters

        return statement;
    }
}
