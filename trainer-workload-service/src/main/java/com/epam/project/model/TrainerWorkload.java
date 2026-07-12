package com.epam.project.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "trainer_workloads")
@CompoundIndex(name = "first_last_name_idx", def = "{'firstName': 1, 'lastName': 1}")
public class TrainerWorkload {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String firstName;
    private String lastName;
    private Boolean isActive;

    private List<Year> years;
}