/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;


/**
 * @author Timur Shaidullin
 */
public class Select {
    void select() {
        //tag::simple-select[]
        PostgreSQL.select("id", "name")
           .from("books")
           .where("id", "=", "2")
           .orderBy("year");
        //end::simple-select[]
    }
}
