package com.polezhaiev.avtodiva.ui.util;

import com.polezhaiev.avtodiva.service.car.CarService;
import com.polezhaiev.avtodiva.service.instructor.InstructorService;
import com.polezhaiev.avtodiva.service.schedule.ScheduleSlotService;
import com.polezhaiev.avtodiva.service.student.StudentService;
import com.polezhaiev.avtodiva.ui.panel.dialog.AddSingleSlotDialog;

import javax.swing.*;
import java.awt.*;

public class SingleSlotButton extends JButton {

    public SingleSlotButton(String text, JFrame owner,
                            ScheduleSlotService scheduleSlotService,
                            InstructorService instructorService,
                            CarService carService,
                            StudentService studentService) {
        super(text);

        this.setBackground(Color.decode("#bcdff7"));

        this.addActionListener(e -> {
            AddSingleSlotDialog dialog = new AddSingleSlotDialog(
                    owner,
                    scheduleSlotService,
                    instructorService,
                    carService,
                    studentService
            );
            dialog.setVisible(true);
        });
    }
}