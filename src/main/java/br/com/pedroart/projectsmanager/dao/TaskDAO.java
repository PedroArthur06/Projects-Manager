package br.com.pedroart.projectsmanager.dao;

import br.com.pedroart.projectsmanager.model.Task;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.com.pedroart.projectsmanager.exception.DataAccessException;

public class TaskDAO {

    public void save(Task task) {
        String sql = "INSERT INTO tasks (title, description, start_date, end_date, priority, status, id_project, id_member) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(task.getEndDate()));
            pstmt.setInt(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getIdProject());
            pstmt.setInt(8, task.getIdMember());

            pstmt.executeUpdate();
            System.out.println("Task saved successfully!");

        } catch (SQLException e) {
            throw new DataAccessException("Error saving task: " + e.getMessage());
        }
    }

    public Task findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id_task = ?";
        Task task = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                task = new Task();
                task.setIdTask(rs.getInt("id_task"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDate(rs.getDate("start_date").toLocalDate());
                task.setEndDate(rs.getDate("end_date").toLocalDate());
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setIdProject(rs.getInt("id_project"));
                task.setIdMember(rs.getInt("id_member"));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching task: " + e.getMessage());
        }
        return task;
    }

    public List<Task> listAll() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task();
                task.setIdTask(rs.getInt("id_task"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDate(rs.getDate("start_date").toLocalDate());
                task.setEndDate(rs.getDate("end_date").toLocalDate());
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setIdProject(rs.getInt("id_project"));
                task.setIdMember(rs.getInt("id_member"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, start_date = ?, end_date = ?, priority = ?, status = ?, id_project = ?, id_member = ? WHERE id_task = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(task.getEndDate()));
            pstmt.setInt(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getIdProject());
            pstmt.setInt(8, task.getIdTask());
            pstmt.setInt(8, task.getIdMember());

            pstmt.executeUpdate();
            System.out.println("Task updated successfully!");

        } catch (SQLException e) {
            throw new DataAccessException("Error updating task: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id_task = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Task deleted successfully!");

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting task: " + e.getMessage());
        }
    }

    public List<Task> findTasksByProjectAndStatus(int projectId, String status) {
        String sql = "SELECT * FROM tasks WHERE id_project = ? AND status = ?";
        List<Task> tasks = new ArrayList<>();
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, projectId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Task task = new Task();
                task.setIdTask(rs.getInt("id_task"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDate(rs.getDate("start_date").toLocalDate());
                task.setEndDate(rs.getDate("end_date").toLocalDate());
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setIdProject(rs.getInt("id_project"));
                tasks.add(task);
            }
    
        } catch (SQLException e) {
            throw new DataAccessException("Error searching for tasks by project and status: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> findTasksByProjectId(int projectId) {
        String sql = "SELECT * FROM tasks WHERE id_project = ?";
        List<Task> tasks = new ArrayList<>();
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Task task = new Task();
                task.setIdTask(rs.getInt("id_task"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStartDate(rs.getDate("start_date").toLocalDate());
                task.setEndDate(rs.getDate("end_date").toLocalDate());
                task.setPriority(rs.getInt("priority"));
                task.setStatus(rs.getString("status"));
                task.setIdProject(rs.getInt("id_project"));
                task.setIdMember(rs.getInt("id_member"));
                tasks.add(task);
            }
    
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar tarefas por projeto: " + e.getMessage());
        }
        return tasks;
    }
}