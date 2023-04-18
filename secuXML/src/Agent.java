import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * La classe Agent représente un agent
 *
 * @author Thomas RZEPKA
 */
public class Agent implements Runnable {

    private final String nomAgent;
    private final Connection con;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private final BlockingQueue<Document> incomingQueue;
    private final BlockingQueue<Document> outgoingQueue;

    /**
     * Constructeur Agent
     *
     * @param nomAgent   nom de l'agent (ils doivent tous impérativement avoir un nom différent)
     * @param dbUrl      URL de la BDD
     * @param dbUsername identifiant de la BDD
     * @param dbPassword mot de passe de la BDD
     */
    public Agent(String nomAgent, String dbUrl, String dbUsername, String dbPassword) {
        this.nomAgent = nomAgent;
        try {
            Connexion.setDbURL(dbUrl);
            Connexion.setUsername(dbUsername);
            Connexion.setPassword(dbPassword);
            this.con = Connexion.getConnection();
        } catch (SQLException e) {
            System.out.println("Erreur d'accès au serveur SQL");
            throw new RuntimeException(e);
        }
        this.incomingQueue = new LinkedBlockingQueue<>();
        this.outgoingQueue = new LinkedBlockingQueue<>();
        this.genererCles();
    }

    /**
     * Methode de génération des clés privée et publique.
     * Elle fait appel aux méthodes de la classe GestionnaireCles pour les générer.
     * Elle enregistre ensuite la clé publique dans un fichier qui sera public.
     */
    public void genererCles() {
        try {
            // Génération des clés
            KeyPair keyPair = GestionnaireCles.genererCles("RSA", 2048);
            // Récupération de la clé publique et de la clé privée
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        GestionnaireCles.enregistrerClePubliqueVersFichier(this.nomAgent, this.publicKey);
    }

    /**
     * Methode pour lancer le Thread.
     */
    @Override
    public void run() {
        //System.out.println("Executing " + this.nomAgent + " on thread " + Thread.currentThread().getName());
        while (!Thread.interrupted()) {
            try {
                processIncomingRequest();
                processOutgoingRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void executerRequeteDepuisXML(String inputFilePath, String outputFilePath) {
        Document loadedXml;
        String extractedSql = "";
        try {
            loadedXml = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier(inputFilePath);
            extractedSql = GestionnaireDocumentsXML.extraireSQLdepuisXML(loadedXml);
            //System.out.println("Extracted SQL: " + extractedSql);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.println("Error during XML-SQL conversion: " + e.getMessage());
        }
        Statement stmt;
        //String query = "select idacteur, nom from acteur";
        String query = extractedSql;

        try {
            stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Document doc = GestionnaireDocumentsXML.conversionResultSQLversXML(rs, true);
            GestionnaireDocumentsXML.enregistrerCodeXMLenFichier(doc, outputFilePath);
            stmt.close();
            con.close();


        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequest(String filePath) {
        System.out.println(filePath);
        // TODO
        //Document request = XMLUtil.loadXML(filePath);
        //outgoingQueue.add(request);
    }

    private void processIncomingRequest(/*PublicKey publicKey*/) {
        /* TODO
        Document request = incomingQueue.take();
        if (XMLUtil.validateXML(request, publicKey)) {
            String sql = XMLUtil.extractSQL(request);
            ResultSet resultSet = executeQuery(sql);
            Document response = XMLUtil.createXMLFromResultSet(resultSet);
            outgoingQueue.add(response);
        }
        */
    }

    private void processOutgoingRequest(/*PrivateKey privateKey, X509Certificate certificate*/) {
        /* TODO
        Document request = outgoingQueue.take();
        String sql = XMLUtil.extractSQL(request);
        ResultSet resultSet = executeQuery(sql);
        Document response = XMLUtil.createXMLFromResultSet(resultSet);
        // Signer le document XML de réponse avec la clé privée de l'agent
        response = XMLUtil.signXML(response, privateKey, certificate);
        // Envoyer la réponse signée à l'autre agent
        // Implémentez la logique d'envoi ici
         */
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
