import model.Show;
import repository.ShowRepository;
import utils.Jdbc;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        Jdbc jdbc = new Jdbc(props);

        ShowRepository showRepository = new ShowRepository(jdbc);

        System.out.println("Adding a new show...");
        Show show = new Show("The Rolling Stones", new Date(), "Madison Square Garden", 5000);
        showRepository.save(show);
        System.out.println("Shows in DB:");
        showRepository.findAll().forEach(System.out::println);
    }
}