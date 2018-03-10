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
public class Sequence {
    void simpleSequence() {
        //tag::simple-sequence[]
        // CREATE SEQUENCE book_seq AS smallint
        // INCREMENT BY 1 MINVALUE 0 MAXVALUE NO MAXVALUE
        // START WITH 0 CACHE 5 CYCLE OWNED BY NONE
        PostgreSQL.createSequence("book_seq")
           .as("smallint")
           .incrementBy(1)
           .minvalue(0)
           .noMaxvalue()
           .startWith(0)
           .cache(5)
           .cycle()
           .ownedByNone()
           .sql();
        //end::simple-sequence[]
    }
}
