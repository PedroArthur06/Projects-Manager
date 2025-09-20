package  br.com.pedroart.projectsmanager.dao;


import br.com.pedroart.projectsmanager.model.Project;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProjectDAO {
    public void save(Project projeto) {
        String sql = "INSERT INTO projects (name, description, start_date, end_date, status, budget) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getName());
            pstmt.setString(2, projeto.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(projeto.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(projeto.getEndDate()));
            pstmt.setString(5, projeto.getStatus());
            pstmt.setDouble(6, projeto.getBudget());

            // Executa o comando SQL
            pstmt.executeUpdate();
            
            System.out.println("Projeto salvo com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Project findByID(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        Project project = null;
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date").toLocalDate());
                project.setStatus(rs.getString("status"));
                project.setBudget(rs.getDouble("budget"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar o projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Project> list() { /* ... código SQL para SELECT ALL ... */ return new ArrayList<>(); }
    public void update(Project projeto) { /* ... código SQL para UPDATE ... */ }
    public void delete(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Projeto deletado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar o projeto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
