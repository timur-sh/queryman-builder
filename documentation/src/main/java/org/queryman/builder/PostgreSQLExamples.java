/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.util.List;

import static org.queryman.builder.PostgreSQL.asDollarString;
import static org.queryman.builder.PostgreSQL.asFunc;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asString;
import static org.queryman.builder.PostgreSQL.asSubQuery;
import static org.queryman.builder.PostgreSQL.select;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQLExamples {
    public void common() {
        //tag::alias[]
        // book.id AS b_id
        PostgreSQL.asName("book.id").as("b_id");
        //end::alias[]

        //tag::cast[]
        // ARRAY[1, 2, 3]::integer[] AS arr
        PostgreSQL.asArray(1, 2, 3).cast("integer[]").as("arr");
        //end::cast[]

        //tag::leverage[]
        // ARRAY[$$one$$, 'two', '3']
        PostgreSQL.asStringArray(asDollarString("one"), asString("two"), asNumber(3));
        //end::leverage[]

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
        // book
        PostgreSQL.asName("book");
        // book.id
        PostgreSQL.asName("book.id");
        // "book"
        PostgreSQL.asQuotedName("book");
        // "book"."id"
        PostgreSQL.asQuotedName("book.id");
        //end::column-reference[]
    }

    public void array() {
        //tag::array[]
        // ARRAY[1, 2, 3]
        PostgreSQL.asArray("1");
        // ARRAY[1, 2, 3]
        PostgreSQL.asArray(List.of(1, 2, 3));
        // ARRAY['a', 'b']
        PostgreSQL.asStringArray("a", "b");
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
        /*
        //tag::func[]
        // concat('price', 1, 2)
        PostgreSQL.asFunc("concat", PostgreSQL.asList("'price'", 2, ".", 1));

        // concat('price', 1, 2)
        PostgreSQL.asFunc("concat", "'price'", 2, ".", 1);

        // (VALUES(1, 2), (3, 4)) AS point(x, y)
        PostgreSQL.values(asList(1, 2), asList(3, 4)).as("point", "x", "y");
        //end::func[]
        */
    }

    public void query() {
        //tag::query[]
        // (SELECT id, name FROM book)
        PostgreSQL.asSubQuery(PostgreSQL.select("id", "name").from("book"));
        //end::query[]

        //tag::select-query[]
        // SELECT (SELECT max(price) FROM book) AS max
        select(
           asSubQuery(select("max(price)").from("book")).as("max")
        );
        //end::select-query[]

        //tag::exists-query[]
        // SELECT EXISTS(SELECT * FROM book) AS exists
        select(
           asFunc("EXISTS", asSubQuery(select("*").from("book"))).as("exists")
        );
        //end::exists-query[]
    }
}
