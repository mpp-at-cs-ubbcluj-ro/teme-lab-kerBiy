package cs.ubb.model;

import java.util.Date;

public class Show extends Entity<Long> {
    private String artist;
    private Date date;
    private String location;
    private int totalSeats;
    private int soldSeats;

    public Show(String artist, Date date, String location, int totalSeats) {
        this.artist = artist;
        this.date = date;
        this.location = location;
        this.totalSeats = totalSeats;
        this.soldSeats = 0;
    }

}
