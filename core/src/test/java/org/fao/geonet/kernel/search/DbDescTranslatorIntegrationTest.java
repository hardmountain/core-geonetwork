package org.fao.geonet.kernel.search;

import org.fao.geonet.AbstractCoreIntegrationTest;
import org.fao.geonet.domain.Localized;
import org.jdom.JDOMException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test DbDescTranslator
 * User: Jesse
 * Date: 9/9/13
 * Time: 9:07 AM
 */
public class DbDescTranslatorIntegrationTest extends AbstractCoreIntegrationTest {
    @Autowired
    ApplicationContext _appContext;

    @Test
    public void testTranslateStringKey() throws Exception {
        testTranslation("key");
    }

    @Test
    public void testTranslateIntKey() throws Exception {
        testTranslation(1);
    }

    private<T extends Serializable> void testTranslation(T key) throws IOException, JDOMException, ClassNotFoundException {
        JpaRepository<Object, T> repo = mock(JpaRepository.class);
        ValueObject value = new ValueObject();

        when(repo.findOne(key)).thenReturn(value);
        final String beanName = "testRepo";
        StaticApplicationContext appContext = new StaticApplicationContext(_appContext);
        appContext.getBeanFactory().registerSingleton(beanName, repo);

        DbDescTranslator translator = new DbDescTranslator(appContext, "eng", repo.getClass().getName() + ":value");
        final String translation = translator.translate("" + key);
        assertEquals(value.getLabel("eng"), translation);

        DbDescTranslator translator2 = new DbDescTranslator(appContext, "eng", beanName + ":value");
        final String translation2 = translator2.translate("" + key);
        assertEquals(value.getLabel("eng"), translation2);
    }

    static class ValueObject extends Localized {
        String value = UUID.randomUUID().toString();

        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("eng", "engValue");

            setLabelTranslations(map);
        }
        String getValue() {
            return value;
        }
    }
}
