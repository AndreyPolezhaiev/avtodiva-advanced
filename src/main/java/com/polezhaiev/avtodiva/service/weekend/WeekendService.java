package com.polezhaiev.avtodiva.service.weekend;

import com.polezhaiev.avtodiva.model.Weekend;
import java.util.List;

public interface WeekendService {
    void saveAllWeekends(List<Weekend> weekends);
    void deleteAllWeekends(List<Weekend> weekends);
    void save(Weekend weekend);
}
