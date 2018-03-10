/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Timur Shaidullin
 */
public class InsertInto {
    void simpleTest() {
        //tag::simple-insert[]
        // INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (id)
        // DO UPDATE SET id = 1, name = 'test' WHERE id = 1 AND id != 3 RETURNING id
        PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .defaultValues()
           .onConflict("id")
           .doUpdate()
           .set("id", 1)
           .set("name", "test")
           .where("id", "=", 1)
           .and("id", "!=", 3)
           .returning("id")
           .sql();
        //end::simple-insert[]
    }

    void preparedTest(Connection connection) throws SQLException {
        //tag::prepared-insert[]
        // INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (id)
        // DO UPDATE SET id = ?, name = ? WHERE id = ? AND id != ? RETURNING id
        PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .defaultValues()
           .onConflict("id")
           .doUpdate()
           .set("id", 1)
           .set("name", "test")
           .where("id", "=", 1)
           .and("id", "!=", 3)
           .returning("id")
           .buildPreparedStatement(connection);
        //end::prepared-insert[]
    }
}
