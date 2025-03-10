package cs.ubb.repository;

import cs.ubb.model.Show;

import java.util.List;
import java.util.Optional;
import java.util.Date;

public interface IShowRepository {
    List<Show> findByDate(Date date);
    Optional<Show> findById(Long id);
    void save(Show show);
    void delete(Long id);
}
