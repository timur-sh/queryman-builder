package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Query;
import org.queryman.builder.Queryman;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.insertInto;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.targetColumn;
import static org.queryman.builder.Queryman.targetExpression;
import static org.queryman.builder.Queryman.with;
import static org.queryman.builder.Queryman.withRecursive;
import static org.queryman.builder.TestHelper.testBindParameters;
import static org.queryman.builder.ast.TreeFormatterUtil.buildPreparedSQL;


class InsertImplTest extends BaseTest {

    @Test
    void insertFull1() throws SQLException {
        Query query = Queryman.insertInto("book")
           .as("b")
           .columns("id", "name")
           .overridingSystemValue()
           .values(1, "test")
           .onConflict()
           .onConstraint("index_name")
           .doNothing()
        ;

        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'test') ON CONFLICT ON CONSTRAINT index_name DO NOTHING", query.sql());
        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES (?, ?) ON CONFLICT ON CONSTRAINT index_name DO NOTHING", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(2, map.size());
        });
        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull2() throws SQLException {
        Query query = Queryman.insertInto("book")
           .columns("id", "name")
           .overridingUserValue()
           .values(1, "test")
           .onConflict(targetColumn("tt", "44", "55"), targetExpression("qq"))
           .where("id", "=", 2)
           .and(3, "!=", "id")
           .doNothing()
           .returning("id")
        ;
        assertEquals("INSERT INTO book (id, name) OVERRIDING USER VALUE VALUES (1, 'test') ON CONFLICT (tt COLLATE 44 55, (qq)) WHERE id = 2 AND 3 != id DO NOTHING RETURNING id", query.sql());
        assertEquals("INSERT INTO book (id, name) OVERRIDING USER VALUE VALUES (?, ?) ON CONFLICT (tt COLLATE 44 55, (qq)) WHERE id = ? AND ? != id DO NOTHING RETURNING id", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(4, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals("test", map.get(2).getValue());
            assertEquals(2, map.get(3).getValue());
            assertEquals(3, map.get(4).getValue());
        });
        //todo this is an incorrect query made for testing purpose
//        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull3() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Query query = Queryman.insertInto("book")
           .as("b")
           .defaultValues()
           .onConflict("name")
           .doUpdate()
           .set("id", 1)
           .set("name", asName("EXCLUDED.name"))
           .where("EXCLUDED.id", "=", 1)
           .and("EXCLUDED.id", "!=", 3)
           .returning("id")
        ;
        assertEquals("INSERT INTO book AS b DEFAULT VALUES ON CONFLICT (name) DO UPDATE SET id = 1, name = EXCLUDED.name WHERE EXCLUDED.id = 1 AND EXCLUDED.id != 3 RETURNING id", query.sql());
        assertEquals("INSERT INTO book AS b DEFAULT VALUES ON CONFLICT (name) DO UPDATE SET id = ?, name = EXCLUDED.name WHERE EXCLUDED.id = ? AND EXCLUDED.id != ? RETURNING id", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(3, map.size());
        });
        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull4() {
        String sql = Queryman.insertInto("book")
           .as("b")
           .columns("id", "name")
           .query(select("id", "name").from("book"))
           .onConflict()
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) (SELECT id, name FROM book) ON CONFLICT DO NOTHING RETURNING id", sql);
    }

    @Test
    void withSelect() throws SQLException {
        Query query = with("latest", "id", "name")
           .as(select("id", "name")
                 .from("book")
                 .where("name", "=", asConstant("test")))
           .with("newest", "id", "name")
           .as(insertInto("book").defaultValues().returning("id", "name"))
           .insertInto("book").defaultValues();

        assertEquals("WITH latest (id, name) AS (SELECT id, name FROM book WHERE name = 'test'), newest (id, name) AS (INSERT INTO book DEFAULT VALUES RETURNING id, name) INSERT INTO book DEFAULT VALUES", query.sql());
        assertEquals("WITH latest (id, name) AS (SELECT id, name FROM book WHERE name = ?), newest (id, name) AS (INSERT INTO book DEFAULT VALUES RETURNING id, name) INSERT INTO book DEFAULT VALUES", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(1, map.size());
            assertEquals("test", map.get(1).getValue());
        });
        inBothStatement(query, rs -> { });
    }

    @Test
    void withRecursiveSelect()  throws SQLException {
        Query query = withRecursive("latest", "id", "name")
           .as(insertInto("book").defaultValues().returning("id", "name"))
           .with("newest", "id", "name")
           .as(select("id", "name")
              .from("book")
              .where("name", "=", asConstant("test")))
           .insertInto("book").defaultValues();

        assertEquals("WITH RECURSIVE latest (id, name) AS (INSERT INTO book DEFAULT VALUES RETURNING id, name), newest (id, name) AS (SELECT id, name FROM book WHERE name = 'test') INSERT INTO book DEFAULT VALUES", query.sql());
        assertEquals("WITH RECURSIVE latest (id, name) AS (INSERT INTO book DEFAULT VALUES RETURNING id, name), newest (id, name) AS (SELECT id, name FROM book WHERE name = ?) INSERT INTO book DEFAULT VALUES", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(1, map.size());
            assertEquals("test", map.get(1).getValue());
        });
        inBothStatement(query, rs -> { });
    }
}