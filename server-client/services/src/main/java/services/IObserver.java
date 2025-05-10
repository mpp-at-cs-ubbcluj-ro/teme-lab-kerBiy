package services;

import models.Show;

public interface IObserver {
    void showUpdated(Show show) throws ServiceException;
}