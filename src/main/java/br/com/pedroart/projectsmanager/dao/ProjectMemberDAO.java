package br.com.pedroart.projectsmanager.dao;

import br.com.pedroart.projectsmanager.model.Member;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.pedroart.projectsmanager.exception.DataAccessException;

public class ProjectMemberDAO {

    public void addMemberToProject(int projectId, int memberId) {
        String sql = "INSERT INTO project_members (id_project, id_member) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();

            System.out.println("Membro " + memberId + " associado ao projeto " + projectId + " com sucesso!");

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao associar membro ao projeto: " + e.getMessage());
        }
    }

    public void removeMemberFromProject(int projectId, int memberId) {
        String sql = "DELETE FROM project_members WHERE id_project = ? AND id_member = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();

            System.out.println("Membro " + memberId + " removido do projeto " + projectId + " com sucesso!");

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao remover membro do projeto: " + e.getMessage());
        }
    }

    public List<Member> findMembersByProjectId(int projectId) {
        String sql = "SELECT m.* FROM members m " +
                     "JOIN project_members pm ON m.id_member = pm.id_member " +
                     "WHERE pm.id_project = ?";

        List<Member> members = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setIdMember(rs.getInt("id_member"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPosition(rs.getString("position"));
                member.setDepartment(rs.getString("department"));
                member.setDateJoined(rs.getDate("date_joined").toLocalDate());
                members.add(member);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar membros do projeto: " + e.getMessage());
        }
        return members;
    }
}
