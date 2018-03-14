package org.queryman.builder.token;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Queryman;
import org.queryman.builder.token.expression.prepared.BigDecimalExpression;
import org.queryman.builder.token.expression.prepared.BooleanExpression;
import org.queryman.builder.token.expression.prepared.ByteExpression;
import org.queryman.builder.token.expression.prepared.BytesExpression;
import org.queryman.builder.token.expression.prepared.DateExpression;
import org.queryman.builder.token.expression.prepared.DoubleExpression;
import org.queryman.builder.token.expression.prepared.FloatExpression;
import org.queryman.builder.token.expression.prepared.IntegerExpression;
import org.queryman.builder.token.expression.prepared.LongExpression;
import org.queryman.builder.token.expression.prepared.NullExpression;
import org.queryman.builder.token.expression.prepared.ShortExpression;
import org.queryman.builder.token.expression.prepared.TimeExpression;
import org.queryman.builder.token.expression.prepared.TimestampExpression;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.queryman.builder.Queryman.all;
import static org.queryman.builder.Queryman.any;
import static org.queryman.builder.Queryman.asArray;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asDollarString;
import static org.queryman.builder.Queryman.asFunc;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asQuotedName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.some;
import static org.queryman.builder.Queryman.values;

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
        SimpleDateFormat format = new SimpleDateFormat("Y-MM-dd");

        assertEquals("'" + format.format(d1) + "'", asConstant(d1).getName());
        assertTrue(asConstant(d1) instanceof DateExpression);

        java.sql.Date d2 = new java.sql.Date(d1.getTime());
        assertEquals("'" + format.format(d2) + "'", asConstant(d2).getName());
        assertTrue(asConstant(d2) instanceof DateExpression);

        assertTrue(asConstant(new Time(System.currentTimeMillis())) instanceof TimeExpression);

        assertTrue(asConstant(new Timestamp(System.currentTimeMillis())) instanceof TimestampExpression);

        assertEquals("NULL", asConstant(null).getName());
        assertTrue(asConstant(null) instanceof NullExpression);

        assertEquals("1", asConstant(new BigDecimal(1)).getName());
        assertTrue(asConstant(new BigDecimal(1)) instanceof BigDecimalExpression);
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
        assertEquals("(one, two, three, four, five, six)", strings.getName());

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

        assertEquals("('one', 'two')", asList(asConstant("one"), asConstant("two")).getName());

        assertEquals("('1', '2')", asList(List.of(asConstant("1"), asConstant('2'))).getName());

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
        assertEquals("(1, 2)", strings.getName());

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

        assertEquals("('one', 'two')", asList(asConstant("one"), asConstant("two")).getName());

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
    void aliasTest() {
        assertEquals("book AS b", asName("book").as("b").getName());
    }


    //------
    // FUNCTIONS AND OPERATORS
    //------

    @Test
    void functionAll() {
        assertEquals("ALL(ARRAY[])", all(asArray()).getName());
        assertEquals("ALL(ARRAY[1, 2])", all(asArray(1, 2)).getName());
        assertEquals("ALL()", all(asList()).getName());
        assertEquals("ALL(1, 2)", all(asList(1, 2)).getName());
        assertEquals("ALL(NULL, 1)", all(null, 1).getName());
        assertEquals("ALL(1, 2)", all(1, 2).getName());
        assertEquals("ALL(SELECT 1)", all(select(1)).getName());
        assertEquals("ALL(VALUES(1), (2))", all(values(1, 2)).getName());
    }

    @Test
    void functionAny() {
        assertEquals("ANY(ARRAY[])", any(asArray()).getName());
        assertEquals("ANY(ARRAY[1, 2])", any(asArray(1, 2)).getName());
        assertEquals("ANY()", any(asList()).getName());
        assertEquals("ANY(1, 2)", any(asList(1, 2)).getName());
        assertEquals("ANY(NULL, 1)", any(null, 1).getName());
        assertEquals("ANY(2)", any(2).getName());
        assertEquals("ANY(1, 2)", any(1, 2).getName());
        assertEquals("ANY(SELECT id FROM book)", any(select("id").from("book")).getName());
        assertEquals("ANY(VALUES(1), (2))", any(values(1, 2)).getName());
    }

    @Test
    void functionSome() {
        assertEquals("SOME(ARRAY[])", some(asArray()).getName());
        assertEquals("SOME(ARRAY[1, 2])", some(asArray(1, 2)).getName());
        assertEquals("SOME()", some(asList()).getName());
        assertEquals("SOME(1, 2)", some(asList(1, 2)).getName());
        assertEquals("SOME(NULL, 1)", some(null, 1).getName());
        assertEquals("SOME(2)", some(2).getName());
        assertEquals("SOME(1, 2)", some(1, 2).getName());
        assertEquals("SOME(SELECT 1)", some(select(1)).getName());
        assertEquals("SOME(VALUES(1), (2))", some(values(1, 2)).getName());
    }

    @Test
    void valuesTest() {
        assertEquals("VALUES(1, 2), (3, 4)", values(asList(1, 2), asList(3, 4)).getName());
        assertEquals("(VALUES(1, 2), (3, 4)) AS point(x, y)", values(asList(1, 2), asList(3, 4)).as("point", "x", "y").getName());


        assertEquals("VALUES(1), (2)", values(1, 2).getName());
        assertEquals("(VALUES(1), (2)) AS point(x, y)", values(1, 2).as("point", "x", "y").getName());
    }

    @Test
    void existsTest() {
        String sql =select(
           asFunc("EXISTS", asSubQuery(select("*").from("book"))).as("exists")
        ).sql();

        assertEquals("SELECT EXISTS(SELECT * FROM book) AS exists", sql);
    }

    @Test
    void commonTest() {
        assertEquals("concat('price', 2, 'USD')", asFunc("concat", asConstant("price"), 2, asConstant("USD")).getName());
    }
}