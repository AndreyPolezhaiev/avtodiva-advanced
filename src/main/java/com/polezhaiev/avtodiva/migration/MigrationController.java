package com.polezhaiev.avtodiva.migration;

import com.polezhaiev.avtodiva.service.migration.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/migrate")
public class MigrationController {
    private final MigrationService migrationService;

    @Autowired
    public MigrationController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @PostMapping("/slotsToNewRules")
    public String migrate() {
        migrationService.migrateSlotsToNewRules();
        return "Migration finished!";
    }

    @PostMapping("/makeAllForOneHourEarlier")
    public String minusHour() {
        migrationService.makeAllForOneHourEarlier();
        return "Every slot is earlier for 1 hour after December now!";
    }

    @PostMapping("/removeFreeSlotsForTanyaAndFord")
    public String removeMistakeCreatedSlots() {
        // Remove free slots Tanya - Ford after 19 December 2025
        migrationService.removeFreeSlotsForTanya();
        return "Slots were removed successfully!";
    }

    @PostMapping("/removeAllFreeSlots")
    public String removeAllFreeSlots() {
        // Remove all free slots starting from 19 December 2025
        migrationService.removeAllFreeSlots();
        return "All free slots were removed successfully!";
    }

    @PostMapping("/removeAllFreeSlotsByInstructorName")
    public String findAllFreeSlotsByInstructorName() {
        // Remove all free slots starting from 19 December 2025
        migrationService.removeAllFreeSlotsByInstructorName();
        return "All free slots were removed successfully!";
    }
}
