/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


import static org.queryman.builder.PostgreSQL.*;

/**
 * @author Timur Shaidullin
 */
public class Select {
    void select() {
        //tag::simple-select[]
        PostgreSQL.select("id", "name")
           .from("books", "author")
           .where("id", "=", "2")
           .orderBy("year")
           .sql();
        //end::simple-select[]

        //tag::simple-select2[]
        PostgreSQL.select("id", "name")
           .from(fromOnly("books").as("b"))
           .innerJoin(asName("authors").as("a"))
           .on("a.id", "=", "b.author_id")
           .where("b.id", "=", "2")
           .orderBy("b.year")
           .limit(25)
           .offset(10)
           .sql();
        //end::simple-select2[]
    }
}
