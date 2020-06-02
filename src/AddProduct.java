import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class AddProduct extends JFrame {
    private JPanel mainpanel;
    private JButton saveBtn;
    private JTextField textPrice;
    private JTextField textID;
    private JTextField textTitle;
    private JButton exitBtn;
    private DBFramework DB;
    private DefaultTableModel model;
    private Product product;
    private boolean editor;
    private JTable dataTable;

    AddProduct(int width, int height, String title, Product product, boolean editor) {
        setContentPane(mainpanel);
        setPreferredSize(new Dimension(width, height));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - width / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - height / 2);
        setTitle(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/myicon.png"));

        DB = new DBFramework();
        this.product = product;
        this.editor = editor;

        if (product != null) {
            textID.setText(product.getId() + "");
            textTitle.setText(product.getTitle());
            textPrice.setText(product.getPrice() + "");
        }


        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textID.getText().length() > 0 && textPrice.getText().length() > 0 && textTitle.getText().length() > 0) {
                    if (checkValue(textID) == 1 || checkValue(textPrice) == 1) {
                        showMessage("Внимание", "Одно из полей содержит недопустимое значение", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (DB.initConnetion("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234") == 0) {
                        DB.execQuerry("create table if not exists product(id INT(64),title varchar(45),price INT(64)) ");

                        Product product = new Product();
                        product.setId(Integer.parseInt(textID.getText()));
                        product.setPrice(Integer.parseInt(textPrice.getText()));
                        product.setTitle(textTitle.getText());

                        if (editor == false) {
                            DB.execQuerry("insert into product (id,title,price) values(" + product.getId() + ",'" + product.getTitle() + "'," + product.getPrice() + ")");

                            model.addRow(new String[]{product.getId() + "", ' ' + product.getTitle() + ' ' + "", product.getPrice() + ""});
                        } else {
                            DB.execQuerry("update product set title='" + product.getTitle() + "',price='" + product.getPrice() + "' where id=" + product.getId());
                            model.removeRow(dataTable.getSelectedRow());
                            model.insertRow(dataTable.getSelectedRow() + 1, new String[]{product.getId() + "", ' ' + product.getTitle() + ' ' + "", product.getPrice() + ""});
                        }
//                        model.removeRow(data.getSelected);

                        dispose();

                    } else {
                        showMessage("Ошибка", "Нет подключения к серверу", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    showMessage("Внимание", "Не все поля товара были заполнены", JOptionPane.WARNING_MESSAGE);
                }
            }

        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    void setModel(JTable dataTable, DefaultTableModel model) {
        this.model = model;
        this.dataTable = dataTable;
    }

    void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    int checkValue(JTextField textField) {
        try {
            Integer.parseInt(textField.getText());
            return 0;
        } catch (NumberFormatException e1) {
            return 1;
        }

    }


}
