package  br.com.pedroart.projectsmanager.dao;


import br.com.pedroart.projectsmanager.model.project;
import br.com.pedroart.projectsmanager.utils.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProjetoDAO {
    public void salvar(project projeto) { /* ... código SQL para INSERT ... */ }
    public project buscarPorId(int id) { /* ... código SQL para SELECT WHERE id ... */ return null; }
    public List<project> listarTodos() { /* ... código SQL para SELECT ALL ... */ return new ArrayList<>(); }
    public void atualizar(project projeto) { /* ... código SQL para UPDATE ... */ }
    public void deletar(int id) { /* ... código SQL para DELETE ... */ }
}
