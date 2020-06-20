package architecture;

import architecture.web.controllers.HomeController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonalSiteApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(this.wac);

        ServletContext servletContext = this.wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean(HomeController.class));
    }

}
