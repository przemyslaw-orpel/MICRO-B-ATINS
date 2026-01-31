package pl.atins.service;

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

import pl.atins.dao.FollowerDao;
import pl.atins.dao.UzytkownikDao;
import pl.atins.dao.WpisDao;
import pl.atins.model.Uzytkownik;
import pl.atins.model.Wpis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@Transactional
@Rollback(true)
public class TestMicroblogService {

    @Autowired
    private MicroblogService microblogService;

    @Autowired
    private WpisDao wpisDao;

    @Autowired
    private UzytkownikDao uzytkownikDao;

    @Autowired
    private FollowerDao followerDao;

    private Uzytkownik user;
    private Uzytkownik otherUser;

    @Before
    public void setUp() {
        assertNotNull(microblogService);
        assertNotNull(wpisDao);
        assertNotNull(uzytkownikDao);
        assertNotNull(followerDao);

        user = new Uzytkownik();
        user.setLogin("svc_user");
        user.setEmail("svc_user@test.pl");
        user.setCreatedAt(LocalDateTime.now());
        uzytkownikDao.save(user);

        otherUser = new Uzytkownik();
        otherUser.setLogin("svc_followed");
        otherUser.setEmail("svc_followed@test.pl");
        otherUser.setCreatedAt(LocalDateTime.now());
        uzytkownikDao.save(otherUser);

        // follow: user obserwuje otherUser
        followerDao.follow(user.getId(), otherUser.getId());

        // wpisy: user + followed
        Wpis w1 = new Wpis();
        w1.setUserId(user.getId());
        w1.setTresc("Serwis: mój wpis 1");
        w1.setCreatedAt(LocalDateTime.now().minusMinutes(3));
        wpisDao.save(w1);

        Wpis w2 = new Wpis();
        w2.setUserId(user.getId());
        w2.setTresc("Serwis: mój wpis 2");
        w2.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        wpisDao.save(w2);

        Wpis wf = new Wpis();
        wf.setUserId(otherUser.getId());
        wf.setTresc("Serwis: wpis followowanego");
        wf.setCreatedAt(LocalDateTime.now().minusMinutes(2));
        wpisDao.save(wf);
    }

    @Test
    public void testGetTimeline_shouldReturnOnlyOwnPosts() {
        List<Wpis> timeline = microblogService.getTimeline(user.getLogin());
        assertNotNull(timeline);

        List<String> tresci = timeline.stream().map(Wpis::getTresc).collect(Collectors.toList());
        assertTrue(tresci.contains("Serwis: mój wpis 1"));
        assertTrue(tresci.contains("Serwis: mój wpis 2"));
    }

    @Test
    public void testGetFullTimeline_shouldContainOwnAndFollowedPosts() {
        List<Wpis> full = microblogService.getFullTimeline(user.getLogin());
        assertNotNull(full);

        List<String> tresci = full.stream().map(Wpis::getTresc).collect(Collectors.toList());
        assertTrue(tresci.contains("Serwis: mój wpis 1"));
        assertTrue(tresci.contains("Serwis: mój wpis 2"));
        assertTrue(tresci.contains("Serwis: wpis followowanego"));
    }

    @Test
    public void testGetPublicTimeline_shouldContainPosts() {
        List<Wpis> pub = microblogService.getPublicTimeline();
        assertNotNull(pub);

        List<String> tresci = pub.stream().map(Wpis::getTresc).collect(Collectors.toList());
        assertTrue(tresci.contains("Serwis: mój wpis 1"));
        assertTrue(tresci.contains("Serwis: mój wpis 2"));
    }

    @Test
    public void testFollowAndUnfollow_shouldChangeFollowingState() {
        // na starcie user followuje otherUser (ustawione w setUp)
        assertTrue(microblogService.isFollowing(user.getLogin(), otherUser.getLogin()));

        microblogService.unfollow(user.getLogin(), otherUser.getLogin());
        assertTrue(!microblogService.isFollowing(user.getLogin(), otherUser.getLogin()));

        microblogService.follow(user.getLogin(), otherUser.getLogin());
        assertTrue(microblogService.isFollowing(user.getLogin(), otherUser.getLogin()));
    }
}
