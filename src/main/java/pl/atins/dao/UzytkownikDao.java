package pl.atins.dao;

import pl.atins.model.Uzytkownik;

public interface UzytkownikDao {

    Uzytkownik findByLogin(String login);

    void save(Uzytkownik uzytkownik);
}
