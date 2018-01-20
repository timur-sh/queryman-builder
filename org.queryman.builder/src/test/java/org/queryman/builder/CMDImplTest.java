package org.queryman.builder;

import org.junit.jupiter.api.Test;
import org.queryman.builder.boot.ServiceRegister;
import org.queryman.builder.command.select.Select;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CMDImplTest {
    @Test
    void initSQLManager() {
        CMD CMD = Bootstrap.register();
        assertNotNull(CMD);
    }

    void selectAllOfClauses() {
        CMD CMD = Bootstrap.register();

        CMD.select().getSQL();
    }
}