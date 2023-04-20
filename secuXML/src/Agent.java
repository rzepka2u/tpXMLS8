import org.w3c.dom.Document;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import java.security.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    }

    /**
     * Methode de génération des clés privée et publique.
     * Elle fait appel aux méthodes de la classe GestionnaireCles pour les générer.
     * Elle enregistre ensuite la clé publique dans un fichier qui sera public (dans le dossier shared).
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

    /**
     * Méthode qui permet d'exécuter la requête issue d'un document XML
     *
     * @param doc document XML
     * @return le résultat de la requête sous forme d'un document XML
     */
    public Document executerRequeteDepuisXML(Document doc) {
        String requeteSQL = GestionnaireDocumentsXML.extraireSQLdepuisXML(doc);
        Statement stmt;
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

    /**
     * Méthode qui permet de recevoir (récupérer) et valider un document XML contenant une requête
     *
     * @param cheminVersFichier chemin vers le fichier
     * @param clePublique       clé publique de l'autre agent
     * @return le document après verification de la signature
     */
    public Document recevoirEtValiderRequete(String cheminVersFichier, PublicKey clePublique) {
        Document doc = null;
        try {
            doc = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier(cheminVersFichier);
            boolean valide = GestionnaireDocumentsXML.validerSignatureXML(doc, clePublique);
            if (!valide) throw new ErreurSignatureException("La signature du document est erronée");
        } catch (MarshalException | XMLSignatureException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    /**
     * Méthode qui permet de signer puis d'envoyer (enregistrer) un document XML
     *
     * @param doc               le document XML à signer
     * @param cheminVersFichier chemin vers le fichier
     * @return true si l'envoie s'est bien passé
     */
    public boolean signerEtEnvoyer(Document doc, String cheminVersFichier) {
        boolean signatureOk = false;
        boolean enregistrementOk = false;
        try {
            // on signe le document doc
            Document docsigne = GestionnaireDocumentsXML.signerDocumentXML(doc, this.privateKey, this.publicKey);

            // on vérifie la signature
            signatureOk = GestionnaireDocumentsXML.validerSignatureXML(docsigne, this.publicKey);

            // on le place à l'emplacement souhaité
            enregistrementOk = GestionnaireDocumentsXML.enregistrerCodeXMLenFichier(docsigne, cheminVersFichier);


        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | KeyException | MarshalException |
                 XMLSignatureException e) {
            throw new RuntimeException(e);
        }
        return signatureOk && enregistrementOk;
    }

    /**
     * Méthode qui permet de recevoir (récupérer) et valider un document XML contenant une réponse à une requête
     *
     * @param cheminVersFichier chemin vers le fichier
     * @param clePublique       clé publique de l'autre agent
     * @return true si la signature est valide
     */
    public boolean recevoirEtValiderReponse(String cheminVersFichier, PublicKey clePublique) {
        boolean res = false;
        try {
            Document doc = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier(cheminVersFichier);
            res = GestionnaireDocumentsXML.validerSignatureXML(doc, clePublique);

        } catch (MarshalException | XMLSignatureException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * Getter clé publique
     *
     * @return la clé publique
     */
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Getter clé privée
     *
     * @return la clé privée
     */
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /**
     * Getter nomAgent
     *
     * @return le nom de l'agent
     */
    public String getNomAgent() {
        return nomAgent;
    }
}
