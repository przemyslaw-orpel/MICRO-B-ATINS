package pl.atins.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.atins.model.Uzytkownik;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@Transactional
@Rollback(true)
public class TestUzytkownikDao {

    @Autowired
    private UzytkownikDao uzytkownikDao;

    @Test
    public void testSave_andFindByLogin_shouldReturnSavedUser() {
        // given
        Uzytkownik u = new Uzytkownik();
        u.setLogin("jan_kowalski");
        u.setEmail("jan_kowalski@test.pl");
        u.setCreatedAt(LocalDateTime.now());

        // when
        uzytkownikDao.save(u);

        Uzytkownik found = uzytkownikDao.findByLogin("jan_kowalski");

        // then
        assertNotNull("Powinien znaleźć użytkownika po loginie", found);
        assertEquals("Login powinien się zgadzać", "jan_kowalski", found.getLogin());
        assertEquals("Email powinien się zgadzać", "jan_kowalski@test.pl", found.getEmail());
    }
    @Test
    public void testFindByLogin_whenNotFound_shouldReturnNull() {
        Uzytkownik found = uzytkownikDao.findByLogin("nie_istnieje_taki_login");
        assertNull("Dla nieistniejącego loginu powinno zwrócić null", found);
    }
    @Test
    public void testSave_whenDuplicateLogin_shouldThrow() {
        // given
        Uzytkownik u1 = new Uzytkownik();
        u1.setLogin("duplikat");
        u1.setEmail("duplikat1@test.pl");
        u1.setCreatedAt(LocalDateTime.now());
        uzytkownikDao.save(u1);

        Uzytkownik u2 = new Uzytkownik();
        u2.setLogin("duplikat"); // ten sam login
        u2.setEmail("duplikat2@test.pl");
        u2.setCreatedAt(LocalDateTime.now());

        // when + then
        assertThrows(Exception.class, () -> uzytkownikDao.save(u2));
    }
}
