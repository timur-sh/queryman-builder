/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.util.List;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQLExamples {
    public void common() {
        //tag::common[]
        // books.id AS b_id
        PostgreSQL.asName("books.id").as("b_id");
        //end::common[]

    }

    public void constants() {
        //tag::constant[]
        // 1
        PostgreSQL.asConstant("1");
        // 20
        PostgreSQL.asNumber(20);
        // 'a string'
        PostgreSQL.asStringArray("a string");
        // $$a dollar string$$
        PostgreSQL.asDollarString("a dollar string");
        // $tag$a dollar string$tag$
        PostgreSQL.asDollarString("a dollar string", "tag");
        //end::constant[]
    }

    public void columnReference() {
        //tag::column-reference[]
        // books
        PostgreSQL.asName("books");
        // books.id
        PostgreSQL.asName("books.id");
        // "books"
        PostgreSQL.asQuotedName("books");
        // "books"."id"
        PostgreSQL.asQuotedName("books.id");
        //end::column-reference[]
    }

    public void array() {
        //tag::array[]
        // ARRAY[1, 2, 3]
        PostgreSQL.asArray(1, 2, 3);
        // ARRAY[1, 2, 3]
        PostgreSQL.asArray(List.of(1, 2, 3));
        // ARRAY['1', '2', '3']
        PostgreSQL.asStringArray(1, 2, 3);
        // ARRAY['1', '2', '3']
        PostgreSQL.asStringArray(List.of(1, 2, 3));
        //end::array[]

        //tag::array-expression[]
        // ARRAY[1, 2]
        PostgreSQL.asArray(PostgreSQL.asNumber(1), PostgreSQL.asNumber(2));
        //end::array-expression[]
    }

    public void list() {
        //tag::list[]
        // (1, 2, 3)
        PostgreSQL.asList(1, 2, 3);
        // (1, 2, 3)
        PostgreSQL.asList(List.of(1, 2, 3));
        // ('1', '2', '3')
        PostgreSQL.asStringList(1, 2, 3);
        // ('1', '2', '3')
        PostgreSQL.asStringList(List.of(1, 2, 3));
        //end::list[]

        //tag::list-expression[]
        // (1, 2)
        PostgreSQL.asList(PostgreSQL.asNumber(1), PostgreSQL.asNumber(2));
        //end::list-expression[]
    }

    public void func() {
        //tag::func[]
        // concat('price', 1, 2)
        PostgreSQL.asFunc("concat", "'price'", "1", ".", "2");
        //end::func[]
    }

    public void query() {
        //tag::query[]
        // (SELECT id, name FROM books)
        PostgreSQL.asSubQuery(PostgreSQL.select("id", "name").from("books"));
        //end::query[]
    }
}
