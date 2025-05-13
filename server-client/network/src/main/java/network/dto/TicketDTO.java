package network.dto;

public class TicketDTO {
    public Long id;
    public String name;
    public Integer numberOfSeats;
    public Long showId;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String name, Integer numberOfSeats, Long showId) {
        this.id = id;
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.showId = showId;
    }
}