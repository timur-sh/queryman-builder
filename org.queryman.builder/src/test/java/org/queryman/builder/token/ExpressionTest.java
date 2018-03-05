package org.queryman.builder.token;

import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.token.expression.prepared.BooleanExpression;
import org.queryman.builder.token.expression.prepared.ByteExpression;
import org.queryman.builder.token.expression.prepared.BytesExpression;
import org.queryman.builder.token.expression.prepared.DateExpression;
import org.queryman.builder.token.expression.prepared.DoubleExpression;
import org.queryman.builder.token.expression.prepared.FloatExpression;
import org.queryman.builder.token.expression.prepared.IntegerExpression;
import org.queryman.builder.token.expression.prepared.LongExpression;
import org.queryman.builder.token.expression.prepared.ShortExpression;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.queryman.builder.PostgreSQL.asArray;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asDollarString;
import static org.queryman.builder.PostgreSQL.asFunc;
import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asSubQuery;
import static org.queryman.builder.PostgreSQL.select;

class ExpressionTest {
    @Test
    void constantExpression() {
        assertEquals("table_name", asName("table_name").getName());
        assertEquals("234.11", asConstant(234.11d).getName());
        assertTrue(asConstant(234.11d) instanceof DoubleExpression);

        assertEquals("0.5", asConstant(.50f).getName());
        assertTrue(asConstant(.50f) instanceof FloatExpression);

        assertEquals("1", asConstant((byte) 1).getName());
        assertTrue(asConstant((byte) 1) instanceof ByteExpression);

        assertEquals("1", asConstant(1).getName());
        assertTrue(asConstant(1) instanceof IntegerExpression);

        assertEquals("1", asConstant(1L).getName());
        assertTrue(asConstant(1L) instanceof LongExpression);

        assertEquals("1", asConstant((short) 1).getName());
        assertTrue(asConstant((short) 1) instanceof ShortExpression);

        assertEquals("true", asConstant(true).getName());
        assertTrue(asConstant(true) instanceof BooleanExpression);

        Date d1 = new Date();
        assertEquals("2018-03-05", asConstant(d1).getName());
        assertTrue(asConstant(d1) instanceof DateExpression);

        java.sql.Date d2 = new java.sql.Date(d1.getTime());
        assertEquals("2018-03-05", asConstant(d2).getName());
        assertTrue(asConstant(d2) instanceof DateExpression);
    }

    @Test
    void constantArrayExpression() {
        byte[] b = { 1, 2, 3 };
        Byte[] b1 = { 1, 2, 3 };

        assertEquals("ARRAY[1, 2, 3]", asConstant(b).getName());
        assertTrue(asConstant(b) instanceof BytesExpression);

        assertEquals("ARRAY[1, 2, 3]", asConstant(b1).getName());
        assertTrue(asConstant(b1) instanceof BytesExpression);
    }

    @Test
    void stringConstantExpression() {
        assertEquals("'Timur'", asConstant("Timur").getName());

        assertEquals("'I''m Timur'", asConstant("I'm Timur").getName());
    }

    @Test
    void dollarStringExpression() {
        assertEquals("$$Timur$$", asDollarString("Timur").getName());

        assertEquals("$$I'm Timur$$", asDollarString("I'm Timur").getName());

        assertEquals("$name$I'm Timur$name$", asDollarString("I'm Timur", "name").getName());
    }

    @Test
    void columnReference() {
        assertEquals("table.column", asName("table.column").getName());
        assertEquals("\"table\".\"column\"", asQuotedName("table.column").getName());
    }

    @Test
    void fieldSelection() {
        assertEquals("expression.fieldname", asName("expression.fieldname").getName());
        assertEquals(".fieldname", asName(".fieldname").getName());
        assertEquals("(tablename.compositecol).id", asName("(tablename.compositecol).id").getName());
    }

