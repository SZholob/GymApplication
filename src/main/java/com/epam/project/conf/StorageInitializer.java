package com.epam.project.conf;

import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StorageInitializer {

    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    private Map<Long, Trainee> traineeStorage;

    private Map<Long, Trainer> trainerStorage;

    private Map<Long, Training> trainingStorage;

    private ResourceLoader resourceLoader;

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${storage.init.file.path}")
    private String initFilePath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource(initFilePath);
            if (resource.exists()) {
                Map<String, List<?>> data = objectMapper.readValue(resource.getInputStream(), Map.class);
                List<Trainee> trainees = objectMapper.convertValue(data.get("trainees"), objectMapper.getTypeFactory().constructCollectionType(List.class, Trainee.class));
                List<Trainer> trainers = objectMapper.convertValue(data.get("trainers"), objectMapper.getTypeFactory().constructCollectionType(List.class, Trainer.class));
                List<Training> trainings = objectMapper.convertValue(data.get("trainings"), objectMapper.getTypeFactory().constructCollectionType(List.class, Training.class));

                trainees.forEach(t -> traineeStorage.put(t.getId(), t));
                trainers.forEach(t -> trainerStorage.put(t.getId(), t));
                trainings.forEach(t -> trainingStorage.put(t.getId(), t));

                logger.info("Storage initialized with data from {}", initFilePath);
            } else {
                logger.warn("Init file {} not found", initFilePath);
            }
        } catch (IOException e) {
            logger.error("Error initializing storage", e);
        }
    }
    @PreDestroy
    public void destroy() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("trainees", traineeStorage.values());
            data.put("trainers", trainerStorage.values());
            data.put("trainings", trainingStorage.values());

            File file = new File(initFilePath);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            logger.info("Storage data successfully saved to {}", initFilePath);
        } catch (IOException e) {
            logger.error("Error saving storage data to file", e);
        }
    }
}
