import org.w3c.dom.Document;

import java.security.PublicKey;

public class Principale {

    private final Agent agent1, agent2;

    private final Thread thread1, thread2;

    public Principale(String[] args) {
        // Création des instances de la classe Agent
        this.agent1 = new Agent("Robert1", "jdbc:mysql://localhost:8889/projetXML", args[0], args[1]);
        this.agent2 = new Agent("Michel2", "jdbc:mysql://localhost:8889/baseTest", args[0], args[1]);

        // Création des deux threads et attribution à un agent chacun
        this.thread1 = new Thread(this.agent1);
        this.thread2 = new Thread(this.agent2);
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

    public void lancerProgramme() {
        //this.agent1.sendRequest("je suis le 1");
        //this.agent1.sendRequest("je suis le 2");

        this.agent1.executerRequeteDepuisXML("shared/sql_query_agent1.xml", "shared/res_agent1.xml");
        this.agent2.executerRequeteDepuisXML("shared/sql_query_agent2.xml", "shared/res_agent2.xml");

        PublicKey publicKey_agent1 = this.agent1.getPublicKey();
        PublicKey publicKey_agent2 = this.agent2.getPublicKey();

        PublicKey publicKey_agent1_file = GestionnaireCles.chargerClePubliqueDepuisFichier("Robert1");
        PublicKey publicKey_agent2_file = GestionnaireCles.chargerClePubliqueDepuisFichier("Michel2");

        //System.out.println("Clé en mémoire pour agent1 : \n " + publicKey_agent1);
        //System.out.println("Clé sur disque pour agent1 : \n " + publicKey_agent1_file);
        //System.out.println("Clé en mémoire pour agent2 : \n " + publicKey_agent2);
        //System.out.println("Clé sur disque pour agent2 : \n " + publicKey_agent2_file);

        try {
            Document doc  = GestionnaireDocumentsXML.signerDocumentXML(GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier("shared/res_agent1.xml"), agent1.getPrivateKey(), agent2.getPublicKey());
            boolean res = GestionnaireDocumentsXML.validerSignatureXML(doc, agent1.getPublicKey());
            System.out.println(res);
            GestionnaireDocumentsXML.enregistrerCodeXMLenFichier(doc, "shared/res_agent1_signed.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Principale p = new Principale(args);
        p.lancerThreads();
        p.lancerProgramme();
        p.arreterThreads();
    }
}