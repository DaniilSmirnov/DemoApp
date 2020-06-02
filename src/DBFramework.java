import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFramework {
    private Connection connection;

    int initConnetion(String url, String login, String password) {
        try {
            connection = DriverManager.getConnection(url, login, password);
            return 0;
        } catch (SQLException e) {

        }
        return 1;
    }

    void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet selectQuerry(String myquerry) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(myquerry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    void execQuerry(String querry) {
        try {
            connection.prepareStatement(querry).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
