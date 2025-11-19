package com.fieldservice.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private String address;
    private String priority;
    private Integer duration;
}
