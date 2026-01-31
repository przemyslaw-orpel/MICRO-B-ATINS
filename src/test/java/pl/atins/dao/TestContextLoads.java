package pl.atins.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.atins.dao.UzytkownikDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@Transactional
@Rollback(true)
public class TestContextLoads {

    @Autowired
    private UzytkownikDao uzytkownikDao;

    @Test
    public void contextLoads_andDaoIsWired() {
        assertNotNull("DAO powinien zostać wstrzyknięty przez Spring", uzytkownikDao);
    }
}