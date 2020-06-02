import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/*
Окно входа в систему - необходимо для входа в систему, осуществляет проверку к доступу к БД, логин для входа -1, пароль - 1 и.т.д
Разработал: Петров Алексей Владимирович
Почта:
 */
public class LoginForm extends JFrame {

    private JPanel mainPanel;
    private JTextField loginField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JLabel labelLogin;
    private JLabel labelPass;
    private final int WIDTH = 400, HEIGHT = 400;
    private Font font;

    private DBFramework DB;


    LoginForm() {
        setContentPane(mainPanel);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - WIDTH / 2, (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - HEIGHT / 2);
        setTitle("Авторизация");
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/myicon.png"));


        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("Fonts/CaviarDreamsRegular.ttf"))).deriveFont(Font.PLAIN, 12);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB = new DBFramework();
        loginBtn.setFont(font.deriveFont(Font.PLAIN, 40));
        loginField.setFont(font);
        labelLogin.setFont(font);
        labelPass.setFont(font);


        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login_s = loginField.getText().toString();
                String pass_s = passField.getText().toString();
                if (login_s.length() == 0 || pass_s.length() == 0) {

                    showMessage("Внимание", "Не все поля заполнены", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (DB.initConnection("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234") == 0) {

                        ResultSet result = DB.selectQuery("Select * from users");
                        try {
                            while (result.next()) {
                                if (login_s.equals(result.getString("login")) && pass_s.equals(result.getString("pass"))) {
                                    MainForm F = new MainForm(600, 500, "Склад", font);
                                    F.setVisible(true);
                                    F.pack();
                                    dispose();
                                    DB.closeConnection();

                                    return;
                                }
                            }
                            showMessage("Ошибка", "Логин или пароль введены неверно", JOptionPane.ERROR_MESSAGE);

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else
                        showMessage("Внимание", "Нет подключения к серверу", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }


    void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }


    public static void main(String a[]) {
        LoginForm loginForm = new LoginForm();
        loginForm.pack();
        loginForm.setVisible(true);
    }

}

