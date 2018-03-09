/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import org.queryman.builder.command.select.SelectFromStep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.queryman.builder.Operators.*;
import static org.queryman.builder.PostgreSQL.*;
import static org.queryman.builder.PostgreSQL.select;

/**
 * @author Timur Shaidullin
 */
public class Select {
    void selectTest() throws SQLException {
        //tag::simple-select[]
        PostgreSQL.select("id", "name")
           .from("book", "author")
           .where("id", "=", "2")
           .orderBy("year")
           .sql();
        //end::simple-select[]

        //tag::simple-select2[]
        PostgreSQL.select("id", "name")
           .from("book")
           .innerJoin(asName("author").as("a"))
           .on("a.id", "=", "author_id")
           .where("author_id", "=", "2")
           .orderBy("author_id.year")
           .limit(25)
           .offset(10)
           .sql();
        //end::simple-select2[]


        //tag::select-prepare[]
        Connection conn = DriverManager.getConnection(
           "jdbc:postgresql://localhost:5432/name", "user", "pass");

        // SELECT * FROM book WHERE author_id = ?
        PostgreSQL.select("*")
           .from("book")
           .where(asName("author_id"), EQUAL, asConstant(10))
           .buildPreparedStatement(conn);
        //end::select-prepare[]
    }

    void selectGroupBy() {
        //tag::select-group[]
        // SELECT id, name FROM book GROUP BY id, name
        select("id", "name")
           .from("book")
           .groupBy("id", "name")
           .sql();
        //end::select-group[]

        //tag::select-group2[]
        // SELECT id, name FROM book GROUP BY ROLLUP(id, name), CUBE(id, name)
        select("id", "name")
           .from("book")
           .groupBy(asOperator("ROLLUP", "id", "name"), asOperator("CUBE", "id", "name"))
           .sql();
        //end::select-group2[]
    }
}
