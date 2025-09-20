package br.com.pedroart.projectsmanager;

import br.com.pedroart.projectsmanager.dao.ProjectDAO;
import br.com.pedroart.projectsmanager.model.Project;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        
        ProjectDAO projetoDAO = new ProjectDAO();
        Project newProject = new Project();
        newProject.setName("Desenvolvimento do App Mobile");
        newProject.setDescription("Criar o novo aplicativo de vendas da empresa.");
        newProject.setStartDate(LocalDate.now());
        newProject.setEndDate(LocalDate.of(2026, 3, 31));
        newProject.setStatus("Planejado");
        newProject.setBudget(50000.00);

        try {
            projetoDAO.create(newProject);
        } catch (Exception e) {
            System.err.println("Ocorreu um erro na aplicação: " + e.getMessage());
        }
    }
}