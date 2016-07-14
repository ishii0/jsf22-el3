package com.github.ishii0.faces.el;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author ishii
 */
@RunWith(Arquillian.class)
public class ClassELResolverTest {

    @ArquillianResource
    private URL base;

    WebClient webClient;

    private static final String WEBAPP_SRC = "src/test/webapp";
    HtmlPage page;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addClass(ClassELResolver.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "faces-config.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "beans.xml"));
    }

    public ClassELResolverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setup() throws IOException {
        webClient = new WebClient();
        page = webClient.getPage(base + "/faces/index.xhtml");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testStaticMethod() throws IOException {
        HtmlParagraph anchor = (HtmlParagraph) page.getElementById("staticMethod");
        assertTrue(anchor.getTextContent().equals("java.lang.Object"));
    }

    @Test
    public void testStaticField() throws IOException {
        HtmlParagraph anchor = (HtmlParagraph) page.getElementById("staticField");
        assertTrue(anchor.getTextContent().equals(String.valueOf(Boolean.TRUE)));
    }
}
