package pl.atins.dao;

import java.util.List;
import pl.atins.model.Wpis;

public interface WpisDao {

    List<Wpis> getUserTimeline(Integer userId);

    List<Wpis> getFullTimeline(Integer userId);

    List<Wpis> getPublicTimeline();

    void save(Wpis wpis);
}
