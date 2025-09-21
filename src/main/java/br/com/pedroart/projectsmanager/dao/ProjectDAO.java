package br.com.pedroart.projectsmanager.dao;

import br.com.pedroart.projectsmanager.model.Project;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import br.com.pedroart.projectsmanager.exception.DataAccessException;

public class ProjectDAO {

    public int create(Project projeto) {
        String sql = "INSERT INTO projects (name, description, start_date, end_date, status, budget) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = 0;
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setString(1, projeto.getName());
            pstmt.setString(2, projeto.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(projeto.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(projeto.getEndDate()));
            pstmt.setString(5, projeto.getStatus());
            pstmt.setDouble(6, projeto.getBudget());
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
            System.out.println("Projeto salvo com sucesso no banco de dados!");
            return generatedId;
    
        } catch (SQLException e) {
            throw new DataAccessException("erro saved project: " + e.getMessage());
        }
    }
    
    public Project findById(int id) {
        String sql = "SELECT * FROM projects WHERE id_project = ?"; 
        Project project = null;
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setIdProject(rs.getInt("id_project")); 
                project.setName(rs.getString("name"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date").toLocalDate());
                project.setStatus(rs.getString("status"));
                project.setBudget(rs.getDouble("budget"));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar o projeto: " + e.getMessage());
        }
        return project;
    }

    public List<Project> list() {
        String sql = "SELECT * FROM projects";
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Project project = new Project();
                project.setIdProject(rs.getInt("id_project"));
                project.setName(rs.getString("name"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date").toLocalDate());
                project.setStatus(rs.getString("status"));
                project.setBudget(rs.getDouble("budget"));
                projects.add(project);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar os projetos: " + e.getMessage());
        }
        return projects;
    }
    
    public void update(Project projeto) {
        String sql = "UPDATE projects SET name = ?, description = ?, start_date = ?, end_date = ?, status = ?, budget = ? WHERE id_project = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getName());
            pstmt.setString(2, projeto.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(projeto.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(projeto.getEndDate()));
            pstmt.setString(5, projeto.getStatus());
            pstmt.setDouble(6, projeto.getBudget());
            pstmt.setInt(7, projeto.getIdProject()); 

            pstmt.executeUpdate();
            System.out.println("Projeto atualizado com sucesso!");

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao atualizar o projeto: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM projects WHERE id_project = ?"; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Projeto deletado com sucesso!");

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao deletar o projeto: " + e.getMessage());
        }
    }
}