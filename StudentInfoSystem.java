import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

// ===== Model =====
class Student {
    private String name;
    private int age;
    private String course;

    public Student(String name, int age, String course) {
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCourse() { return course; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setCourse(String course) { this.course = course; }
}

// ===== View =====
class StudentView extends JFrame {
    JTextField nameField = new JTextField();
    JTextField ageField = new JTextField();
    JTextField courseField = new JTextField();
    JButton addButton = new JButton("Add");
    JButton updateButton = new JButton("Update");
    JButton deleteButton = new JButton("Delete");
    JTable studentTable;
    DefaultTableModel tableModel;

    public StudentView() {
        setTitle("Student Information System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Form"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Age:"));
        formPanel.add(new JLabel("Course:"));
        formPanel.add(new JLabel()); // spacer
        formPanel.add(nameField);
        formPanel.add(ageField);
        formPanel.add(courseField);
        formPanel.add(addButton);
        add(formPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"Name", "Age", "Course"}, 0);
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

// ===== Controller =====
class StudentController {
    private final StudentView view;

    public StudentController(StudentView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.addButton.addActionListener(e -> addStudent());
        view.updateButton.addActionListener(e -> updateStudent());
        view.deleteButton.addActionListener(e -> deleteStudent());

        view.studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    view.nameField.setText(view.tableModel.getValueAt(selectedRow, 0).toString());
                    view.ageField.setText(view.tableModel.getValueAt(selectedRow, 1).toString());
                    view.courseField.setText(view.tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });
    }

    private void addStudent() {
        try {
            String name = view.nameField.getText();
            int age = Integer.parseInt(view.ageField.getText());
            String course = view.courseField.getText();
            view.tableModel.addRow(new Object[]{name, age, course});
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Invalid input. Please enter correct data.");
        }
    }

    private void updateStudent() {
        int selectedRow = view.studentTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String name = view.nameField.getText();
                int age = Integer.parseInt(view.ageField.getText());
                String course = view.courseField.getText();
                view.tableModel.setValueAt(name, selectedRow, 0);
                view.tableModel.setValueAt(age, selectedRow, 1);
                view.tableModel.setValueAt(course, selectedRow, 2);
                clearFields();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Invalid input for update.");
            }
        }
    }

    private void deleteStudent() {
        int selectedRow = view.studentTable.getSelectedRow();
        if (selectedRow != -1) {
            view.tableModel.removeRow(selectedRow);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(view, "Please select a row to delete.");
        }
    }

    private void clearFields() {
        view.nameField.setText("");
        view.ageField.setText("");
        view.courseField.setText("");
    }
}

// ===== Main =====
public class StudentInfoSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentView view = new StudentView();
            new StudentController(view);
            view.setVisible(true);
        });
    }
}
