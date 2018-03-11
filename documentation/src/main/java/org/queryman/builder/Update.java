/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import java.sql.Connection;
import java.sql.SQLException;

import static org.queryman.builder.Queryman.asConstant;

/**
 * @author Timur Shaidullin
 */
public class Update {
    void simpleUpdateTest() {
        //tag::simple-update[]
        // UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING *
        Queryman.update("book")
           .as("b")
           .set("author", asConstant("Andrew"))
           .where("b.id", "=", 1)
           .returning("*")
           .sql();
        //end::simple-update[]
    }

    void preparedUpdateTest(Connection connection) throws SQLException {
        //tag::prepared-update[]
        // UPDATE book AS b SET author = ? WHERE b.id = ? RETURNING *
        Queryman.update("book")
           .as("b")
           .set("author", asConstant("Andrew"))
           .where("b.id", "=", 1)
           .returning("*")
           .buildPreparedStatement(connection);
        //end::prepared-update[]
    }
}
