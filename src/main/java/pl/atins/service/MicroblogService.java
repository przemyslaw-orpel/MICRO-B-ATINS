package pl.atins.service;

import java.util.List;

import pl.atins.model.Uzytkownik;
import pl.atins.model.Wpis;

public interface MicroblogService {

    // Wpisy / timeline
    List<Wpis> getTimeline(String login);
    List<Wpis> getFullTimeline(String login);
    List<Wpis> getPublicTimeline();
    void addWpis(String login, String tresc);

    // Użytkownicy
    Uzytkownik getUzytkownikByLogin(String login);
    void registerUzytkownik(String login, String haslo, String imie, String nazwisko);

    // Follow
    void follow(String followerLogin, String followedLogin);
    void unfollow(String followerLogin, String followedLogin);
    boolean isFollowing(String followerLogin, String followedLogin);
}
