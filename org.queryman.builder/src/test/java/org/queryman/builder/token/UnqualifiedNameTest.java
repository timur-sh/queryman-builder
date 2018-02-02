package org.queryman.builder.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.PostgreSQL.qualifiedName;
import static org.queryman.builder.PostgreSQL.unqualifiedName;

class UnqualifiedNameTest {
    @Test
    void nameWithoutSchema() {
        Name name = unqualifiedName("books");
        assertEquals("\"books\"", name.getName());

        Name name2 = unqualifiedName(null);
        assertNull(name2.getName());
    }
}