package com.example.datvexe.common;

import com.example.datvexe.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CommonAsyncService {

    @Autowired
    private EmailService emailService;

    @Autowired
    @Qualifier("threadPoolExecutor")
    private TaskExecutor taskExecutor;

    @Async
    public void sendEmail(String email, String msg, String subject, boolean html){
        Runnable task3 = () -> {
            try {
                emailService.sendEmail(email,msg,subject,html);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        };
        taskExecutor.execute(task3);
    }
}
