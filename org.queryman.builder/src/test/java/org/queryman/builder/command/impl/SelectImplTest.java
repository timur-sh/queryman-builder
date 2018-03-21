package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Query;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.select.SelectJoinStep;
import org.queryman.builder.token.Expression;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.IN;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.NE2;
import static org.queryman.builder.Operators.NOT_IN;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asFunc;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asQuotedName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.condition;
import static org.queryman.builder.Queryman.conditionBetween;
import static org.queryman.builder.Queryman.fromOnly;
import static org.queryman.builder.Queryman.max;
import static org.queryman.builder.Queryman.operator;
import static org.queryman.builder.Queryman.orderBy;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.selectAll;
import static org.queryman.builder.Queryman.selectDistinct;
import static org.queryman.builder.Queryman.selectDistinctOn;
import static org.queryman.builder.TestHelper.testBindParameters;
import static org.queryman.builder.ast.TreeFormatterUtil.buildPreparedSQL;

class SelectImplTest extends BaseTest {

    @Test
    void selectTest() throws NoSuchFieldException, IllegalAccessException {
        SelectFromStep select = select("id", 2, .4, "name");

        assertEquals("SELECT id, 2, 0.4, name", select.sql());
        assertEquals("SELECT id, 2, 0.4, name", buildPreparedSQL(select));

        SelectFromStep select2 = select(asQuotedName("id2"), asQuotedName("name"), asFunc("min", asName("price")).as("min"));
        assertEquals("SELECT \"id2\", \"name\", min(price) AS min", select2.sql());
        assertEquals("SELECT \"id2\", \"name\", min(price) AS min", buildPreparedSQL(select2));

        SelectFromStep select3 = select(asQuotedName("id2"), asSubQuery(select("max(sum)")).as("sum"));
        assertEquals("SELECT \"id2\", (SELECT max(sum)) AS sum", select3.sql());
        assertEquals("SELECT \"id2\", (SELECT max(sum)) AS sum", buildPreparedSQL(select3));

        assertEquals("SELECT MAX(total) FROM order", select(max("total")).from("order").sql());
    }

    @Test
    void selectAllTest() {
        SelectFromStep select = selectAll("id", "name");
        assertEquals("SELECT ALL id, name", select.sql());
        assertEquals("SELECT ALL id, name", buildPreparedSQL(select));

        SelectFromStep select2 = selectAll(asQuotedName("id2"), asQuotedName("name"), asName("min(price) as min"));
        assertEquals("SELECT ALL \"id2\", \"name\", min(price) as min", select2.sql());
        assertEquals("SELECT ALL \"id2\", \"name\", min(price) as min", buildPreparedSQL(select2));
    }

    @Test
    void selectDistinctTest() {
        SelectFromStep select = selectDistinct("id", "name");
        assertEquals("SELECT DISTINCT id, name", select.sql());
        assertEquals("SELECT DISTINCT id, name", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });

        select = selectDistinct(asQuotedName("id2"), asQuotedName("name"), asName("min(price) as min"));
        assertEquals("SELECT DISTINCT \"id2\", \"name\", min(price) as min", select.sql());
        assertEquals("SELECT DISTINCT \"id2\", \"name\", min(price) as min", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
    }

    @Test
    void selectDistinctOnTest() {
        SelectFromStep select = selectDistinctOn(new String[]{ "price", "id" }, "id", "name");
        assertEquals("SELECT DISTINCT ON (price, id) id, name", select.sql());
        assertEquals("SELECT DISTINCT ON (price, id) id, name", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });

        select = selectDistinctOn(new Expression[]{ asName("price"), asName("id") }, asQuotedName("id2"), asQuotedName("name"), asName("min(price) as min"));
        assertEquals("SELECT DISTINCT ON (price, id) \"id2\", \"name\", min(price) as min", select.sql());
        assertEquals("SELECT DISTINCT ON (price, id) \"id2\", \"name\", min(price) as min", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
    }

