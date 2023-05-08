package com.example.servertest.main.crop.cron;

import com.example.servertest.main.crop.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Scheduler {

    private final CrawlingService crawlingService;

    @Scheduled(cron = "0 0 0 ? * TUE")
    public void saveNotice() {

        String idx = crawlingService.getUrl(crawlingService.getIndex());
        crawlingService.updateAllData(idx);
    }
}
