package model;

public class Ticket extends Entity<Long> {
    private String name;
    private Integer numberOfSeats;
    private Show show;

    public Ticket(String name, Integer numberOfSeats, Show show) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.show = show;
    }
}
