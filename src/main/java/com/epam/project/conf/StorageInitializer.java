package com.epam.project.conf;

import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.model.TrainingType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StorageInitializer implements CommandLineRunner {

    private final TrainingTypeDao trainingTypeDao;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<TrainingType> existingTypes = trainingTypeDao.findAll();
        if (existingTypes.isEmpty()) {
            trainingTypeDao.save(new TrainingType(null, "FITNESS"));
            trainingTypeDao.save(new TrainingType(null, "YOGA"));
            trainingTypeDao.save(new TrainingType(null, "ZUMBA"));
            trainingTypeDao.save(new TrainingType(null, "STRETCHING"));
            trainingTypeDao.save(new TrainingType(null, "RESISTANCE"));

            System.out.println("✅ Storage Initialized with default Training Types!");
        }
    }
}