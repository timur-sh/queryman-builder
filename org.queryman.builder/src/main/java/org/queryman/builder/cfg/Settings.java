/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.cfg;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings for this module enumerate here.
 *
 * @author Timur Shaidullin
 */
public final class Settings {
    public static final String[] settings = new String[]{
       Settings.USE_UPPERCASE,
    };

    public static final Map<String, String> DEFAULTS = new HashMap<String, String>();

    static {
        DEFAULTS.put(Settings.USE_UPPERCASE, "true");
    }

    /**
     * If it is {@code true}, the keywords of SQL case are to be uppercase,
     * otherwise them are to be lowercase
     */
    public static final String USE_UPPERCASE = "queryman.builder.use_uppercase";
}
