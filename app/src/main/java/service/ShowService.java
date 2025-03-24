package service;

import model.Show;
import repository.ShowRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowService {
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public List<Show> getAllShows() {
        List<Show> list = new ArrayList<>();
        showRepository.findAll().forEach(list::add);
        return list;
    }

    public List<Show> searchByDate(LocalDate date) {
        return getAllShows().stream()
                .filter(show -> convertToLocalDate(show.getDate()).equals(date))
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void updateShow(Show show) {
        showRepository.update(show);
    }
}