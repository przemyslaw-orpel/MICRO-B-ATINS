package pl.atins.dao;

import java.time.LocalDateTime;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
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
public class TestFollowerDao {

    @Autowired
    private FollowerDao followerDao;

    @Autowired
    private UzytkownikDao uzytkownikDao;

    private Uzytkownik followee; // śledzony
    private Uzytkownik follower; // śledzący

    @Before
    public void setUp() {
        assertNotNull(followerDao);
        assertNotNull(uzytkownikDao);

        followee = new Uzytkownik();
        followee.setLogin("followee_user");
        followee.setEmail("followee@test.pl");
        followee.setCreatedAt(LocalDateTime.now());

        follower = new Uzytkownik();
        follower.setLogin("follower_user");
        follower.setEmail("follower@test.pl");
        follower.setCreatedAt(LocalDateTime.now());

        uzytkownikDao.save(followee);
        uzytkownikDao.save(follower);
    }

    @Test
    public void smokeTest_contextAndDaos() {
        assertNotNull(followee);
        assertNotNull(follower);
    }

    @Test
    public void testFollow_shouldCreateRelation() {
        // given
        Integer followerId = follower.getId();
        Integer followeeId = followee.getId();

        // when
        followerDao.follow(followerId, followeeId);

        // then
        boolean exists = followerDao.exists(followerId, followeeId);
        assertTrue("Relacja follow powinna istnieć", exists);
    }

    @Test
    public void testUnfollow_shouldRemoveRelation() {
        // given
        Integer followerId = follower.getId();
        Integer followeeId = followee.getId();

        followerDao.follow(followerId, followeeId);
        assertTrue("Relacja powinna istnieć po follow()", followerDao.exists(followerId, followeeId));

        // when
        followerDao.unfollow(followerId, followeeId);

        // then
        assertFalse("Relacja nie powinna istnieć po unfollow()", followerDao.exists(followerId, followeeId));
    }

    @Test
    public void testUnfollow_whenRelationDoesNotExist_shouldNotThrow() {
        // given
        Integer followerId = follower.getId();
        Integer followeeId = followee.getId();

        // when + then
        followerDao.unfollow(followerId, followeeId);

        assertFalse("Relacja nie powinna istnieć", followerDao.exists(followerId, followeeId));
    }
}