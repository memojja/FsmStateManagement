package com.fsm.schedule;

import com.fsm.core.Fsm;
import com.fsm.service.FsmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class FsmDeleteScheduler {

    private final FsmService fsmService;

    public FsmDeleteScheduler(FsmService fsmService) {
        this.fsmService = fsmService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void deleteFsmAfterFiveMinuteIdle() {
        long currentMs = System.currentTimeMillis();
        List<String> toBeDeleted = new ArrayList<>();
        for (Map.Entry<String, Fsm> entry : fsmService.getFsmMap().entrySet()) {
            if (currentMs - entry.getValue().getLastUsed() > 1000 * 60 * 5) {
                toBeDeleted.add(entry.getKey());
            }
        }

        toBeDeleted.forEach(fsmService::removeInMemoryFsm);
    }
}
