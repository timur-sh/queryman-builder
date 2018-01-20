/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.boot.ServiceRegister;

import java.io.IOException;

/**
 * @author Timur Shaidullin
 */
public class Bootstrap {
    public static CMD register() {
        try {
            return new ServiceRegister().make().getCmd();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
