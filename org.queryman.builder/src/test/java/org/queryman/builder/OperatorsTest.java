package org.queryman.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.Operators.ILIKE;
import static org.queryman.builder.Operators.NOT_ILIKE;
import static org.queryman.builder.Operators.NOT_LIKE;
import static org.queryman.builder.Operators.LIKE;
import static org.queryman.builder.Operators.map;

class OperatorsTest {
    @Test
    void like() {
        assertEquals(NOT_LIKE.getName(), "NOT LIKE");
        assertEquals(LIKE.getName(), "LIKE");
        assertEquals("LIKE", map("LIKE").getName());
        assertEquals("LIKE", map("like").getName());
    }

    @Test
    void ilike() {
        assertEquals(NOT_ILIKE.getName(), "NOT ILIKE");
        assertEquals(ILIKE.getName(), "ILIKE");
        assertEquals("ILIKE", map("ILIKE").getName());
        assertEquals("ILIKE", map("ilike").getName());
    }

}