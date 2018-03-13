/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.queryman.builder.boot.adapter.JaxbMetadataAdapter;
import org.queryman.builder.boot.jaxb.JaxbCfg;
import org.queryman.builder.utils.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class JaxbLoader extends AbstractConfigLoader {
    private final static Logger LOG = LogManager.getLogger("org.queryman.builder.boot");

    private JaxbCfg jaxb = new JaxbCfg();

    public JaxbLoader(String cfgFile) {
        super(cfgFile);
    }

    @Override
    public boolean load() throws IOException, ClassNotFoundException {
        if (StringUtils.isEmpty(cfgFile)) {
            throw new IllegalStateException("Xml file is not specified");
        }
        InputStream stream = getResource(cfgFile);

        try {
            jaxb = (JaxbCfg) JAXBContext.newInstance(JaxbCfg.class)
                .createUnmarshaller()
                .unmarshal(stream);
            return true;
        } catch (JAXBException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    public Properties getConfiguration() {
        return JaxbMetadataAdapter.convert(jaxb);
    }
}
