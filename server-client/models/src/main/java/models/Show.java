package models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "shows")
public class Show implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    private Long id;

    private String artist;

    @Column(name = "date", columnDefinition = "DATE")
    private Date date;

    private String location;

    private Integer totalSeats;
    private Integer soldSeats;

    public Show() {
    }

    public Show(String artist, Date date, String location, Integer totalSeats, Integer soldSeats) {
        this.artist = artist;
        this.date = date;
        this.location = location;
        this.totalSeats = totalSeats;
        this.soldSeats = soldSeats;
    }

    @Override
    public void setId(Long newId) {
        this.id = newId;
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
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", totalSeats=" + totalSeats +
                ", soldSeats=" + soldSeats +
                '}';
    }
}