package pl.atins.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import pl.atins.dao.UzytkownikDao;
import pl.atins.model.Uzytkownik;

@Transactional
public class UzytkownikDaoImpl implements UzytkownikDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Uzytkownik findByLogin(String login) {
        List<Uzytkownik> res = em.createQuery(
            "SELECT u FROM Uzytkownik u WHERE u.login = :login",
            Uzytkownik.class
        )
        .setParameter("login", login)
        .getResultList();

        return res.isEmpty() ? null : res.get(0);
    }


    @Override
    public void save(Uzytkownik uzytkownik) {
        em.persist(uzytkownik);
    }
}
