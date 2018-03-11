/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import static org.queryman.builder.Operators.IN;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.conditionBetween;
import static org.queryman.builder.Queryman.operator;
import static org.queryman.builder.Queryman.select;

/**
 * @author Timur Shaidullin
 */
public class Conditions {
    void selectWhere() {
        //tag::select-where[]
        // SELECT * FROM book WHERE id = 2 AND year > 2000
        Queryman.select("*")
           .from("book")
           .where("id", "=", "2")
           .and("year", ">", "2000")
           .sql();
        //end::select-where[]

        //tag::select-where-query[]
        // SELECT * FROM book WHERE year > 2010 AND author_id IN (SELECT id FROM author)
        Queryman.select("*")
           .from("book")
           .where("year", ">", "2010")
           .and(asName("author_id"), IN, select("id").from("authors"))
           .sql();
        //end::select-where-query[]

        //tag::select-where-complex[]
        // SELECT * FROM book WHERE year > 2010 AND (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
        Queryman.select("*")
           .from("book")
           .where("year", ">", "2010")
           .and(
              conditionBetween("id", "1", "10")
                 .and(asName("name"), operator("="), asConstant("Advanced SQL"))
           )
           .sql();
        //end::select-where-complex[]
    }
}
