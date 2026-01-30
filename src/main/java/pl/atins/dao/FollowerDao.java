package pl.atins.dao;

public interface FollowerDao {

    void follow(Integer userId, Integer followsUserId);

    void unfollow(Integer userId, Integer followsUserId);

    boolean exists(Integer userId, Integer followsUserId);
}