    @Test
    void asConstantListExpression() {
        assertEquals("()", asList().getName());

        Expression[] numbers = new Expression[]{ asConstant("one"), asConstant("two") };
        assertEquals("('one', 'two')", asList(numbers).getName());

        Expression strings = asList("one", "two", "three", "four", "five", "six");
        assertEquals("('one', 'two', 'three', 'four', 'five', 'six')", strings.getName());

        Expression chars = asList('1', '2', '3', '4', '5', '6');
        assertEquals("('1', '2', '3', '4', '5', '6')", chars.getName());

        Expression integers = asList(1, 0x1a);
        assertEquals("(1, 26)", integers.getName());

        Expression floats = asList(1f, 2e1);
        assertEquals("(1.0, 20.0)", floats.getName());

        Expression doubles = asList(1d, 2e-3);
        assertEquals("(1.0, 0.002)", doubles.getName());

        Expression longs = asList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("(1, 2, -1253794, 123)", longs.getName());

        assertEquals("('one', 'two')", asList(List.of("one", "two")).getName());

        assertEquals("('1', '2')", asList(List.of('1', '2')).getName());

        assertEquals("(1, 2)", asList(List.of(1, 2)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1f, 2f)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1d, 2d)).getName());
    }

    @Test
    void asListExpression() {
        assertEquals("()", asList().getName());

        Number[] numbers2 = { 1, 2 };
        assertEquals("(1, 2)", asList(numbers2).getName());

        Expression strings = asList("1", "2");
        assertEquals("('1', '2')", strings.getName());

        Expression chars = asList('1', '2', '3', '4', '5', '6');
        assertEquals("('1', '2', '3', '4', '5', '6')", chars.getName());

        Expression integers = asList(1, 0x1a);
        assertEquals("(1, 26)", integers.getName());

        Expression floats = asList(1f, 2e1);
        assertEquals("(1.0, 20.0)", floats.getName());

        Expression doubles = asList(1d, 2e-3);
        assertEquals("(1.0, 0.002)", doubles.getName());

        Expression longs = asList(1L, 2L, 0xFF_EC_DE_5E, 1_2_3L);
        assertEquals("(1, 2, -1253794, 123)", longs.getName());

        assertEquals("('one', 'two')", asList(List.of("one", "two")).getName());

        assertEquals("('1', '2')", asList(List.of('1', '2')).getName());

        assertEquals("(1, 2)", asList(List.of(1, 2)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1f, 2f)).getName());

        assertEquals("(1.0, 2.0)", asList(List.of(1d, 2d)).getName());
    }

    @Test
    void arrayTest() {
        assertEquals("ARRAY[]", asArray().getName());

        Integer[] numbers = { 1, 2 };
        assertEquals("ARRAY[1, 2]", asArray(numbers).getName());
        assertEquals("ARRAY[1, 2]::bigint[]", asArray(numbers).cast("bigint[]").getName());
        assertEquals("ARRAY[1, 2]::bigint[]", asArray(numbers).cast("bigint[]").getName());
        assertEquals("ARRAY[1, 2]", asArray(List.of(numbers)).getName());
        assertEquals("ARRAY[1, 2]", asArray(List.of(1, 2)).getName());
        assertEquals("ARRAY[]", asArray().getName());

        assertEquals("ARRAY[]", asArray().getName());

        String[] numbers2 = { "1", "2" };
        assertEquals("ARRAY['1', '2']", asArray(numbers2).getName());
        assertEquals("ARRAY['1', '2']", asArray(List.of(asConstant("1"), asConstant("2"))).getName());
    }

    @Test
    void functionAll() {
        assertEquals("ALL(ARRAY[])", asFunc("ALL", asArray()).getName());
        assertEquals("ALL(ARRAY[1, 2])", asFunc("ALL", asArray(1, 2)).getName());
        assertEquals("ALL()", asFunc("ALL", asList()).getName());
        assertEquals("ALL(1, 2)", asFunc("ALL", asList(1, 2)).getName());
    }

    @Test
    void aliasTest() {
        assertEquals("book AS b", asName("book").as("b").getName());
    }

    @Test
    void valuesTest() {
        Expression values = PostgreSQL.values(asList(1, 2), asList(3, 4));

        assertEquals("VALUES(1, 2), (3, 4)", values.getName());
        assertEquals("(VALUES(1, 2), (3, 4)) AS point(x, y)", values.as("point", "x", "y").getName());
    }

    @Test
    void existsTest() {
        String sql =select(
           asFunc("EXISTS", asSubQuery(select("*").from("book"))).as("exists")
        ).sql();

        assertEquals("SELECT EXISTS(SELECT * FROM book) AS exists", sql);

    }
}