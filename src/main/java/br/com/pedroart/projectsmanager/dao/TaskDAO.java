package br.com.pedroart.projectsmanager.dao;

import br.com.pedroart.projectsmanager.model.Task;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public void save(Task task) {
        String sql = "INSERT INTO tasks (title, description, start_date, end_date, priority, status, id_project) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(task.getEndDate()));
            pstmt.setInt(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getIdProject());

            pstmt.executeUpdate();
            System.out.println("Tarefa salva com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar a tarefa: " + e.getMessage());
            e.printStackTrace();
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
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar a tarefa: " + e.getMessage());
            e.printStackTrace();
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
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar as tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        return tasks;
    }

    public void update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, start_date = ?, end_date = ?, priority = ?, status = ?, id_project = ? WHERE id_task = ?";

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

            pstmt.executeUpdate();
            System.out.println("Tarefa atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id_task = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Tarefa deletada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar a tarefa: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error searching for tasks by project and status: " + e.getMessage());
            e.printStackTrace();
        }
        return tasks;
    }
}