package controller;

import domain.Person;
import service.Service;

public interface Controller {
    void setService(Service service, Person user);
}