    @Test
    void selectFrom() throws SQLException {
        SelectFromStep select = select("id", "name");

        select.from("book");
        assertEquals("SELECT id, name FROM book", select.from("book").sql());
        assertEquals("SELECT id, name FROM book", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select.from("book as b(id, name)");
        assertEquals("SELECT id, name FROM book as b(id, name)", select.sql());
        assertEquals("SELECT id, name FROM book as b(id, name)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        assertEquals("SELECT id, name FROM \"book\"", select.from(asQuotedName("book")).sql());

        assertEquals("SELECT id, name FROM public.book", select.from(asName("public.book")).sql());

        assertEquals("SELECT id, name FROM table1, table2", select.from("table1", "table2").sql());
    }

    @Test
    void selectFromLocking() throws SQLException {
        Query select = select("*").from("book").forShare().of("book").noWait();
        assertEquals("SELECT * FROM book FOR SHARE OF book NOWAIT", select.sql());
        assertEquals("SELECT * FROM book FOR SHARE OF book NOWAIT", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").forKeyShare().of("book").skipLocked();
        assertEquals("SELECT * FROM book FOR KEY SHARE OF book SKIP LOCKED", select.sql());
        assertEquals("SELECT * FROM book FOR KEY SHARE OF book SKIP LOCKED", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").forUpdate().of("book");
        assertEquals("SELECT * FROM book FOR UPDATE OF book", select.sql());
        assertEquals("SELECT * FROM book FOR UPDATE OF book", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").forNoKeyUpdate().of("book");
        assertEquals("SELECT * FROM book FOR NO KEY UPDATE OF book", select.sql());
        assertEquals("SELECT * FROM book FOR NO KEY UPDATE OF book", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").forNoKeyUpdate().of("book").forKeyShare();
        assertEquals("SELECT * FROM book FOR NO KEY UPDATE OF book FOR KEY SHARE", select.sql());
        assertEquals("SELECT * FROM book FOR NO KEY UPDATE OF book FOR KEY SHARE", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book", "author", "types").forNoKeyUpdate().forKeyShare().of("author", "types").noWait();
        assertEquals("SELECT * FROM book, author, types FOR NO KEY UPDATE FOR KEY SHARE OF author, types NOWAIT", select.sql());
        assertEquals("SELECT * FROM book, author, types FOR NO KEY UPDATE FOR KEY SHARE OF author, types NOWAIT", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromOnly() throws SQLException {
        SelectFromStep select = select(asName("book.name"));

        select.from(fromOnly("book"));
        assertEquals("SELECT book.name FROM ONLY book", select.sql());
        assertEquals("SELECT book.name FROM ONLY book", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select.from(fromOnly("book"), fromOnly("author"));
        assertEquals("SELECT book.name FROM ONLY book, ONLY author", select.sql());
        assertEquals("SELECT book.name FROM ONLY book, ONLY author", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select.from(fromOnly("book").as("b"));
        assertEquals("SELECT book.name FROM ONLY book AS b", select.sql());

        select.from(fromOnly("book").tablesample("BERNOULLI", "30"));
        assertEquals("SELECT book.name FROM ONLY book TABLESAMPLE BERNOULLI(30)", select.sql());
        assertEquals("SELECT book.name FROM ONLY book TABLESAMPLE BERNOULLI(30)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select.from(fromOnly("book")
           .tablesample("BERNOULLI", "30")
           .repeatable(15));
        assertEquals("SELECT book.name FROM ONLY book TABLESAMPLE BERNOULLI(30) REPEATABLE(15)", select.sql());
        assertEquals("SELECT book.name FROM ONLY book TABLESAMPLE BERNOULLI(30) REPEATABLE(15)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromJoin() throws SQLException {
        SelectJoinStep select = select("*").from("book");

        select.join("author").on(true);
        assertEquals("SELECT * FROM book JOIN author ON (true)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON (?)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(1, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").using("id");
        assertEquals("SELECT * FROM book JOIN author USING (id)", select.sql());
        assertEquals("SELECT * FROM book JOIN author USING (id)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").using("id", "name");
        assertEquals("SELECT * FROM book JOIN author USING (id, name)", select.sql());
        assertEquals("SELECT * FROM book JOIN author USING (id, name)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id");
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id").andExists(select("1", "2"));
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND EXISTS (SELECT 1, 2)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND EXISTS (SELECT 1, 2)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id").and(asName("author.id"), IN, select(1));
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND author.id IN (SELECT 1)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND author.id IN (SELECT 1)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id").andNot(asName("author.id"), IN, select(1));
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND NOT author.id IN (SELECT 1)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id AND NOT author.id IN (SELECT 1)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id").or(asName("author.id"), IN, select(1));
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id OR author.id IN (SELECT 1)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id OR author.id IN (SELECT 1)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on("author.id", "=", "author_id").orNot(asName("author.id"), IN, select(1));
        assertEquals("SELECT * FROM book JOIN author ON author.id = author_id OR NOT author.id IN (SELECT 1)", select.sql());

        select = select("*").from("book").join("author").onExists(select("1", "2"));
        assertEquals("SELECT * FROM book JOIN author ON EXISTS (SELECT 1, 2)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON EXISTS (SELECT 1, 2)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").join("author").on(true).innerJoin("types").onExists(select("1", "2"));
        assertEquals("SELECT * FROM book JOIN author ON (true) INNER JOIN types ON EXISTS (SELECT 1, 2)", select.sql());
        assertEquals("SELECT * FROM book JOIN author ON (?) INNER JOIN types ON EXISTS (SELECT 1, 2)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(1, map.size());
        });
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromInnerJoin() throws SQLException {
        SelectJoinStep select = select("*").from("book");

        select.innerJoin("author").on(true);
        assertEquals("SELECT * FROM book INNER JOIN author ON (true)", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").innerJoin("author").on(true).join("sales").onExists(select("1", "2"));
        assertEquals("SELECT * FROM book INNER JOIN author ON (true) JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromLeftJoin() throws SQLException {
        SelectJoinStep select = select("*").from("book");

        select.leftJoin("author").on(true);
        assertEquals("SELECT * FROM book LEFT JOIN author ON (true)", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").leftJoin("author").on(true).innerJoin(asName("sales")).onExists(select("1", "2"));
        assertEquals("SELECT * FROM book LEFT JOIN author ON (true) INNER JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromRightJoin() throws SQLException {
        SelectJoinStep select = select("*").from("book");

        select.rightJoin("author").on(true);
        assertEquals("SELECT * FROM book RIGHT JOIN author ON (true)", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").rightJoin("author").on(true).leftJoin(asName("sales")).onExists(select("1", "2"));
        assertEquals("SELECT * FROM book RIGHT JOIN author ON (true) LEFT JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromFullJoin() throws SQLException {
        SelectJoinStep select = select("*").from("book");

        select.fullJoin("author").on(true);
        assertEquals("SELECT * FROM book FULL JOIN author ON (true)", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").fullJoin("author").on(true).rightJoin(asName("sales")).onExists(select("1", "2"));
        assertEquals("SELECT * FROM book FULL JOIN author ON (true) RIGHT JOIN sales ON EXISTS (SELECT 1, 2)", select.sql());
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromCrossJoin() throws SQLException {
        Query select = select("*").from("book").crossJoin("author");

        assertEquals("SELECT * FROM book CROSS JOIN author", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").crossJoin("author").forShare();
        assertEquals("SELECT * FROM book CROSS JOIN author FOR SHARE", select.sql());
        inBothStatement(select, rs -> {
        });
    }

    @Test
    void selectFromNaturalJoin() throws SQLException {
        Query select = select("*").from("book").naturalJoin("author");
        assertEquals("SELECT * FROM book NATURAL JOIN author", select.sql());
        inBothStatement(select, rs -> {
        });

        select = select("*").from("book").naturalJoin(asName("sales")).crossJoin("author").crossJoin(asName("types"));
        assertEquals("SELECT * FROM book NATURAL JOIN sales CROSS JOIN author CROSS JOIN types", select.sql());
        assertEquals("SELECT * FROM book NATURAL JOIN sales CROSS JOIN author CROSS JOIN types", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });
    }

    //---
    // WHERE
    //---

    @Test
    void selectFromWhere() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND id2 = 2", sql);

        sql = select("id", "name").from("book")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .forShare()
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND id2 = 2 FOR SHARE", sql);

        sql = select("id", "name").from("book")
           .where(asQuotedName("id"), EQUAL, asConstant(1))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE \"id\" = 1", sql);

        sql = select.from("book")
           .where(asName("id"), IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .whereExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE EXISTS (SELECT 1, 2)", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT id2 = 2", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orNot("id3", "=", "3")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT id3 = 3 AND NOT id2 = 2", sql);

        sql = select.from("book")
           .where(asName("id1"), EQUAL, asConstant("1"))
           .or(asQuotedName("id2"), EQUAL, asConstant(2))
           .orNot(asName("table.id3"), EQUAL, asConstant(3))
           .and(asQuotedName("table.id4"), EQUAL, asConstant(4))
           .andNot(asQuotedName("id5"), EQUAL, asConstant(5))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id1 = '1' OR \"id2\" = 2 OR NOT table.id3 = 3 AND \"table\".\"id4\" = 4 AND NOT \"id5\" = 5", sql);
    }

    @Test
    void selectFromWhereBetween() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where(conditionBetween("id", "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id BETWEEN 1 AND 2", sql);

        sql = select.from("book")
           .where(conditionBetween(asQuotedName("id"), asConstant(3), asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE \"id\" BETWEEN 3 AND 4", sql);

        sql = select.from("book")
           .where(conditionBetween(asQuotedName("id"), asConstant(3), asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE \"id\" BETWEEN 3 AND 4", sql);
    }

    @Test
    void selectFromWhereAnd() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND id2 = 2", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .and(asQuotedName("id2"), EQUAL, asConstant(2))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND \"id2\" = 2", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .and(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND \"id2\" = 4", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .and(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void selectFromWhereAndNot() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .andNot("id2", "!=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT id2 != 2", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), EQUAL, asConstant(3))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT \"id2\" = 3", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andNot(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT \"id2\" = 4", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .andNotExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND NOT EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void selectFromWhereOr() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .or("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR id2 = 2", sql);

        sql = select.from("book")
           .where("id", "<>", "1")
           .or(asQuotedName("id2"), NE2, asConstant(3))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id <> 1 OR \"id2\" <> 3", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .or(condition(asQuotedName("id2"), LT, asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR \"id2\" < 4", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .or(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void selectFromWhereOrNot() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .orNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT id2 = 2", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), EQUAL, asConstant(3))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT \"id2\" = 3", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orNot(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT \"id2\" = 4", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .orNotExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 OR NOT EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void selectFromWhereGroup() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("user")
           .where(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", asConstant("timur@shaidullin.net").getName())
              .and(condition("id", "!=", "3")
                 .and("name", "is not", asConstant("max").getName())
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
        SelectFromStep select = select("id", "name");
        String sql = select.from("user")
           .where("id", "=", "1")
           .and(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .and("id2", "=", "2")
           .sql();

        assertEquals("SELECT id, name FROM user WHERE id = 1 AND (name = timur AND phone is null OR email = 'timur@shaidullin.net') AND id2 = 2", sql);

        sql = select("*")
           .from("book")
           .where("year", ">", "2010")
           .orNot(
              conditionBetween("id", "1", "10")
                 .and(asName("name"), operator("="), asConstant("Advanced SQL"))
           )
           .sql();

        assertEquals("SELECT * FROM book WHERE year > 2010 OR NOT (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')", sql);

        sql = select("*")
           .from("book")
           .where(
              conditionBetween("id", "1", "10")
                 .and(asName("name"), operator("="), asConstant("Advanced SQL"))
           )
           .sql();

        assertEquals("SELECT * FROM book WHERE (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')", sql);
    }

    //---
    // GROUP BY
    //---

    @Test
    void selectFromGroupBy() throws SQLException {
        Query select = select("id", "name").from("book")
           .groupBy("id", "name");
        assertEquals("SELECT id, name FROM book GROUP BY id, name", select.sql());
        assertEquals("SELECT id, name FROM book GROUP BY id, name", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        select = select("id", "name").from("book")
           .groupBy(asFunc("ROLLUP", "id", "name"), asFunc("CUBE", "id", "name"));
        assertEquals("SELECT id, name FROM book GROUP BY ROLLUP(id, name), CUBE(id, name)", select.sql());
        assertEquals("SELECT id, name FROM book GROUP BY ROLLUP(id, name), CUBE(id, name)", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {
        });

        Query select2 = select("id", "name").from("book")
           .groupBy(asFunc("ROLLUP", "id", "name"), asFunc("CUBE", "id", "name"))
           .forKeyShare();
        assertEquals("SELECT id, name FROM book GROUP BY ROLLUP(id, name), CUBE(id, name) FOR KEY SHARE", select2.sql());
        assertEquals("SELECT id, name FROM book GROUP BY ROLLUP(id, name), CUBE(id, name) FOR KEY SHARE", buildPreparedSQL(select2));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });

        assertThrows(SQLException.class, () -> inBothStatement(select2, rs -> { }));
    }

    @Test
    void selectFromWhereGroupBy() throws SQLException {
        SelectFromStep select = select("id");
        String sql = select.from("book")
           .where("id", "=", 1)
           .groupBy("id")
           .sql();

        assertEquals("SELECT id FROM book WHERE id = 1 GROUP BY id", sql);
        assertEquals("SELECT id FROM book WHERE id = ? GROUP BY id", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(1, map.size());
            assertEquals(1, map.get(1).getValue());
        });
        inBothStatement(select, rs -> {});
    }

    //---
    // HAVING
    //---

    @Test
    void selectFromHaving() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .having("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id = 1 AND id2 = 2", sql);

        sql = select.from("book")
           .having(asQuotedName("id"), EQUAL, asConstant(1))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING \"id\" = 1", sql);

        sql = select.from("book")
           .having(asName("id"), IN, select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id IN (SELECT 1, 2)", sql);

        sql = select.from("book")
           .havingExists(select("1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING EXISTS (SELECT 1, 2)", sql);

        sql = select.from("book")
           .having("id", "=", "1")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id = 1 AND NOT id2 = 2", sql);

        sql = select.from("book")
           .having("id", "=", "1")
           .orNot("id3", "=", "3")
           .andNot("id2", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id = 1 OR NOT id3 = 3 AND NOT id2 = 2", sql);

        sql = select.from("book")
           .having(asName("id1"), EQUAL, asConstant("1"))
           .or(asQuotedName("id2"), EQUAL, asConstant(2))
           .orNot(asName("table.id3"), EQUAL, asConstant(3))
           .and(asQuotedName("table.id4"), EQUAL, asConstant(4))
           .andNot(asQuotedName("id5"), EQUAL, asConstant(5))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id1 = '1' OR \"id2\" = 2 OR NOT table.id3 = 3 AND \"table\".\"id4\" = 4 AND NOT \"id5\" = 5", sql);


        sql = select.from("book")
           .having("id", "=", "1")
           .orNot("id3", "=", "3")
           .andNot("id2", "=", "2")
           .forShare()
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id = 1 OR NOT id3 = 3 AND NOT id2 = 2 FOR SHARE", sql);
    }

    @Test
    void selectFromWhereHaving() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .having(asName("name"), LT, asConstant("Anna"))
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 HAVING name < 'Anna'", sql);
    }

    @Test
    void selectFromHavingBetween() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .having(conditionBetween("id", "1", "2"))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING id BETWEEN 1 AND 2", sql);

        sql = select.from("book")
           .having(conditionBetween(asQuotedName("id"), asConstant(3), asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING \"id\" BETWEEN 3 AND 4", sql);

        sql = select.from("book")
           .having(conditionBetween(asQuotedName("id"), asConstant(3), asConstant(4)))
           .sql();
        assertEquals("SELECT id, name FROM book HAVING \"id\" BETWEEN 3 AND 4", sql);
    }

    @Test
    void selectFromWhereGroupByHaving() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .groupBy("id")
           .having("id", "=", "2")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 GROUP BY id HAVING id = 2", sql);
    }

    @Test
    void selectFromWhereGroupByHavingOrderBy() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .groupBy("id")
           .having("id", "=", "2")
           .or("id", "=", "2")
           .orderBy("id")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 GROUP BY id HAVING id = 2 OR id = 2 ORDER BY id", sql);
    }

    @Test
    void selectFromWhereGroupByHavingLimit() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .groupBy("id")
           .having("id", "=", "2")
           .or("id", "=", "2")
           .limit(1)
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 GROUP BY id HAVING id = 2 OR id = 2 LIMIT 1", sql);
    }

    @Test
    void selectFromWhereGroupByHavingOffset() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .groupBy("id")
           .having("id", "=", "2")
           .offset(1)
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 GROUP BY id HAVING id = 2 OFFSET 1", sql);
    }

    //---
    // ORDER BY
    //---

    @Test
    void selectFromOrderBy() throws SQLException {
        SelectFromStep select = select("id", "name");

        select.from("book")
           .orderBy("id");

        assertEquals("SELECT id, name FROM book ORDER BY id", select.sql());
        assertEquals("SELECT id, name FROM book ORDER BY id", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});

        select.from("book")
           .orderBy("name", "desc");

        assertEquals("SELECT id, name FROM book ORDER BY name desc", select.sql());
        assertEquals("SELECT id, name FROM book ORDER BY name desc",  buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});

        select.from("book")
           .orderBy("name", "desc", "last");

        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last", select.sql());
        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last",  buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});

        select.from("book")
           .orderBy(orderBy("name", "desc", "last"), orderBy("id"));

        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last, id", select.sql());
        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last, id",  buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});

        select.from("book")
           .orderBy(orderBy("name", "desc", "last"), orderBy("id"))
           .forShare();

        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last, id FOR SHARE", select.sql());
        assertEquals("SELECT id, name FROM book ORDER BY name desc NULLS last, id FOR SHARE",  buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromWhereGroupByOrderBy() throws SQLException {
        SelectFromStep select = select("id");
        String sql = select.from("book")
           .where(15, "=", "id")
           .groupBy("id")
           .orderBy("id")
           .sql();
        assertEquals("SELECT id FROM book WHERE 15 = id GROUP BY id ORDER BY id", sql);
        assertEquals("SELECT id FROM book WHERE ? = id GROUP BY id ORDER BY id",  buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(1, map.size());
            assertEquals(15, map.get(1).getValue());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromWhereOrderBy() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .orderBy("name")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 ORDER BY name", sql);

        sql = select.from("book")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .orderBy("id")
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 1 AND id2 = 2 ORDER BY id", sql);
    }

    //---
    // LIMIT
    //---

    @Test
    void selectFromLimit() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .limit(1)
           .sql();

        assertEquals("SELECT id, name FROM book LIMIT 1", sql);
        assertEquals("SELECT id, name FROM book LIMIT 1", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromLimitLocking() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .limit(1)
           .forShare()
           .sql();

        assertEquals("SELECT id, name FROM book LIMIT 1 FOR SHARE", sql);
        assertEquals("SELECT id, name FROM book LIMIT 1 FOR SHARE", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromOrderByLimit() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .orderBy("id")
           .limit(2)
           .sql();

        assertEquals("SELECT id, name FROM book ORDER BY id LIMIT 2", sql);
        assertEquals("SELECT id, name FROM book ORDER BY id LIMIT 2", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromGroupByLimit() throws SQLException {
        SelectFromStep select = select("id");
        String sql = select.from("book")
           .groupBy("id")
           .limit(2)
           .sql();

        assertEquals("SELECT id FROM book GROUP BY id LIMIT 2", sql);
        assertEquals("SELECT id FROM book GROUP BY id LIMIT 2", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromWhereLimit() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", 22)
           .and("name", "=", asConstant(435).cast("varchar"))
           .limit(3)
           .sql();
        assertEquals("SELECT id, name FROM book WHERE id = 22 AND name = 435::varchar LIMIT 3", sql);
        assertEquals("SELECT id, name FROM book WHERE id = ? AND name = ?::varchar LIMIT 3", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(2, map.size());
            assertEquals(22, map.get(1).getValue());
            assertEquals(435, map.get(2).getValue());
        });
        inBothStatement(select, rs -> {});
    }

    //---
    // OFFSET
    //---

    @Test
    void selectFromOffset() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .offset(1)
           .sql();

        assertEquals("SELECT id, name FROM book OFFSET 1", sql);
        assertEquals("SELECT id, name FROM book OFFSET 1", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromOffsetLocking() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .offset(1)
           .forShare()
           .sql();

        assertEquals("SELECT id, name FROM book OFFSET 1 FOR SHARE", sql);
        assertEquals("SELECT id, name FROM book OFFSET 1 FOR SHARE", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromGroupByOffset() throws SQLException {
        SelectFromStep select = select("id");
        String sql = select.from("book")
           .groupBy("id")
           .limit(1)
           .offset(1)
           .sql();

        assertEquals("SELECT id FROM book GROUP BY id LIMIT 1 OFFSET 1", sql);
        assertEquals("SELECT id FROM book GROUP BY id LIMIT 1 OFFSET 1", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(0, map.size());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromWhereOffset() throws SQLException {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("year", "=", 2010)
           .and("id", "=", 8)
           .offset(3)
           .sql();
        assertEquals("SELECT id, name FROM book WHERE year = 2010 AND id = 8 OFFSET 3", sql);
        assertEquals("SELECT id, name FROM book WHERE year = ? AND id = ? OFFSET 3", buildPreparedSQL(select));
        testBindParameters(select, map -> {
            assertEquals(2, map.size());
            assertEquals(2010, map.get(1).getValue());
            assertEquals(8, map.get(2).getValue());
        });
        inBothStatement(select, rs -> {});
    }

    @Test
    void selectFromWhereLimitOffset() {
        SelectFromStep select = select("id", "name");
        String sql = select.from("book")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .limit(3)
           .offset(3)
           .sql();

        assertEquals("SELECT id, name FROM book WHERE id = 1 AND id2 = 2 LIMIT 3 OFFSET 3", sql);
    }

    //---
    // UNION
    //---

    @Test
    void selectUnion() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .union(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 UNION SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? UNION SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }

    @Test
    void selectFromUnion() {
        SelectFromStep select = select("1", "2");
        select
           .from("book")
           .union(select("2", "2"));
        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM book UNION SELECT 2, 2", sql);

        select = select("1", "2");
        select
           .from("book")
           .crossJoin("author")
           .union(select("2", "2"));
        sql = select.sql();
        assertEquals("SELECT 1, 2 FROM book CROSS JOIN author UNION SELECT 2, 2", sql);

        select = select("1", "2");
        select
           .from("book")
           .join("author").on(true)
           .union(select("2", "2"));
        sql = select.sql();
        assertEquals("SELECT 1, 2 FROM book JOIN author ON (true) UNION SELECT 2, 2", sql);
    }

    @Test
    void selectFromWhereUnion() {
        SelectFromStep select = select("1", "2");
        select
           .from("book")
           .where(conditionBetween(asName("id"), asConstant(1), asConstant(2)))
           .union(select("2", "2"));

        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN 1 AND 2 UNION SELECT 2, 2", select.sql());

        select = select("1", "2");
        select
           .from("book")
           .where(conditionBetween(asName("id"), asConstant(1), asConstant(2)))
           .and("name", "IS NOT", null)
           .union(select("2", "2"));

        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN 1 AND 2 AND name IS NOT NULL UNION SELECT 2, 2", select.sql());
    }

    @Test
    void selectFromWhereGroupByUnion() throws NoSuchFieldException, IllegalAccessException {
        SelectFromStep select = select("1", "2");
        select
           .from("book")
           .where(conditionBetween(asName("id"), asConstant(1), asConstant(2)))
           .groupBy(asQuotedName("book.id"))
           .union(select("2", "2"));

        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN 1 AND 2 GROUP BY \"book\".\"id\" UNION SELECT 2, 2", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN ? AND ? GROUP BY \"book\".\"id\" UNION SELECT 2, 2", buildPreparedSQL(select));
    }

    @Test
    void selectFromWhereGroupByUnionOrderBy() throws NoSuchFieldException, IllegalAccessException {
        SelectFromStep select = select("1", "2");
        select
           .from("book")
           .where(conditionBetween(asName("id"), asConstant(1), asConstant(2)))
           .groupBy(asQuotedName("book.id"))
           .union(select("2", "2"))
           .orderBy("id", "DESC");

        String sql = select.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN 1 AND 2 GROUP BY \"book\".\"id\" UNION SELECT 2, 2 ORDER BY id DESC", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id BETWEEN ? AND ? GROUP BY \"book\".\"id\" UNION SELECT 2, 2 ORDER BY id DESC", buildPreparedSQL(select));
    }

    @Test
    void selectUnionAll() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .unionAll(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 UNION ALL SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? UNION ALL SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }

    //---
    // INTERSECT
    //---

    @Test
    void selectIntersect() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .intersect(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 INTERSECT SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? INTERSECT SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }

    @Test
    void selectIntersectAll() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .intersectAll(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 INTERSECT ALL SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? INTERSECT ALL SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }


    //---
    // EXCEPT
    //---

    @Test
    void selectExcept() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .except(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 EXCEPT SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? EXCEPT SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }

    @Test
    void selectExceptAll() throws NoSuchFieldException, IllegalAccessException {
        Query query = select("1", "2")
           .from("book")
           .where(asName("id"), operator("="), asConstant(1))
           .exceptAll(select("2", "2")
              .from("book")
              .where(asName("id"), operator("="), asConstant(1))
           );
        String sql = query.sql();
        assertEquals("SELECT 1, 2 FROM book WHERE id = 1 EXCEPT ALL SELECT 2, 2 FROM book WHERE id = 1", sql);
        assertEquals("SELECT 1, 2 FROM book WHERE id = ? EXCEPT ALL SELECT 2, 2 FROM book WHERE id = ?", buildPreparedSQL(query));
    }


    @Test
    void selectWithSubselect() throws SQLException {
        Query query = select("*")
           .from(asSubQuery(select("id", "name", "year")
                 .from("book")
                 .where("year", ">", 2000)
              ).as("b")
           )
           .where("b.id", "=", 1)
           .forShare();

        assertEquals("SELECT * FROM (SELECT id, name, year FROM book WHERE year > 2000) AS b WHERE b.id = 1 FOR SHARE", query.sql());
        assertEquals("SELECT * FROM (SELECT id, name, year FROM book WHERE year > ?) AS b WHERE b.id = ? FOR SHARE", buildPreparedSQL(query));
        inBothStatement(query, rs -> {
        });
    }
}