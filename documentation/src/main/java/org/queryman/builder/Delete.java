/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.operator;

/**
 * @author Timur Shaidullin
 */
public class Delete {
    void selectTest() {
        //tag::simple-delete[]
        // DELETE FROM book AS b USING author WHERE b.id = 1 AND author.id = b.author_id RETURNING *
        PostgreSQL.deleteFrom("book")
           .as("b")
           .using("author", "order")
           .where("b.id", "=", "1")
           .and(asName("author.id"), operator("="), asName("b.author_id"))
           .returning("*")
           .sql();
        //end::simple-delete[]
    }
}
