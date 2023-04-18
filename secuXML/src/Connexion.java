import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe Connexion gère la phase de connexion avec la BDD
 *
 * @author Thomas RZEPKA
 */
public class Connexion {

    private static Connection connexion;

    private static String dbURL, username, password;

    /**
     * Créé une connexion
     *
     * @return une connexion à la base
     * @throws SQLException erreur SQL
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connexion == null) {
            connexion = DriverManager.getConnection(dbURL, username, password);
        }
        return connexion;
    }

    /**
     * Méthode pour definir l'URL de la base de donnees
     *
     * @param url URL de la base de donnees
     */
    public static void setDbURL(String url) {
        dbURL = url;
    }

    /**
     * Méthode pour définir le nom d'utilisateur
     *
     * @param userName nom d'utilisateur
     */
    public static void setUsername(String userName) {
        username = userName;
    }

    /**
     * Méthode pour définir le mot de passe
     *
     * @param passWord mot de passe
     * @throws SQLException si un paramètre est incorrect
     */
    public static void setPassword(String passWord) throws SQLException {
        password = passWord;
        connexion = null;
        connexion = getConnection();
    }
}