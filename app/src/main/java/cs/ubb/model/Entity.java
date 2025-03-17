package cs.ubb.model;

public abstract class Entity<ID> {
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID newId) {
        this.id = newId;
    }
}
