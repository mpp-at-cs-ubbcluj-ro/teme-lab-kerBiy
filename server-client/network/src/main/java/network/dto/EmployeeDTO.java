package network.dto;

public class EmployeeDTO {
    public Long id;
    public String username;
    public String password;  // doar la request

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public EmployeeDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}