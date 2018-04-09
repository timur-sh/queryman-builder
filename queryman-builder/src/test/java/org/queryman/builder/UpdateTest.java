/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.update;

/**
 * @author Timur Shaidullin
 */
public class UpdateTest extends BaseTest  {

    @Test
    void complexUpdate() throws SQLException {

        Query query = update("types")
           .set(
              asList("smallint","integer", "bigint", "decimal", "numeric", "real", "double_precision", "boolean"),
              asList(11, 12, 13, 14, 15, 16, 17, false)
           )
           .returning("smallint","integer", "bigint", "decimal", "numeric", "real", "double_precision", "boolean");

        inBothStatement(query, rs -> {
            assertEquals(rs.getInt(1), 11);
            assertEquals(rs.getInt(2), 12);
            assertEquals(rs.getInt(3), 13);
            assertEquals(rs.getInt(4), 14);
            assertEquals(rs.getInt(5), 15);
            assertEquals(rs.getInt(6), 16);
            assertEquals(rs.getInt(7), 17);
            assertEquals(rs.getBoolean(8), false);
        });
    }
}
