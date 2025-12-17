package com.polezhaiev.avtodiva.service.window;

import com.polezhaiev.avtodiva.model.Window;

public interface WindowService {
    void bookWindow(Window window);
    void addFreeWindowsForEachInstructor(int days);
    void addFreeWindowsForCar(String carName, int days);
    void addFreeWindowsForInstructor(String instructorName, int days);
}
