package model;

public interface Identifiable<Tid> {
    Tid getID();
    void setID(Tid id);

    void setId(Integer id);

    Integer getId();
}
