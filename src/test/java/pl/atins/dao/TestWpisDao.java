package pl.atins.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.atins.model.Uzytkownik;
import pl.atins.model.Wpis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@Transactional
@Rollback(true)
public class TestWpisDao {

    @Autowired
    private WpisDao wpisDao;

    @Autowired
    private UzytkownikDao uzytkownikDao;

    private Uzytkownik user;

    @Before
    public void setUp() {
        assertNotNull(wpisDao);
        assertNotNull(uzytkownikDao);

        user = new Uzytkownik();
        user.setLogin("timeline_user");
        user.setEmail("timeline_user@test.pl");
        user.setCreatedAt(LocalDateTime.now());
        uzytkownikDao.save(user);

        Wpis w1 = new Wpis();
        w1.setUserId(user.getId());
        w1.setTresc("Pierwszy wpis");
        w1.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        wpisDao.save(w1);

        Wpis w2 = new Wpis();
        w2.setUserId(user.getId());
        w2.setTresc("Drugi wpis");
        w2.setCreatedAt(LocalDateTime.now());
        wpisDao.save(w2);
    }

    @Test
    public void testGetUserTimeline_shouldReturnUserPosts() {
        List<Wpis> timeline = wpisDao.getUserTimeline(user.getId());

        assertNotNull(timeline);
        // minimalna asercja, bez zgadywania sortowania
        assertEquals("Timeline użytkownika powinien mieć 2 wpisy", 2, timeline.size());
    }

    @Test
    public void testGetPublicTimeline_shouldContainOurPosts() {
        List<Wpis> publicTimeline = wpisDao.getPublicTimeline();

        assertNotNull(publicTimeline);

        List<String> tresci = publicTimeline.stream()
                .map(Wpis::getTresc)
                .collect(Collectors.toList());

        assertTrue("Public timeline powinien zawierać 'Pierwszy wpis'", tresci.contains("Pierwszy wpis"));
        assertTrue("Public timeline powinien zawierać 'Drugi wpis'", tresci.contains("Drugi wpis"));
    }
}