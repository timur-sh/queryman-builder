package org.queryman.builder;

import org.junit.jupiter.api.Test;
import org.queryman.builder.boot.ServiceRegister;

import static org.junit.jupiter.api.Assertions.*;

class CommandImplTest {
    @Test
    void initSQLManager() {
        Command command = new ServiceRegister().make().getCommand();
        assertNotNull(command);
    }

    @Test
    void selectAllOfClauses() {
        Command command = new ServiceRegister().make().getCommand();

        String sql = command.select().sql();
    }
}