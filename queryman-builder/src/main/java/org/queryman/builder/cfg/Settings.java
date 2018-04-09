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
 * Settings for this module are here.
 *
 * @author Timur Shaidullin
 */
public final class Settings {
    public static final String[] settings = new String[]{
       Settings.USE_UPPERCASE,
    };

    public static final Map<String, String> DEFAULTS = new HashMap<String, String>();

    static {
        DEFAULTS.put(Settings.USE_UPPERCASE, "false");
    }

    /**
     * If value equal {@code true}, then the keywords of SQL are converted
     * to uppercase otherwise them are converted to lowercase.
     */
    public static final String USE_UPPERCASE = "queryman.builder.use_uppercase";
}
