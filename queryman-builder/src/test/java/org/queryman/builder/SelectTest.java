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
import static org.queryman.builder.Queryman.select;

/**
 * @author Timur Shaidullin
 */
public class SelectTest extends BaseTest  {
    @Test
    void complexSelectTest() throws SQLException {
        Query query = select(1, 2, "integer")
           .from("types")
           .where("bigint", "=", 3)
           .and("smallint", "IS NOT", null)
//           .and(conditionAny("bigint", ))
           ;

        inBothStatement(query, rs -> {
            assertEquals(1, rs.getInt(1));
            assertEquals(2, rs.getInt(2));
            assertEquals(2, rs.getInt(3));
        });
    }
}
