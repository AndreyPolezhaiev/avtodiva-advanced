package com.polezhaiev.avtodiva.service.car;


import com.polezhaiev.avtodiva.model.Car;

public interface CarService {
    void saveCar(Car car);
    void deleteByName(String name);
    String[] getCarsNames();
    Car findByName(String name);
}
