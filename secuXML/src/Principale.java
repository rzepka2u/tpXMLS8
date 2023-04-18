import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.PublicKey;

public class Principale {

    private final Agent agent1, agent2;

    private final Thread thread1, thread2;

    public Principale(String[] args) {
        // Création des instances de la classe Agent
        this.agent1 = new Agent("Robert1", "jdbc:mysql://localhost:8889/projetXML", args[0], args[1]);
        this.agent2 = new Agent("Michel2", "jdbc:mysql://localhost:8889/baseTest", args[0], args[1]);

        // Création des deux threads et attribution à un agent chacun
        this.thread1 = new Thread(this.agent1, "Robert1");
        this.thread2 = new Thread(this.agent2, "Michel2");
    }

    public void lancerThreads() {
        // Démarrage des deux threads
        this.thread1.start();
        this.thread2.start();
    }

    public void arreterThreads() {
        // Arrêt des Threads
        try {
            this.thread1.interrupt();
            this.thread2.interrupt();
            this.thread1.join();
            this.thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void lancerProgramme() throws ParserConfigurationException, IOException, SAXException {
        // L'agent 1 récupère un document XML non signé
        Document docASignerEtEnvoyer1 = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier("local/sql_query_agent1.xml");

        // L'agent 1 signe et envoie le document
        boolean ok = this.agent1.signerEtEnvoyer(docASignerEtEnvoyer1, "shared/sql_query_agent1_signed.xml");

        System.out.println("Signature obtenue ok : "+ok);

        // L'agent 2 reçoit et valide le document d'après la clé publique du client 1
        Document docAExecuter = this.agent2.recevoirEtValiderRequete("shared/sql_query_agent1_signed.xml", GestionnaireCles.chargerClePubliqueDepuisFichier(this.agent1.getNomAgent()));

        System.out.println("Document obtenu : "+docAExecuter);

        // L'agent 2 execute la requête du document
        Document docResASignerEtEnvoyer = this.agent2.executerRequeteDepuisXML(docAExecuter);

        // L'agent 2 signer et envoie la réponse
        this.agent2.signerEtEnvoyer(docResASignerEtEnvoyer, "shared/ql_query_agent1_response_signed.xml");

        // L'agent 1 reçoit et valide la signature de la réponse
        boolean res = this.agent1.recevoirEtValiderReponse("shared/ql_query_agent1_response_signed.xml", GestionnaireCles.chargerClePubliqueDepuisFichier(this.agent2.getNomAgent()));

        /*
        try {
            Document doc  = GestionnaireDocumentsXML.signerDocumentXML(GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier("shared/sql_query_agent1.xml"), agent1.getPrivateKey(), agent2.getPublicKey());
            boolean res = GestionnaireDocumentsXML.validerSignatureXML(doc, agent1.getPublicKey());
            System.out.println(res);
            GestionnaireDocumentsXML.enregistrerCodeXMLenFichier(doc, "shared/sql_query_agent1_signed.xml");
            this.agent1.executerRequeteDepuisXML("shared/sql_query_agent1_signed.xml", "shared/sql_query_signed_res_agent1.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Principale p = new Principale(args);
        p.lancerThreads();
        p.lancerProgramme();
        p.arreterThreads();
    }
}