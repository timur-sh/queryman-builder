/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader;

import org.queryman.loader.impl.LoaderImpl;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class LoaderExample {
    public void loadBoth() throws IOException, ClassNotFoundException {
        //tag::load-both[]
        Loader loader = new LoaderImpl();
        loader.setPropertiesConfigurationName("queryman-configuration.properties")
           .setXmlConfigurationName("queryman-configuration.xml");
        Properties properties = loader.getConfiguration();
        //end::load-both[]
    }

    public void loadPropertiesCfg() throws IOException, ClassNotFoundException {
        //tag::load-properties[]
        Loader loader = new LoaderImpl();
        loader.setPropertiesConfigurationName("queryman-configuration.properties")
           .load();
        Properties properties = loader.getConfiguration();
        //end::load-properties[]
    }

    public void loadXmlCfg() throws IOException, ClassNotFoundException {
        //tag::load-xml[]
        Loader loader = new LoaderImpl();
        loader.setXmlConfigurationName("queryman-configuration.xml")
           .load();
        Properties properties = loader.getConfiguration();
        //end::load-xml[]
    }
}
