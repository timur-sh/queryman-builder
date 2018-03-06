package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Query;
import org.queryman.builder.token.Expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.from;
import static org.queryman.builder.PostgreSQL.fromOnly;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.PostgreSQL.select;
import static org.queryman.builder.PostgreSQL.selectAll;
import static org.queryman.builder.PostgreSQL.selectDistinct;
import static org.queryman.builder.PostgreSQL.selectDistinctOn;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildPreparedSQL;

class TreeFormatterTest {

    @Test
    void completedSelect() throws NoSuchFieldException, IllegalAccessException {
        Query query = select(asConstant(1), asName("id"))
           .from(asConstant("book"))
           .leftJoin(asConstant("author")).on(asName("id"), operator("="), asConstant(1))
           .crossJoin(asQuotedName("order"))
           .where(condition(asName("year"), operator("="), asConstant(22)))
           .having(asName("year"), operator("="), asConstant(22))
           .orderBy("test")
           .limit(1)
           .offset(10);


        assertEquals("SELECT ?, id FROM ? LEFT JOIN ? ON id = ? CROSS JOIN \"order\" WHERE year = ? HAVING year = ? ORDER BY test LIMIT 1 OFFSET 10", buildPreparedSQL(query));
    }

    @Test
    void preparedSelectAll() throws NoSuchFieldException, IllegalAccessException {
        Query query;

        query = selectAll(asConstant(1), asName("id"));
        assertEquals("SELECT ALL ?, id", buildPreparedSQL(query));
    }

    @Test
    void preparedSelectDistinct() throws NoSuchFieldException, IllegalAccessException {
        Query query;

        query = selectDistinct(asConstant(1), asName("id"));
        assertEquals("SELECT DISTINCT ?, id", buildPreparedSQL(query));
    }

    @Test
    void preparedSelectDistinctOn() throws NoSuchFieldException, IllegalAccessException {
        Query query;

        Expression[] expressions = { asConstant(1), asConstant(2), asName("date") };
        query = selectDistinctOn(expressions, asConstant("name"), asName("id"));
        assertEquals("SELECT DISTINCT ON (?, ?, date) ?, id", buildPreparedSQL(query));
    }

    @Test
    void preparedSelectFrom() throws NoSuchFieldException, IllegalAccessException {
        Query query;

        query = select(asConstant(1), asName("id")).from(fromOnly("book").as("b").tablesample("SYSTEM", "30").repeatable(10));
        assertEquals("SELECT ?, id FROM ONLY book AS b TABLESAMPLE SYSTEM(30) REPEATABLE(10)", buildPreparedSQL(query));

        query = select(asConstant(1), asName("id")).from(from("book").as("b"));
        assertEquals("SELECT ?, id FROM book AS b", buildPreparedSQL(query));

        query = select(asConstant(1), asName("id")).from("book");
        assertEquals("SELECT ?, id FROM book", buildPreparedSQL(query));
    }

    @Test
    void preparedSelectFromJoin() throws NoSuchFieldException, IllegalAccessException {
        Query query;

        query = select(asConstant(1), asName("id")).from("book").leftJoin("order");
        assertEquals("SELECT ?, id FROM book LEFT JOIN order", buildPreparedSQL(query));
    }
}