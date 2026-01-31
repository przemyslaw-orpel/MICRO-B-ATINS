package pl.atins.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import pl.atins.dao.FollowerDao;
import pl.atins.dao.UzytkownikDao;
import pl.atins.dao.WpisDao;
import pl.atins.model.Uzytkownik;
import pl.atins.model.Wpis;
import pl.atins.service.MicroblogService;

@Transactional
public class MicroblogServiceImpl implements MicroblogService {

    private final WpisDao wpisDao;
    private final UzytkownikDao uzytkownikDao;
    private final FollowerDao followerDao;

    public MicroblogServiceImpl(WpisDao wpisDao, UzytkownikDao uzytkownikDao, FollowerDao followerDao) {
        this.wpisDao = wpisDao;
        this.uzytkownikDao = uzytkownikDao;
        this.followerDao = followerDao;
    }

    @Override
    public List<Wpis> getTimeline(String login) {
        Uzytkownik user = uzytkownikDao.findByLogin(login);
        return wpisDao.getUserTimeline(user.getId());
    }

    @Override
    public List<Wpis> getFullTimeline(String login) {
        Uzytkownik user = uzytkownikDao.findByLogin(login);
        return wpisDao.getFullTimeline(user.getId());
    }

    @Override
    public List<Wpis> getPublicTimeline() {
        return wpisDao.getPublicTimeline();
    }

    @Override
    public void addWpis(String login, String tresc) {
        Uzytkownik user = uzytkownikDao.findByLogin(login);

        Wpis wpis = new Wpis();
        wpis.setUserId(user.getId());
        wpis.setTresc(tresc);
        wpis.setCreatedAt(LocalDateTime.now());

        wpisDao.save(wpis);
    }

    @Override
    public Uzytkownik getUzytkownikByLogin(String login) {
        return uzytkownikDao.findByLogin(login);
    }

    @Override
    public void registerUzytkownik(String login, String haslo, String imie, String nazwisko) {
        // W Twoim DAO nie ma rejestracji - robimy minimalnie: zapis użytkownika
        Uzytkownik u = new Uzytkownik();
        u.setLogin(login);
        u.setEmail(login + "@test.pl");
        u.setCreatedAt(LocalDateTime.now());
        uzytkownikDao.save(u);
    }

    @Override
    public void follow(String followerLogin, String followedLogin) {
        Uzytkownik user = uzytkownikDao.findByLogin(followerLogin);
        Uzytkownik follows = uzytkownikDao.findByLogin(followedLogin);
        followerDao.follow(user.getId(), follows.getId());
    }

    @Override
    public void unfollow(String followerLogin, String followedLogin) {
        Uzytkownik user = uzytkownikDao.findByLogin(followerLogin);
        Uzytkownik follows = uzytkownikDao.findByLogin(followedLogin);
        followerDao.unfollow(user.getId(), follows.getId());
    }

    @Override
    public boolean isFollowing(String followerLogin, String followedLogin) {
        Uzytkownik user = uzytkownikDao.findByLogin(followerLogin);
        Uzytkownik follows = uzytkownikDao.findByLogin(followedLogin);
        return followerDao.exists(user.getId(), follows.getId());
    }
}
