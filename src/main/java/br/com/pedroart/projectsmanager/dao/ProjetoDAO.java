package  br.com.pedroart.projectsmanager.dao;


import br.com.pedroart.projectsmanager.model.Project;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProjetoDAO {
    public void salvar(Project projeto) {
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
    public Project buscarPorId(int id) { /* ... código SQL para SELECT WHERE id ... */ return null; }
    public List<Project> listarTodos() { /* ... código SQL para SELECT ALL ... */ return new ArrayList<>(); }
    public void atualizar(Project projeto) { /* ... código SQL para UPDATE ... */ }
    public void deletar(int id) { /* ... código SQL para DELETE ... */ }
}
