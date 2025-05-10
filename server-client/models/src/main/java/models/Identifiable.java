package models;

public interface Identifiable<ID> {
    void setId(ID id);

    ID getId();
}
