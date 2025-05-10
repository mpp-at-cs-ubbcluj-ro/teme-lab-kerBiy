package client.controller;

import services.IServices;

import java.util.Optional;

public abstract class GenericController {
    protected IServices service;

    /**
     * Setează serviciul principal (proxy-ul client)
     */
    public void setService(IServices service) {
        this.service = service;
    }

    /**
     * Poate fi suprascris pentru a primi parametri (ex: Employee după login)
     */
    public void setSomething(Optional<Object> object) {
        // Default: nimic
    }
}