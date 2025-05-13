package model;

public class Ticket implements Identifiable<Long> {
    private Long id;
    private String name;
    private Integer numberOfSeats;
    private Long showId;

    public Ticket(String name, Integer numberOfSeats, Long showId) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.showId = showId;
    }

    @Override
    public void setId(Long newId) {
        id = newId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    @Override
    public String toString() {
        return "models.Ticket{" +
                "name='" + name + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", showId=" + showId +
                ", id=" + id +
                '}';
    }
}