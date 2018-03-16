/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.sql.Connection;
import java.sql.SQLException;

import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.select;

/**
 * @author Timur Shaidullin
 */
public class Prepared {
    public void prepared1(Connection connection) throws SQLException {
        //tag::prepared1[]
        // SELECT * FROM book id = ?
        // Parameters
        // {
        //    1 -> 10
        // }
        Queryman.select("*")
           .from("book")
           .where("id", "=", 10)
           .buildPreparedStatement(connection);
        //end::prepared1[]
    }

    public void prepared2(Connection connection) throws SQLException {
        //tag::prepared2[]
        // SELECT * FROM (SELECT id, name, year FROM book WHERE year > ?) AS b WHERE b.id = ? FOR SHARE
        // Parameters
        // {
        //    1 -> 2000
        //    2 -> 1
        // }
        Queryman.select("*")
           .from(asSubQuery(select("id", "name", "year")
                 .from("book")
                 .where("year", ">", 2000)
              ).as("b")
           )
           .where("b.id", "=", 1)
           .forShare()
           .buildPreparedStatement(connection);
        //end::prepared2[]
    }
}
