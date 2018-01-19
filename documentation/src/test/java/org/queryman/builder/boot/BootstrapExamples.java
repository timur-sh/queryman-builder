/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;


import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Timur Shaidullin
 */
public class BootstrapExamples {
    @Test
    public void serviceRegister() throws IOException, ClassNotFoundException {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.make();

    }
}
