package pl.atins.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import pl.atins.dao.FollowerDao;
import pl.atins.model.Follower;

import java.time.LocalDateTime;

@Transactional
public class FollowerDaoImpl implements FollowerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void follow(Integer userId, Integer followsUserId) {
        if (exists(userId, followsUserId)) return;

        Follower f = new Follower();
        f.setUserId(userId);
        f.setFollowsUserId(followsUserId);
        f.setCreatedAt(LocalDateTime.now());

        em.persist(f);
    }

    @Override
    public void unfollow(Integer userId, Integer followsUserId) {
        TypedQuery<Follower> q = em.createQuery(
            "SELECT f FROM Follower f WHERE f.userId = :u AND f.followsUserId = :f",
            Follower.class
        );
        q.setParameter("u", userId);
        q.setParameter("f", followsUserId);

        q.getResultList().forEach(em::remove);
    }

    @Override
    public boolean exists(Integer userId, Integer followsUserId) {
        Long count = em.createQuery(
            "SELECT COUNT(f) FROM Follower f WHERE f.userId = :u AND f.followsUserId = :f",
            Long.class
        )
        .setParameter("u", userId)
        .setParameter("f", followsUserId)
        .getSingleResult();

        return count > 0;
    }
}
