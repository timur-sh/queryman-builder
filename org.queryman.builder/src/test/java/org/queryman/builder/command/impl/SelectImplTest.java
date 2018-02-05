package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.JdbcException;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.testing.Jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.NE2;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asQualifiedName;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asQuotedQualifiedName;
import static org.queryman.builder.PostgreSQL.asString;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.testing.Jdbc.inJdbcByDriverManager;

class SelectImplTest extends BaseTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void select() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        assertEquals("SELECT id, name", select.sql());
        assertThrows(JdbcException.class, () -> inJdbcByDriverManager(select), "ERROR: column \"id\" does not exist");

        SelectFromStep select2 = new SelectImpl(ast, asQuotedName("id2"), asQuotedName("name"), asConstant("min(price) as min"));
        assertEquals("SELECT \"id2\", \"name\", min(price) as min", select2.sql());
        assertThrows(JdbcException.class, () -> inJdbcByDriverManager(select2), "ERROR: column \"id2\" does not exist");

        SelectFromStep select3 = new SelectImpl(ast, asString("id"), asNumber(1));
        inJdbcByDriverManager(select3);
    }

    @Test
    void selectFrom() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");

        assertEquals("SELECT id, name FROM books", select.from("books").sql());

        assertEquals("SELECT id, name FROM \"books\"", select.from(asQuotedName("books")).sql());

        assertEquals("SELECT id, name FROM public.books", select.from(asQualifiedName("public.books")).sql());

        assertEquals("SELECT id, name FROM table1, table2", select.from("table1", "table2").sql());
    }

    //---
    // WHERE
    //---

    @Test
    void selectFromWhere() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2", sql);

        sql = select.from("books")
           .where(asQuotedName("id"), "=", asNumber(1))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE \"id\" = 1", sql);

        sql = select.from("books")
           .where(asQuotedName("id"), EQUAL, asNumber(1))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE \"id\" = 1", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT id2 = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot("id3", "=", "3")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT id3 = 3 AND NOT id2 = 2", sql);

        sql = select.from("books")
           .where(asName("id1"), "=", asString("1"))
           .or(asQuotedName("id2"), "=", asNumber(2))
           .orNot(asQualifiedName("table.id3"), "=", asNumber(3))
           .and(asQuotedQualifiedName("table.id4"), "=", asNumber(4))
           .andNot(asQuotedName("id5"), "=", asNumber(5))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id1 = '1' OR \"id2\" = 2 OR NOT table.id3 = 3 AND \"table\".\"id4\" = 4 AND NOT \"id5\" = 5", sql);
    }

    @Test
    void selectFromWhereAnd() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and(asQuotedName("id2"), "=", asNumber(2))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" = 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" = 4", sql);
    }

    @Test
    void selectFromWhereAndNot() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .andNot("id2", "!=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT id2 != 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), "<=", asNumber(2))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" <= 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" = 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" = 4", sql);
    }

    @Test
    void selectFromWhereOr() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .or("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR id2 = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .or(asQuotedName("id2"), "=", asNumber(2))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR \"id2\" = 2", sql);

        sql = select.from("books")
           .where("id", "<>", "1")
           .or(asQuotedName("id2"), NE2, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id <> 1 OR \"id2\" <> 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .or(condition(asQuotedName("id2"), LT, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR \"id2\" < 4", sql);
    }

    @Test
    void selectFromWhereOrNot() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .orNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT id2 = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), "=", asNumber(2))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" = 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" = 4", sql);
    }

    @Test
    void selectFromWhereGroup() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("user")
           .where(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", asString("timur@shaidullin.net").getName())
              .and(condition("id", "!=", "3")
                 .and("name", "is not", asString("max").getName())
              )
           )
           .or("id", "=", "1")
           .sql();

        assertEquals("SELECT id, name FROM user WHERE (name = timur AND phone is null OR email = 'timur@shaidullin.net' AND (id != 3 AND name is not 'max')) OR id = 1", sql);

        sql = select.from("user")
           .where(condition("name", "=", "timur")
              .and("phone", "is", "null")
           )
           .or("id", "=", "1")
           .and(condition("id", "!=", "3")
              .and("name", "is not", "max")
           )
           .sql();

        assertEquals("SELECT id, name FROM user WHERE (name = timur AND phone is null) OR id = 1 AND (id != 3 AND name is not max)", sql);
    }

    @Test
    void selectFromWhereAndWhereGroup() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("user")
           .where("id", "=", "1")
           .and(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .and("id2", "=", "2")
           .sql();

        assertEquals("SELECT id, name FROM user WHERE id = 1 AND (name = timur AND phone is null OR email = 'timur@shaidullin.net') AND id2 = 2", sql);
    }

    //---
    // GROUP BY
    //---

    @Test
    void selectFromGroupBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .groupBy("id", "name")
           .sql();
        assertEquals("SELECT id, name FROM books GROUP BY id, name", sql);
    }

    @Test
    void selectFromWhereGroupBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .groupBy("id")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 GROUP BY id", sql);
    }

    //---
    // ORDER BY
    //---

    @Test
    void selectFromOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .orderBy("id")
           .sql();

        assertEquals("SELECT id, name FROM books ORDER BY id", sql);

        sql = select.from("books")
           .orderBy("name", "desc")
           .sql();

        assertEquals("SELECT id, name FROM books ORDER BY name desc", sql);

        sql = select.from("books")
           .orderBy("name", "desc", "nulls last")
           .sql();

        assertEquals("SELECT id, name FROM books ORDER BY name desc nulls last", sql);
    }

    @Test
    void selectFromWhereGroupByOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .groupBy("id")
           .orderBy("name")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 GROUP BY id ORDER BY name", sql);
    }

    @Test
    void selectFromWhereOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .orderBy("name")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 ORDER BY name", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .orderBy("id")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2 ORDER BY id", sql);
    }

    //---
    // LIMIT
    //---

    @Test
    void selectFromLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .limit(1)
           .sql();

        assertEquals("SELECT id, name FROM books LIMIT 1", sql);
    }

    @Test
    void selectFromOrderByLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .orderBy("id")
           .limit(2)
           .sql();

        assertEquals("SELECT id, name FROM books ORDER BY id LIMIT 2", sql);
    }

    @Test
    void selectFromGroupByLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .groupBy("id")
           .limit(2)
           .sql();

        assertEquals("SELECT id, name FROM books GROUP BY id LIMIT 2", sql);
    }

    @Test
    void selectFromWhereLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .limit(3)
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2 LIMIT 3", sql);
    }

    //---
    // OFFSET
    //---

    @Test
    void selectFromOffset() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .limit(1)
           .sql();

        assertEquals("SELECT id, name FROM books LIMIT 1", sql);
    }

    @Test
    void selectFromGroupByOffset() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .groupBy("id")
           .limit(1)
           .sql();

        assertEquals("SELECT id, name FROM books GROUP BY id LIMIT 1", sql);
    }

    @Test
    void selectFromWhereOffset() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .offset(3)
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2 OFFSET 3", sql);
    }

    @Test
    void selectFromWhereLimitOffset() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .limit(3)
           .offset(3)
           .sql();

        assertEquals("SELECT id, name FROM books WHERE id = 1 AND id2 = 2 LIMIT 3 OFFSET 3", sql);
    }
}