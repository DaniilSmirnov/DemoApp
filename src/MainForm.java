import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;



public class MainForm extends JFrame {
    private JPanel mainpanel;
    private JButton addBtn;
    private JTable dataTable;
    private JButton deleteBtn;
    private JButton editBtn;
    private DefaultTableModel model;
    private DBFramework DB;
    private Font font;


    MainForm(int width, int height, String title, Font font) {
        setContentPane(mainpanel);
        setPreferredSize(new Dimension(width, height));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - width / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - height / 2);
        setTitle(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/myicon.png"));

        DB = new DBFramework();
//        if (DB.initConnetion("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234")==0 ) {
//            DB.execQuerry("create table if not exists product(id INT(64),title varchar(45),price int(64)) ");
//            DB.execQuerry("insert into product(id,title,price) values(1,'box',100)");
//        }

//        String header[] = {"Идентификатор", "Цена", "Название"};
//            String data[][] = {
//                    {"123", "3456", "231345t"},
//                    {"1323", "3412356", "2313r4ewed45t"},
//            };

        this.font=font;
        model = new DefaultTableModel();
        model.addColumn("Идентификатор");
        model.addColumn("Название");
        model.addColumn("Цена");

        dataTable.setModel(model);

        if (DB.initConnetion("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234") == 0) {
            ResultSet result = DB.selectQuerry("Select * from product");

            if (result != null) {
                try {
                    while (result.next()) {
                        model.addRow(new String[]{result.getString("id"), result.getString("title"), result.getString("price")});
                    }

                } catch (SQLException e1) {
                }

            } else {
                showMessage("Ошибка", "Запрашиваемая таблица не существует", JOptionPane.ERROR_MESSAGE);
            }

        }

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProduct A = new AddProduct(300, 600, "Добавить товар", null, false);
                A.setModel(dataTable, model);
                A.setVisible(true);
                A.pack();


            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataTable.getSelectedRow() >= 0) {
                    DB.execQuerry("delete from product where id=" + dataTable.getValueAt(dataTable.getSelectedRow(), 0));
                    model.removeRow(dataTable.getSelectedRow());
                } else {
                    showMessage("Внимание", "Ни одна запись для удаления не выбрана", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataTable.getSelectedRow() >= 0) {
                    Product product = new Product();
                    product.setId(Integer.parseInt(dataTable.getValueAt(dataTable.getSelectedRow(), 0) + ""));
                    product.setTitle(dataTable.getValueAt(dataTable.getSelectedRow(), 1) + "");

                    product.setPrice(Integer.parseInt(dataTable.getValueAt(dataTable.getSelectedRow(), 2) + ""));

                    AddProduct A = new AddProduct(300, 600, "Редактировать товар", product, true);
                    A.setModel(dataTable, model);
                    A.setVisible(true);
                    A.pack();
                } else {
                    showMessage("Внимание", "Ни одна запись для редактирования не выбрана", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

    }

    void showMessage(String title, String message, int type) {

        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
