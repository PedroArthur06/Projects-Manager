package br.com.pedroart.projectsmanager.model;

import java.time.LocalDate;
import java.util.List;

public class Member {
  private int idMember;
  private String name;
  private String email;
  private String position;
  private String department;
  private LocalDate dateJoined;
  private List<Project> projects;
}
