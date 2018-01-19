/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader;

import org.queryman.loader.jaxb.JaxbCfg;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is abstract class for JAXB that must be extending by a
 * loader for different configurations.
 *
 * @author Timur Shaidullin
 */
public class JaxbLoader extends AbstractConfigLoader {
    private JaxbCfg configuration = new JaxbCfg();

    public JaxbLoader() {
        super();
    }

    public JaxbLoader(String cfgFile) {
        super(cfgFile);
    }

    @Override
    public boolean load() throws IOException, ClassNotFoundException {
        if (StringUtils.isEmpty(cfgFile)) {
            throw new FileNotFoundException("Configuration file not specified");
        }
        InputStream stream = getResource(cfgFile);

        try {
            configuration = (JaxbCfg) JAXBContext.newInstance(JaxbCfg.class)
                .createUnmarshaller()
                .unmarshal(stream);
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();  //todo log stacktrace
            return false;
        }
    }

    public Properties getConfiguration() {
        return configuration.properties;
    }
}
