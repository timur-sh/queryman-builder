package org.queryman.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asDollarString;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.asString;

class PostgreSQLTest {
    @Test
    void asConstantTest() {
        assertEquals("table_name", asConstant("table_name").getName());
    }

    @Test
    void asStringTest() {
        assertEquals("'Timur'", asString("Timur").getName());
        assertEquals("'I''m Timur'", asString("I'm Timur").getName());
    }

    @Test
    void asDollarStringTest() {
        assertEquals("$$Timur$$", asDollarString("Timur").getName());

        assertEquals("$$I'm Timur$$", asDollarString("I'm Timur").getName());

        assertEquals("$name$I'm Timur$name$", asDollarString("I'm Timur", "name").getName());
    }

    @Test
    void asNumberTest() {
        assertEquals("234.11", asNumber(234.11).getName());
        assertEquals("0.5", asNumber(.50).getName());
        assertEquals("0.51", asNumber(.51).getName());
        assertEquals("5.1", asNumber(.51E+1).getName());
    }

    @Test
    void asNameTest() {
        assertEquals("column", asName("column").getName());
        assertEquals("\"column\"", asQuotedName("column").getName());
    }

    @Test
    void asQualifiedNameTest() {
        assertEquals("table.column", asName("table.column").getName());
        assertEquals("\"table\".\"column\"", asQuotedName("table.column").getName());
    }

}