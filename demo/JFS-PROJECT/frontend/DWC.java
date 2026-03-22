import javax.swing.*;
import java.awt.*;

public class DWC {
    public static void main(String[] args) {
        new DWC().createDashboard();
    }

    // Method to create Dashboard
    public void createDashboard() {
        JFrame frame = new JFrame("Digital Warranty Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("📦 Digital Warranty Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(25, 118, 210)); // blue
        frame.add(title, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton addBtn = new JButton("➕ Add Product");
        JButton viewBtn = new JButton("📖 View Warranties");
        JButton searchBtn = new JButton("🔍 Search");
        JButton exitBtn = new JButton("❌ Exit");

        // Button styles
        addBtn.setBackground(new Color(102, 187, 106));   // Green
        viewBtn.setBackground(new Color(66, 165, 245));   // Blue
        searchBtn.setBackground(new Color(255, 202, 40)); // Yellow
        exitBtn.setBackground(new Color(239, 83, 80));    // Red

        JButton[] buttons = {addBtn, viewBtn, searchBtn, exitBtn};
        for (JButton b : buttons) {
            b.setFont(new Font("SansSerif", Font.ITALIC, 16));
            b.setFocusPainted(false);
            b.setForeground(Color.WHITE); // white text
        }

        // Add to panel
        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(searchBtn);
        panel.add(exitBtn);
        frame.add(panel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("© 2025 Warranty Tracker", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(Color.GRAY);
        frame.add(footer, BorderLayout.SOUTH);

        // Add Functionality
        addBtn.addActionListener(e -> openAddProductPage());
        viewBtn.addActionListener(e -> openViewWarrantiesPage());
        searchBtn.addActionListener(e -> openSearchPage());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    // Page 1: Add Product
    public void openAddProductPage() {
        JFrame addFrame = new JFrame("Add Product");
        addFrame.setSize(400, 300);
        addFrame.setLayout(new GridLayout(4, 2, 10, 10));

        addFrame.add(new JLabel("Product Name:"));
        JTextField nameField = new JTextField();
        addFrame.add(nameField);

        addFrame.add(new JLabel("Purchase Date:"));
        JTextField dateField = new JTextField("YYYY-MM-DD");
        addFrame.add(dateField);

        addFrame.add(new JLabel("Warranty Period (months):"));
        JTextField warrantyField = new JTextField();
        addFrame.add(warrantyField);

        JButton saveBtn = new JButton("💾 Save");
        addFrame.add(saveBtn);

        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(addFrame,
                "Product Saved: " + nameField.getText()));

        addFrame.setVisible(true);
    }

    // Page 2: View Warranties
    public void openViewWarrantiesPage() {
        JFrame viewFrame = new JFrame("View Warranties");
        viewFrame.setSize(500, 300);

        // Dummy table data for now
        String[] columns = {"Product", "Purchase Date", "Warranty (months)"};
        String[][] data = {
                {"Laptop", "2024-01-01", "24"},
                {"Mobile", "2023-08-15", "12"}
        };

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);

        viewFrame.setVisible(true);
    }

    // Page 3: Search
    public void openSearchPage() {
        JFrame searchFrame = new JFrame("Search Product");
        searchFrame.setSize(400, 200);
        searchFrame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter Product Name:");
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("🔍 Search");

        searchFrame.add(label);
        searchFrame.add(searchField);
        searchFrame.add(searchBtn);

        searchBtn.addActionListener(e -> JOptionPane.showMessageDialog(searchFrame,
                "Searching for: " + searchField.getText()));

        searchFrame.setVisible(true);
    }
}
