package org.queryman.builder;

import org.junit.jupiter.api.Test;
import org.queryman.builder.boot.ServiceRegister;
import org.queryman.builder.command.select.Select;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CMDImplTest {
    @Test
    void initSQLManager() throws IOException, ClassNotFoundException {
        CMD CMD = new ServiceRegister().make().getCmd();
        assertNotNull(CMD);

//        assertTrue(CMD.selectCmd() instanceof Select);
    }

    void selectAllOfClauses() throws IOException, ClassNotFoundException {
        CMD CMD = new ServiceRegister().make().getCmd();

        CMD.select().getSQL();
    }
}