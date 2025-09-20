package br.com.pedroart.projectsmanager.service;

import br.com.pedroart.projectsmanager.dao.ProjectDAO;
import br.com.pedroart.projectsmanager.model.Project;
import br.com.pedroart.projectsmanager.dao.MemberDAO;
import br.com.pedroart.projectsmanager.dao.ProjectMemberDAO;
import br.com.pedroart.projectsmanager.model.Member;
import br.com.pedroart.projectsmanager.dao.TaskDAO;
import br.com.pedroart.projectsmanager.model.Task;
import java.util.List;

public class ManagerService {
  private ProjectDAO projectDAO = new ProjectDAO();

  public void createNewProject(Project projeto) throws Exception {
      if (projeto.getName() == null || projeto.getName().trim().isEmpty()) {
          throw new Exception("Project name is required.");
      }

      projeto.setStatus("Planned");

      projectDAO.create(projeto);
  }

    private MemberDAO memberDAO = new MemberDAO();
    private ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();

  public void addMemberInProject(int idProjeto, int idMembro) throws Exception {
    Project projeto = projectDAO.findById(idProjeto);
    Member membro = memberDAO.findById(idMembro);

    if (projeto == null) {
      throw new Exception("Project not found!");
    }

    if (membro == null) {
      throw new Exception("Member not found!");
    }

    if ("completed".equals(projeto.getStatus())) {
      throw new Exception("You cannot add members to a project that has already been completed.");
    }

    projectMemberDAO.addMemberToProject(idProjeto, idMembro);
  }

  private TaskDAO taskDAO = new TaskDAO();

  public void completeTask(int idTarefa) {
      Task tarefa = taskDAO.findById(idTarefa);
      tarefa.setStatus("Completed");
      taskDAO.update(tarefa);

      int idProjeto = tarefa.getIdProject();
      List<Task> tarefasRestantes = taskDAO.findTasksByProjectAndStatus(idProjeto, "Pending");

      if (tarefasRestantes.isEmpty()) {
          Project projeto = projectDAO.findById(idProjeto);
          projeto.setStatus("completed");
          projectDAO.update(projeto);
          System.out.println("Project " + projeto.getName() + " completed!");
      }
  }
}
