package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectJoinStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.IN;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.NE2;
import static org.queryman.builder.Operators.NOT_IN;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asString;
import static org.queryman.builder.PostgreSQL.between;
import static org.queryman.builder.PostgreSQL.condition;

class SelectImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void select() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        assertEquals("SELECT id, name", select.sql());

        SelectFromStep select2 = new SelectImpl(ast, asQuotedName("id2"), asQuotedName("name"), asConstant("min(price) as min"));
        assertEquals("SELECT \"id2\", \"name\", min(price) as min", select2.sql());
    }

    @Test
    void selectFrom() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");

        assertEquals("SELECT id, name FROM books", select.from("books").sql());

        assertEquals("SELECT id, name FROM books as b(1,2)", select.from("books as b(1,2)").sql());

        assertEquals("SELECT id, name FROM \"books\"", select.from(asQuotedName("books")).sql());

        assertEquals("SELECT id, name FROM public.books", select.from(asName("public.books")).sql());

        assertEquals("SELECT id, name FROM table1, table2", select.from("table1", "table2").sql());
    }

    @Test
    void selectFromJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");

        assertEquals("SELECT id, name FROM books JOIN author ON (true)", select.join("author").on(true).sql());

        select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books JOIN author USING (id)", select.join("author").using("id").sql());

        select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books JOIN author USING (id, name)", select.join("author").using("id", "name").sql());

        select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id", select.join("author").on("id", "=", "author_id").sql());

        select = new SelectImpl(ast, "id", "name").from("books").join("author").on("id", "=", "author_id").andExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id AND EXISTS (SELECT 1, 2)", select.sql());

        select = new SelectImpl(ast, "id", "name").from("books").join("author").on("id", "=", "author_id").and(asName("id"), IN, new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id AND id IN (SELECT 1, 2)", select.sql());

        select = new SelectImpl(ast, "id", "name").from("books").join("author").on("id", "=", "author_id").andNot(asName("id"), IN, new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id AND NOT id IN (SELECT 1, 2)", select.sql());

        select = new SelectImpl(ast, "id", "name").from("books").join("author").on("id", "=", "author_id").or(asName("id"), IN, new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id OR id IN (SELECT 1, 2)", select.sql());

        select = new SelectImpl(ast, "id", "name").from("books").join("author").on("id", "=", "author_id").orNot(asName("id"), IN, new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT id, name FROM books JOIN author ON id = author_id OR NOT id IN (SELECT 1, 2)", select.sql());

        select = new SelectImpl(ast, "*").from("books");
        assertEquals("SELECT * FROM books JOIN author ON EXISTS (SELECT 1, 2)", select.join("author").onExists(new SelectImpl(ast, "1", "2")).sql());

        select = new SelectImpl(ast, "*").from("books").join("author").on(true).innerJoin("sales").onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books JOIN author ON (true) INNER JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
    }

    @Test
    void selectFromInnerJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");

        assertEquals("SELECT id, name FROM books INNER JOIN author ON (true)", select.innerJoin("author").on(true).sql());

        select = new SelectImpl(ast, "*").from("books").innerJoin("author").on(true).join("sales").onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books INNER JOIN author ON (true) JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
    }

    @Test
    void selectFromLeftJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books LEFT JOIN author ON (true)", select.leftJoin("author").on(true).sql());

        select = new SelectImpl(ast, "*").from("books").leftJoin("author").on(true).innerJoin(asName("sales")).onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books LEFT JOIN author ON (true) INNER JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
    }

    @Test
    void selectFromRightJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books RIGHT JOIN author ON (true)", select.rightJoin("author").on(true).sql());

        select = new SelectImpl(ast, "*").from("books").rightJoin("author").on(true).leftJoin(asName("sales")).onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books RIGHT JOIN author ON (true) LEFT JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
    }

    @Test
    void selectFromFullJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books FULL JOIN author ON (true)", select.fullJoin("author").on(true).sql());

        select = new SelectImpl(ast, "*").from("books").fullJoin("author").on(true).rightJoin(asName("sales")).onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books FULL JOIN author ON (true) RIGHT JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
    }

    @Test
    void selectFromCrossJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books CROSS JOIN author", select.crossJoin("author").sql());

        SelectJoinStep select1 = new SelectImpl(ast, "*").from("books");
        select1.crossJoin("author").fullJoin(asName("sales")).onExists(new SelectImpl(ast, "1", "2"));
        assertEquals("SELECT * FROM books CROSS JOIN author FULL JOIN sales ON EXISTS (SELECT 1, 2)", select1.sql());
    }

    @Test
    void selectFromNaturalJoin() {
        SelectJoinStep select = new SelectImpl(ast, "id", "name").from("books");
        assertEquals("SELECT id, name FROM books NATURAL JOIN author", select.naturalJoin("author").sql());

        SelectJoinStep select1 = new SelectImpl(ast, "*").from("books");
        select1.crossJoin("author").naturalJoin(asName("sales")).crossJoin(asName("calls"));
        assertEquals("SELECT * FROM books CROSS JOIN author NATURAL JOIN sales CROSS JOIN calls", select1.sql());
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
           .where(asQuotedName("id"), EQUAL, asNumber(1))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE \"id\" = 1", sql);

        sql = select.from("books")
           .where(asName("id"), IN, new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id IN (SELECT 1, 2)", sql);

        sql = select.from("books")
           .whereExists(new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE EXISTS (SELECT 1, 2)", sql);

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
           .where(asName("id1"), EQUAL, asString("1"))
           .or(asQuotedName("id2"), EQUAL, asNumber(2))
           .orNot(asName("table.id3"), EQUAL, asNumber(3))
           .and(asQuotedName("table.id4"), EQUAL, asNumber(4))
           .andNot(asQuotedName("id5"), EQUAL, asNumber(5))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id1 = '1' OR \"id2\" = 2 OR NOT table.id3 = 3 AND \"table\".\"id4\" = 4 AND NOT \"id5\" = 5", sql);
    }

    @Test
    void selectFromWhereBetween() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .whereBetween("id", "1", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id BETWEEN 1 AND 2", sql);

        sql = select.from("books")
           .whereBetween(between(asQuotedName("id"), asNumber(3), asNumber(4))
              .and("phone", "is", "null")
           )
           .or("id", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM books WHERE (\"id\" BETWEEN 3 AND 4 AND phone is null) OR id = 2", sql);

        sql = select.from("books")
           .whereBetween(asQuotedName("id"), asNumber(3), asNumber(4))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE \"id\" BETWEEN 3 AND 4", sql);

        sql = select.from("books")
           .whereBetween(asQuotedName("id"), asNumber(3), asNumber(4))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE \"id\" BETWEEN 3 AND 4", sql);
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
           .and(asQuotedName("id2"), EQUAL, asNumber(2))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" = 2", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" = 4", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and(asQuotedName("id2"), NOT_IN, new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andExists(new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND EXISTS (SELECT 1, 2)", sql);
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
           .andNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" = 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" = 4", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), NOT_IN, new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .andNotExists(new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 AND NOT EXISTS (SELECT 1, 2)", sql);
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
           .where("id", "<>", "1")
           .or(asQuotedName("id2"), NE2, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id <> 1 OR \"id2\" <> 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .or(condition(asQuotedName("id2"), LT, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR \"id2\" < 4", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .or(asQuotedName("id2"), NOT_IN, new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orExists(new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR EXISTS (SELECT 1, 2)", sql);
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
           .orNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" = 3", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" = 4", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), NOT_IN, new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .orNotExists(new SelectImpl(ast, "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM books WHERE id = 1 OR NOT EXISTS (SELECT 1, 2)", sql);
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

    //---
    // UNION
    //---

    @Test
    void selectUnion() {
        String sql = new SelectImpl(ast, "1", "2").union(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 UNION SELECT 2, 2", sql);
    }

    @Test
    void selectFromUnion() {
        SelectFromStep select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .union(new SelectImpl(ast, "2", "2"));
        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM books UNION SELECT 2, 2", sql);

        select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .crossJoin("author")
           .union(new SelectImpl(ast, "2", "2"));
        sql = select.sql();
        assertEquals("SELECT 1, 2 FROM books CROSS JOIN author UNION SELECT 2, 2", sql);

        select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .join("author").on(true)
           .union(new SelectImpl(ast, "2", "2"));
        sql = select.sql();
        assertEquals("SELECT 1, 2 FROM books JOIN author ON (true) UNION SELECT 2, 2", sql);
    }

    @Test
    void selectFromWhereUnion() {
        SelectFromStep select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .where(between(asString("id"), asNumber(1), asNumber(2)))
           .union(new SelectImpl(ast, "2", "2"));

        assertEquals("SELECT 1, 2 FROM books WHERE 'id' BETWEEN 1 AND 2 UNION SELECT 2, 2", select.sql());

        select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .where(between(asString("id"), asNumber(1), asNumber(2)))
           .and("name", "IS NOT", null)
           .union(new SelectImpl(ast, "2", "2"));

        assertEquals("SELECT 1, 2 FROM books WHERE 'id' BETWEEN 1 AND 2 AND name IS NOT NULL UNION SELECT 2, 2", select.sql());
    }

    @Test
    void selectFromWhereGroupByUnion() {
        SelectFromStep select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .where(between(asString("id"), asNumber(1), asNumber(2)))
           .groupBy(asQuotedName("books.id"))
           .union(new SelectImpl(ast, "2", "2"));

        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM books WHERE 'id' BETWEEN 1 AND 2 GROUP BY \"books\".\"id\" UNION SELECT 2, 2", sql);
    }

    @Test
    void selectFromWhereGroupByUnionOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "1", "2");
        select
           .from("books")
           .where(between(asString("id"), asNumber(1), asNumber(2)))
           .groupBy(asQuotedName("books.id"))
           .union(new SelectImpl(ast, "2", "2"))
           .orderBy("id", "DESC");

        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM books WHERE 'id' BETWEEN 1 AND 2 GROUP BY \"books\".\"id\" UNION SELECT 2, 2 ORDER BY id DESC", sql);
    }

    @Test
    void selectUnionAll() {
        String sql = new SelectImpl(ast, "1", "2").unionAll(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 UNION ALL SELECT 2, 2", sql);
    }

    @Test
    void selectUnionDistinct() {
        String sql = new SelectImpl(ast, "1", "2").unionDistinct(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 UNION DISTINCT SELECT 2, 2", sql);
    }

    //---
    // INTERSECT
    //---

    @Test
    void selectIntersect() {
        String sql = new SelectImpl(ast, "1", "2").intersect(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 INTERSECT SELECT 2, 2", sql);
    }

    @Test
    void selectIntersectAll() {
        String sql = new SelectImpl(ast, "1", "2").intersectAll(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 INTERSECT ALL SELECT 2, 2", sql);
    }

    @Test
    void selectIntersectDistinct() {
        String sql = new SelectImpl(ast, "1", "2").intersectDistinct(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 INTERSECT DISTINCT SELECT 2, 2", sql);
    }

    //---
    // EXCEPT
    //---

    @Test
    void selectExcept() {
        String sql = new SelectImpl(ast, "1", "2").except(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 EXCEPT SELECT 2, 2", sql);
    }

    @Test
    void selectExceptAll() {
        String sql = new SelectImpl(ast, "1", "2").exceptAll(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 EXCEPT ALL SELECT 2, 2", sql);
    }

    @Test
    void selectExceptDistinct() {
        String sql = new SelectImpl(ast, "1", "2").exceptDistinct(new SelectImpl(ast, "2", "2")).sql();
        assertEquals("SELECT 1, 2 EXCEPT DISTINCT SELECT 2, 2", sql);
    }


}