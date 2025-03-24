package model;

import java.util.Date;

public class Show implements Identifiable<Long> {
    private Long id;
    private String artist;
    private Date date;
    private String location;
    private Integer totalSeats;
    private Integer soldSeats;


    public Show(String artist, Date date, String location, Integer totalSeats, Integer soldSeats) {
        this.artist = artist;
        this.date = date;
        this.location = location;
        this.totalSeats = totalSeats;
        this.soldSeats = soldSeats;
    }

    @Override
    public void setId(Long newId) {
        id = newId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getSoldSeats() {
        return soldSeats;
    }

    public void setSoldSeats(Integer soldSeats) {
        this.soldSeats = soldSeats;
    }

    @Override
    public String toString() {
        return "Show{" +
                "artist='" + artist + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", totalSeats=" + totalSeats +
                ", soldSeats=" + soldSeats +
                ", id=" + id +
                '}';
    }
}
