package br.com.pedroart.projectsmanager.dao;

import br.com.pedroart.projectsmanager.model.Member;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import br.com.pedroart.projectsmanager.exception.DataAccessException;

public class MemberDAO {

    public int create(Member member) {
      String sql = "INSERT INTO members (name, email, position, department, date_joined) VALUES (?, ?, ?, ?, ?)";
      int generatedId = 0;
      try (Connection conn = DatabaseConnector.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

          pstmt.setString(1, member.getName());
          pstmt.setString(2, member.getEmail());
          pstmt.setString(3, member.getPosition());
          pstmt.setString(4, member.getDepartment());
          pstmt.setDate(5, java.sql.Date.valueOf(member.getDateJoined()));

          int affectedRows = pstmt.executeUpdate();
          if (affectedRows > 0) {
              try (ResultSet rs = pstmt.getGeneratedKeys()) {
                  if (rs.next()) {
                      generatedId = rs.getInt(1);
                  }
              }
          }
          System.out.println("Membro salvo com sucesso!");
          return generatedId;

      } catch (SQLException e) {
          throw new DataAccessException("Erro ao salvar o membro: " + e.getMessage());
      }
    }

    public Member findById(int id) {
      String sql = "SELECT * FROM members WHERE id_member = ?";
      Member member = null;
        try (Connection conn = DatabaseConnector.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

          pstmt.setInt(1, id);
          ResultSet rs = pstmt.executeQuery();

          if (rs.next()) {
            member = new Member();
            member.setIdMember(rs.getInt("id_member"));
            member.setName(rs.getString("name"));
            member.setEmail(rs.getString("email"));
            member.setPosition(rs.getString("position"));
            member.setDepartment(rs.getString("department"));
            member.setDateJoined(rs.getDate("date_joined").toLocalDate());
          }

        }catch (SQLException e) {
          throw new DataAccessException("Erro ao buscar o membro: " + e.getMessage());
        }
        return member;
    }

    public List<Member> listAll() {
      String sql = "SELECT * FROM members";
      List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {

          while (rs.next()) {
            Member member = new Member();
            member.setIdMember(rs.getInt("id_member"));
            member.setName(rs.getString("name"));
            member.setEmail(rs.getString("email"));
            member.setPosition(rs.getString("position"));
            member.setDepartment(rs.getString("department"));
            member.setDateJoined(rs.getDate("date_joined").toLocalDate());
            members.add(member); // Adiciona o membro à lista
          }
        } catch (SQLException e) {
          throw new DataAccessException("Erro ao listar os membros: " + e.getMessage());
        }
        return members;
    }

    public void update(Member member) {
      String sql = "UPDATE members SET name = ?, email = ?, position = ?, department = ?, date_joined = ? WHERE id_member = ?";

        try (Connection conn = DatabaseConnector.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

          pstmt.setString(1, member.getName());
          pstmt.setString(2, member.getEmail());
          pstmt.setString(3, member.getPosition());
          pstmt.setString(4, member.getDepartment());
          pstmt.setDate(5, java.sql.Date.valueOf(member.getDateJoined()));
          pstmt.setInt(6, member.getIdMember());
          pstmt.executeUpdate();
          System.out.println("Membro atualizado com sucesso!");

        } catch (SQLException e) {
          System.err.println("Erro ao atualizar o membro: " + e.getMessage());
          e.printStackTrace();
        }
    }

    public void delete(int id) {
      String sql = "DELETE FROM members WHERE id_member = ?";

        try (Connection conn = DatabaseConnector.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

          pstmt.setInt(1, id);
          pstmt.executeUpdate();
          System.out.println("Membro deletado com sucesso!");

        } catch (SQLException e) {
          throw new DataAccessException("Erro ao deletar o membro: " + e.getMessage());
        }
    }
}