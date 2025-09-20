package br.com.pedroart.projectsmanager.model;

import java.time.LocalDate;

public class Task {
  private int idTask;
  private String title;
  private int idProject;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
  private int priority;
  private Project project;
}
