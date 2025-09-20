package br.com.pedroart.projectsmanager.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
    private int idProject;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double budget;
    private List<Task> tasks;
    private List<Member> members;
}