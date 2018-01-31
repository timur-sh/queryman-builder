package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.SelectFromStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.condition;

class SelectImplTest extends BaseTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void select() {
        SelectFromStep select = new SelectImpl(ast, "id", "name", "min(price) as min");
        assertEquals("SELECT id, name, min(price) as min", select.sql());
    }

    @Test
    void selectFrom() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");

        assertEquals("SELECT id, name FROM books", select.from("books").sql());

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
    }

    @Test
    void selectFromWhereGroup() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("user")
           .where(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .or("id", "=", "1")
           .sql();

        assertEquals("SELECT id, name FROM user WHERE (name = timur AND phone is null OR email = 'timur@shaidullin.net') OR id = 1", sql);
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
           .groupBy("id")
           .sql();
        assertEquals("SELECT id, name FROM books GROUP BY id", sql);
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