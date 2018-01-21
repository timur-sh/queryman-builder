package org.queryman.builder;

import org.junit.jupiter.api.Test;
import org.queryman.builder.boot.ServiceRegister;

import static org.junit.jupiter.api.Assertions.*;

class CMDImplTest {
    @Test
    void initSQLManager() {
        CMD CMD = new ServiceRegister().make().getCmd();
        assertNotNull(CMD);
    }

    void selectAllOfClauses() {
        CMD CMD = new ServiceRegister().make().getCmd();

        CMD.select().getSQL();
    }
}