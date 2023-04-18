import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.*;
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

        }
        try {
            this.con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Document executerRequeteDepuisXML(Document doc) {
        String requeteSQL = GestionnaireDocumentsXML.extraireSQLdepuisXML(doc);
        //System.out.println("Extracted SQL: " + extractedSql);
        Statement stmt;
        //String query = "select idacteur, nom from acteur";
        Document docRes = null;
        try {
            stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery(requeteSQL);
            docRes = GestionnaireDocumentsXML.conversionResultSQLversXML(rs, true);
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return docRes;
    }

    public Document recevoirEtValiderRequete(String cheminVersFichier, PublicKey clePublique) {
        Document doc;
        try {
            doc = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier(cheminVersFichier);
            System.out.println("Document obtenu : " + doc);

            boolean valide = GestionnaireDocumentsXML.validerSignatureXML(doc, clePublique);

        } catch (ParserConfigurationException | IOException | SAXException | MarshalException |
                 XMLSignatureException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    public boolean signerEtEnvoyer(Document doc, String cheminVersFichier) {
        boolean ok = false;
        try {
            // on signe le document doc
            Document docsigne = GestionnaireDocumentsXML.signerDocumentXML(doc, this.privateKey, this.publicKey);

            // on le place à l'emplacement souhaité
            GestionnaireDocumentsXML.enregistrerCodeXMLenFichier(docsigne, cheminVersFichier);

            // on vérifie la signature
            ok = GestionnaireDocumentsXML.validerSignatureXML(docsigne, this.publicKey);

        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | KeyException | MarshalException |
                 XMLSignatureException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
        return ok;
    }

    public boolean recevoirEtValiderReponse(String cheminVersFichier, PublicKey clePublique) {
        boolean res = false;
        try {
            Document doc = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier(cheminVersFichier);
            res = GestionnaireDocumentsXML.validerSignatureXML(doc, clePublique);

        } catch (ParserConfigurationException | IOException | SAXException | MarshalException |
                 XMLSignatureException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getNomAgent() {
        return nomAgent;
    }
}
