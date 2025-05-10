package network.dto;

public class ShowDTO {
    public Long id;
    public String artist;
    public String date; // ISO string format pentru JSON
    public String location;
    public Integer totalSeats;
    public Integer soldSeats;

    public ShowDTO() {
    }

    public ShowDTO(Long id, String artist, String date, String location, Integer totalSeats, Integer soldSeats) {
        this.id = id;
        this.artist = artist;
        this.date = date;
        this.location = location;
        this.totalSeats = totalSeats;
        this.soldSeats = soldSeats;
    }
}