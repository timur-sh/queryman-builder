/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.cfg;

/**
 * Settings for this module enumerate here.
 *
 * @author Timur Shaidullin
 */
public final class Settings {
    public static final String[] settings = new String[]{
       Settings.TRANSACTION_LEVEL,
       "queryman.builder.transaction1", //todo test case. need to be deleted
    };

    public static final String TRANSACTION_LEVEL = "queryman.builder.transaction";
}
