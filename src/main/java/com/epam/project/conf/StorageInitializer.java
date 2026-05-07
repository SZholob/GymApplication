package com.epam.project.conf;

import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.model.TrainingType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StorageInitializer {

    private final TrainingTypeDao trainingTypeDao;

    @PostConstruct
    @Transactional
    public void initDb() {

        if (trainingTypeDao.findByTypeName("FITNESS").isEmpty()) {
            trainingTypeDao.save(new TrainingType(null, "FITNESS"));
            trainingTypeDao.save(new TrainingType(null, "YOGA"));
            trainingTypeDao.save(new TrainingType(null, "ZUMBA"));
            trainingTypeDao.save(new TrainingType(null, "STRETCHING"));
        }
    }
}