package br.com.pedroart.projectsmanager.view;

import br.com.pedroart.projectsmanager.dao.MemberDAO;
import br.com.pedroart.projectsmanager.dao.ProjectDAO;
import br.com.pedroart.projectsmanager.dao.ProjectMemberDAO;
import br.com.pedroart.projectsmanager.dao.TaskDAO;
import br.com.pedroart.projectsmanager.model.Member;
import br.com.pedroart.projectsmanager.model.Project;
import br.com.pedroart.projectsmanager.model.Task;
import static br.com.pedroart.projectsmanager.view.UIFactory.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ProjectManagerGUI extends JFrame {

    // DAOs
    private static final ProjectDAO projectDAO = new ProjectDAO();
    private static final MemberDAO memberDAO = new MemberDAO();
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final ProjectMemberDAO projectMemberDAO = new ProjectMemberDAO();

    // Componentes principais
    private JTabbedPane tabbedPane;
    private JTable projectsTable;
    private DefaultTableModel projectsTableModel;
    private JLabel projectInfoLabel;
    private JTable membersTable;
    private DefaultTableModel membersTableModel;
    private JTable tasksTable;
    private DefaultTableModel tasksTableModel;
    private Project currentProject;

    public ProjectManagerGUI() {
        initializeGUI();
        refreshProjectsList();
    }

    private void initializeGUI() {
        setTitle("🚀 Gerenciador de Projetos - Pedro Art");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainTabbedPane(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    // Painéis principais
    private JTabbedPane createMainTabbedPane() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.addTab("📋 Projetos", createProjectsPanel());
        tabbedPane.addTab("🔍 Gerenciar Projeto", createProjectDetailsPanel());
        tabbedPane.setEnabledAt(1, false);
        return tabbedPane;
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel sectionTitle = new JLabel("📋 Lista de Projetos");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(TEXT_COLOR);
        panel.add(sectionTitle, BorderLayout.NORTH);

        projectsTableModel = createNonEditableTableModel(new String[]{"ID", "Nome", "Status", "Início", "Fim", "Orçamento"});
        projectsTable = new JTable(projectsTableModel);
        configureTableStyle(projectsTable);
        panel.add(new JScrollPane(projectsTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setOpaque(false);
        JButton refreshBtn = createStyledButton("🔄 Atualizar", SECONDARY_COLOR);
        JButton viewDetailsBtn = createStyledButton("👁️ Gerenciar", PRIMARY_COLOR);
        JButton editBtn = createStyledButton("✏️ Editar", EDIT_COLOR);
        JButton deleteBtn = createStyledButton("🗑️ Excluir", DELETE_COLOR);

        refreshBtn.addActionListener(e -> refreshProjectsList());
        viewDetailsBtn.addActionListener(e -> viewSelectedProject());
        editBtn.addActionListener(e -> editSelectedProject());
        deleteBtn.addActionListener(e -> deleteSelectedProject());

        buttonsPanel.add(refreshBtn);
        buttonsPanel.add(viewDetailsBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProjectDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            createMembersManagementPanel(), createTaskManagementPanel());
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);

        panel.add(createInfoPanel(), BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMembersManagementPanel() {
        JPanel membersPanel = new JPanel(new BorderLayout(10, 10));
        membersPanel.setOpaque(false);
        membersPanel.setBorder(new TitledBorder(null, "👥 Membros da Equipe",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), TEXT_COLOR));

        membersTableModel = createNonEditableTableModel(new String[]{"ID", "Nome", "Cargo", "Email", "Depto"});
        membersTable = new JTable(membersTableModel);
        configureTableStyle(membersTable);
        membersPanel.add(new JScrollPane(membersTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        JButton editMemberBtn = createStyledButton("✏️ Editar Membro", EDIT_COLOR);
        JButton removeMemberBtn = createStyledButton("🗑️ Remover do Projeto", DELETE_COLOR);

        editMemberBtn.addActionListener(e -> editSelectedMember());
        removeMemberBtn.addActionListener(e -> removeSelectedMemberFromProject());

        buttonsPanel.add(editMemberBtn);
        buttonsPanel.add(removeMemberBtn);
        membersPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return membersPanel;
    }

    private JPanel createTaskManagementPanel() {
        JPanel tasksPanel = new JPanel(new BorderLayout(10, 10));
        tasksPanel.setOpaque(false);
        tasksPanel.setBorder(new TitledBorder(null, "📋 Tarefas do Projeto",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), TEXT_COLOR));

        tasksTableModel = createNonEditableTableModel(new String[]{"ID", "Título", "Status", "Responsável"});
        tasksTable = new JTable(tasksTableModel);
        configureTableStyle(tasksTable);
        tasksPanel.add(new JScrollPane(tasksTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        JButton addTaskBtn = createStyledButton("➕ Nova Tarefa", ACCENT_COLOR);
        JButton editTaskBtn = createStyledButton("✏️ Editar Tarefa", EDIT_COLOR);
        JButton deleteTaskBtn = createStyledButton("🗑️ Excluir Tarefa", DELETE_COLOR);

        addTaskBtn.addActionListener(e -> openAddTaskDialogForCurrentProject());
        editTaskBtn.addActionListener(e -> editSelectedTask());
        deleteTaskBtn.addActionListener(e -> deleteSelectedTask());

        buttonsPanel.add(addTaskBtn);
        buttonsPanel.add(editTaskBtn);
        buttonsPanel.add(deleteTaskBtn);
        tasksPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return tasksPanel;
    }

    // Eventos CRUD
    private void deleteSelectedProject() {
        int row = projectsTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione um projeto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) projectsTable.getValueAt(row, 0);
        String name = (String) projectsTable.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir o projeto '" + name + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            projectDAO.delete(id);
            refreshProjectsList();
        }
    }

    private void editSelectedProject() {
        int row = projectsTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione um projeto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) projectsTable.getValueAt(row, 0);
        openEditProjectDialog(projectDAO.findById(id));
    }

    private void editSelectedMember() {
        int row = membersTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione um membro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) membersTable.getValueAt(row, 0);
        openEditMemberDialog(memberDAO.findById(id));
    }

    private void removeSelectedMemberFromProject() {
        int row = membersTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione um membro para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) membersTable.getValueAt(row, 0);
        String name = (String) membersTable.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Remover '" + name + "' do projeto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            projectMemberDAO.removeMemberFromProject(currentProject.getIdProject(), id);
            loadProjectMembers(currentProject.getIdProject());
        }
    }

    private void openAddTaskDialogForCurrentProject() {
        if (currentProject == null) return;
        List<Member> members = projectMemberDAO.findMembersByProjectId(currentProject.getIdProject());
        if (members.isEmpty()) {
            showStyledMessage("Adicione membros antes de criar tarefas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        openAddTasksDialog(currentProject.getIdProject(), members);
    }

    private void editSelectedTask() {
        int row = tasksTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione uma tarefa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) tasksTable.getValueAt(row, 0);
        openEditTaskDialog(taskDAO.findById(id));
    }

    private void deleteSelectedTask() {
        int row = tasksTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione uma tarefa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) tasksTable.getValueAt(row, 0);
        String title = (String) tasksTable.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir a tarefa '" + title + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            taskDAO.delete(id);
            loadProjectTasks(currentProject.getIdProject());
        }
    }

    // Painéis de atualização e carregamento de dados
    private void refreshProjectsList() {
        projectsTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        projectDAO.list().forEach(p -> projectsTableModel.addRow(new Object[] {
            p.getIdProject(), p.getName(), p.getStatus(),
            p.getStartDate().format(formatter), p.getEndDate().format(formatter),
            String.format("R$ %.2f", p.getBudget())
        }));
    }

    private void viewSelectedProject() {
        int row = projectsTable.getSelectedRow();
        if (row < 0) {
            showStyledMessage("Selecione um projeto para gerenciar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) projectsTable.getValueAt(row, 0);
        currentProject = projectDAO.findById(id);

        if (currentProject != null) {
            displayProjectDetails(currentProject);
            loadProjectMembers(id);
            loadProjectTasks(id);
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);
        }
    }

    private void loadProjectMembers(int projectId) {
        membersTableModel.setRowCount(0);
        projectMemberDAO.findMembersByProjectId(projectId).forEach(m -> membersTableModel.addRow(new Object[] {
            m.getIdMember(), m.getName(), m.getPosition(), m.getEmail(), m.getDepartment()
        }));
    }

    private void loadProjectTasks(int projectId) {
        tasksTableModel.setRowCount(0);
        taskDAO.findTasksByProjectId(projectId).forEach(t -> {
            Member member = memberDAO.findById(t.getIdMember());
            tasksTableModel.addRow(new Object[] {
                t.getIdTask(), t.getTitle(), t.getStatus(), (member != null) ? member.getName() : "N/A"
            });
        });
    }

    // Painéis de criação de edição
    private void openNewProjectDialog() {
        JDialog dialog = createBaseDialog("➕ Novo Projeto", 600, 500);

        JTextField nameField = new JTextField(20);
        JTextArea descField = new JTextArea(3, 20);
        JTextField startDateField = new JTextField(20);
        JTextField endDateField = new JTextField(20);
        JTextField budgetField = new JTextField(20);

        JPanel formPanel = createFormPanel(
            new String[] {"Nome:", "Descrição:", "Início (YYYY-MM-DD):", "Fim (YYYY-MM-DD):", "Orçamento:"},
            new JComponent[] {nameField, new JScrollPane(descField), startDateField, endDateField, budgetField}
        );

        JButton cancelBtn = createStyledButton("Cancelar", DELETE_COLOR);
        JButton saveBtn = createStyledButton("Salvar", ACCENT_COLOR);
        cancelBtn.addActionListener(e -> dialog.dispose());
        saveBtn.addActionListener(e -> {
            if (saveNewProject(nameField.getText(), descField.getText(), startDateField.getText(), endDateField.getText(), budgetField.getText())) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(cancelBtn, saveBtn), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private boolean saveNewProject(String name, String desc, String start, String end, String budget) {
        try {
            Project project = new Project();
            project.setName(name);
            project.setDescription(desc);
            project.setStartDate(LocalDate.parse(start));
            project.setEndDate(LocalDate.parse(end));
            project.setBudget(Double.parseDouble(budget));
            project.setStatus("Planned");
            int projectId = projectDAO.create(project);
            refreshProjectsList();
            openAddMembersDialog(projectId);
            return true;
        } catch (Exception e) {
            showStyledMessage("Erro ao salvar projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void openAddMembersDialog(int projectId) {
        List<Member> addedMembers = new ArrayList<>();
        JDialog dialog = createBaseDialog("👥 Adicionar Membros", 500, 400);

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField positionField = new JTextField(20);
        JTextField departmentField = new JTextField(20);

        JPanel formPanel = createFormPanel(
            new String[] {"Nome:", "Email:", "Cargo:", "Departamento:"},
            new JComponent[] {nameField, emailField, positionField, departmentField}
        );

        JButton saveAndAddBtn = createStyledButton("Salvar e Adicionar Outro", PRIMARY_COLOR);
        JButton finishBtn = createStyledButton("Concluir", ACCENT_COLOR);

        saveAndAddBtn.addActionListener(e -> {
            Member member = saveNewMember(nameField, emailField, positionField, departmentField);
            if (member != null) {
                projectMemberDAO.addMemberToProject(projectId, member.getIdMember());
                addedMembers.add(member);
                nameField.setText(""); emailField.setText(""); positionField.setText(""); departmentField.setText("");
                nameField.requestFocus();
            }
        });

        finishBtn.addActionListener(e -> {
            if (!nameField.getText().trim().isEmpty()) {
                Member member = saveNewMember(nameField, emailField, positionField, departmentField);
                if (member != null) {
                    projectMemberDAO.addMemberToProject(projectId, member.getIdMember());
                    addedMembers.add(member);
                }
            }
            dialog.dispose();
            if (!addedMembers.isEmpty()) {
                openAddTasksDialog(projectId, addedMembers);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(saveAndAddBtn, finishBtn), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private Member saveNewMember(JTextField nameField, JTextField emailField, JTextField posField, JTextField deptField) {
        try {
            Member member = new Member();
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPosition(posField.getText());
            member.setDepartment(deptField.getText());
            member.setDateJoined(LocalDate.now());
            int memberId = memberDAO.create(member);
            member.setIdMember(memberId);
            return member;
        } catch (Exception ex) {
            showStyledMessage("Erro ao salvar membro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void openAddTasksDialog(int projectId, List<Member> members) {
        JDialog dialog = createBaseDialog("📋 Adicionar Tarefas", 600, 500);

        JTextField titleField = new JTextField(20);
        JTextArea descField = new JTextArea(3, 20);
        JTextField startDateField = new JTextField(20);
        JTextField endDateField = new JTextField(20);
        JComboBox<Member> memberComboBox = new JComboBox<>(new Vector<>(members));

        JPanel formPanel = createFormPanel(
            new String[] {"Título:", "Descrição:", "Início (YYYY-MM-DD):", "Fim (YYYY-MM-DD):", "Responsável:"},
            new JComponent[] {titleField, new JScrollPane(descField), startDateField, endDateField, memberComboBox}
        );

        JButton saveAndAddBtn = createStyledButton("Salvar e Add Outra", PRIMARY_COLOR);
        JButton finishBtn = createStyledButton("Concluir", ACCENT_COLOR);

        saveAndAddBtn.addActionListener(ev -> {
            if (saveNewTask(projectId, (Member) memberComboBox.getSelectedItem(), titleField.getText(), descField.getText(), startDateField.getText(), endDateField.getText())) {
                titleField.setText(""); descField.setText(""); startDateField.setText(""); endDateField.setText("");
            }
        });

        finishBtn.addActionListener(ev -> dialog.dispose());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(saveAndAddBtn, finishBtn), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private boolean saveNewTask(int pid, Member m, String t, String d, String s, String en) {
        try {
            Task task = new Task();
            task.setTitle(t);
            task.setDescription(d);
            task.setStartDate(LocalDate.parse(s));
            task.setEndDate(LocalDate.parse(en));
            task.setIdProject(pid);
            task.setIdMember(m.getIdMember());
            task.setStatus("Pendente");
            taskDAO.save(task);
            return true;
        } catch (Exception e) {
            showStyledMessage("Erro ao salvar tarefa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void openEditProjectDialog(Project project) {
        if (project == null) return;
        JDialog dialog = createBaseDialog("✏️ Editar Projeto - " + project.getName(), 600, 500);

        JTextField nameField = new JTextField(project.getName());
        JTextArea descField = new JTextArea(project.getDescription(), 3, 20);
        JTextField startDateField = new JTextField(project.getStartDate().toString());
        JTextField endDateField = new JTextField(project.getEndDate().toString());
        JTextField budgetField = new JTextField(String.valueOf(project.getBudget()));

        JPanel formPanel = createFormPanel(
            new String[] {"Nome:", "Descrição:", "Início (YYYY-MM-DD):", "Fim (YYYY-MM-DD):", "Orçamento:"},
            new JComponent[] {nameField, new JScrollPane(descField), startDateField, endDateField, budgetField}
        );

        JButton saveBtn = createStyledButton("💾 Salvar Alterações", ACCENT_COLOR);
        saveBtn.addActionListener(e -> {
            if (updateProject(project, nameField.getText(), descField.getText(), startDateField.getText(), endDateField.getText(), budgetField.getText())) {
                dialog.dispose();
            }
        });
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(saveBtn), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private boolean updateProject(Project p, String n, String d, String s, String en, String b) {
        try {
            p.setName(n); p.setDescription(d); p.setStartDate(LocalDate.parse(s));
            p.setEndDate(LocalDate.parse(en)); p.setBudget(Double.parseDouble(b));
            projectDAO.update(p);
            refreshProjectsList();
            return true;
        } catch (Exception e) {
            showStyledMessage("Erro ao atualizar projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void openEditMemberDialog(Member member) {
        if (member == null) return;
        JDialog dialog = createBaseDialog("✏️ Editar Membro - " + member.getName(), 500, 400);

        JTextField nameField = new JTextField(member.getName());
        JTextField emailField = new JTextField(member.getEmail());
        JTextField positionField = new JTextField(member.getPosition());
        JTextField departmentField = new JTextField(member.getDepartment());

        JPanel formPanel = createFormPanel(
            new String[] {"Nome:", "Email:", "Cargo:", "Departamento:"},
            new JComponent[] {nameField, emailField, positionField, departmentField}
        );
        
        JButton saveButton = createStyledButton("💾 Salvar Alterações", ACCENT_COLOR);
        saveButton.addActionListener(e -> {
            member.setName(nameField.getText()); member.setEmail(emailField.getText());
            member.setPosition(positionField.getText()); member.setDepartment(departmentField.getText());
            memberDAO.update(member);
            dialog.dispose();
            loadProjectMembers(currentProject.getIdProject());
        });
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void openEditTaskDialog(Task task) {
        if (task == null) return;
        JDialog dialog = createBaseDialog("✏️ Editar Tarefa - " + task.getTitle(), 600, 500);

        JTextField titleField = new JTextField(task.getTitle());
        JTextArea descField = new JTextArea(task.getDescription(), 3, 20);
        JTextField startDateField = new JTextField(task.getStartDate().toString());
        JTextField endDateField = new JTextField(task.getEndDate().toString());
        List<Member> members = projectMemberDAO.findMembersByProjectId(task.getIdProject());
        JComboBox<Member> memberComboBox = new JComboBox<>(new Vector<>(members));
        members.stream().filter(m -> m.getIdMember() == task.getIdMember()).findFirst().ifPresent(memberComboBox::setSelectedItem);

        JPanel formPanel = createFormPanel(
            new String[] {"Título:", "Descrição:", "Início (YYYY-MM-DD):", "Fim (YYYY-MM-DD):", "Responsável:"},
            new JComponent[] {titleField, new JScrollPane(descField), startDateField, endDateField, memberComboBox}
        );

        JButton saveButton = createStyledButton("💾 Salvar Alterações", ACCENT_COLOR);
        saveButton.addActionListener(e -> {
            try {
                task.setTitle(titleField.getText()); task.setDescription(descField.getText());
                task.setStartDate(LocalDate.parse(startDateField.getText())); task.setEndDate(LocalDate.parse(endDateField.getText()));
                task.setIdMember(((Member) memberComboBox.getSelectedItem()).getIdMember());
                taskDAO.update(task);
                dialog.dispose();
                loadProjectTasks(currentProject.getIdProject());
            } catch (Exception ex) {
                showStyledMessage("Erro ao atualizar tarefa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Métodos utilitários
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel title = new JLabel("🚀 Gerenciador de Projetos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        JButton newProjectBtn = createStyledButton("➕ Novo Projeto", ACCENT_COLOR);
        newProjectBtn.addActionListener(e -> openNewProjectDialog());
        header.add(newProjectBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(SECONDARY_COLOR);
        footer.setBorder(new EmptyBorder(10, 30, 10, 30));
        JLabel label = new JLabel("© 2025 Pedro Art");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(255, 255, 255, 150));
        footer.add(label, BorderLayout.WEST);
        return footer;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setBorder(new TitledBorder(null, "ℹ️ Informações",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), TEXT_COLOR));
        projectInfoLabel = new JLabel("<html><p style='padding:15px;'>Selecione um projeto na aba anterior para gerenciar.</p></html>");
        infoPanel.add(projectInfoLabel, BorderLayout.CENTER);
        return infoPanel;
    }
    
    private void displayProjectDetails(Project p) {
        projectInfoLabel.setText(String.format(
            "<html><div style='padding:15px;font-family:Segoe UI;line-height:1.5;'>" +
            "<h2 style='color:#2980b9;'>🎯 %s</h2>" +
            "<p style='color:#7f8c8d;'>%s</p>" +
            "</div></html>",
            p.getName(), p.getDescription()
        ));
    }
    
    private JDialog createBaseDialog(String title, int width, int height) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        return dialog;
    }
    
    private JPanel createFormPanel(String[] labels, JComponent[] fields) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i = 0; i < labels.length; i++) {
            addFormField(panel, gbc, labels[i], fields[i], i);
        }
        return panel;
    }

    private JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private void showStyledMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}