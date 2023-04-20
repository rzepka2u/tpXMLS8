import org.w3c.dom.Document;

/**
 * Classe Principale du programme
 *
 * @author Thomas RZEPKA
 */
public class Principale {

    private final String nomAgent1, nomAgent2;
    private final Agent agent1, agent2;

    private final Thread thread1, thread2;

    /**
     * Constructeur
     *
     * @param args arguments du programme
     */
    public Principale(String[] args) {
        // Attribution des noms (au choix, ils doivent être strictement différents / uniques)
        this.nomAgent1 = "Robert1";
        this.nomAgent2 = "Michel2";

        // Création des instances de la classe Agent
        this.agent1 = new Agent(this.nomAgent1, "jdbc:mysql://localhost:8889/projetXML", args[0], args[1]);
        this.agent2 = new Agent(this.nomAgent2, "jdbc:mysql://localhost:8889/baseTest", args[0], args[1]);

        // Création des deux threads et attribution à un agent chacun
        this.thread1 = new Thread(this.agent1, this.nomAgent1);
        this.thread2 = new Thread(this.agent2, this.nomAgent2);

    }

    /**
     * Methode qui permet de lancer les Threads
     */
    public void lancerThreads() {
        // Démarrage des deux threads
        this.thread1.start();
        this.thread2.start();
    }

    /**
     * Methode qui permet d'arrêter les Threads
     */
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

    /**
     * Méthode principale
     */
    public void lancerProgramme() {
        // Génération des clés publique et privée de chaque agent (les clés publiques sont publiées dans le dossier 'shared')
        this.agent1.genererCles();
        this.agent2.genererCles();

        // L'agent 1 récupère un document XML non signé
        Document docASignerEtEnvoyer1 = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier("local/requete_sql_agent1.xml");

        // L'agent 1 signe et envoie le document
        boolean ok = this.agent1.signerEtEnvoyer(docASignerEtEnvoyer1, "shared/requete_sql_agent1_signed.xml");

        if (ok) System.out.println("L'agent 1 a signé et envoyé la requête");
        else System.out.println("La signature de la requête a échoué, l'agent 1 ne l'a pas envoyé");

        // L'agent 2 reçoit et valide le document d'après la clé publique du client 1
        Document docAExecuter = this.agent2.recevoirEtValiderRequete("shared/requete_sql_agent1_signed.xml", GestionnaireCles.chargerClePubliqueDepuisFichier(this.agent1.getNomAgent()));

        System.out.println("L'agent 2 a reçu la requête de l'agent 1 et a validé sa signature");

        // L'agent 2 execute la requête du document
        Document docResASignerEtEnvoyer = this.agent2.executerRequeteDepuisXML(docAExecuter);

        System.out.println("L'agent 2 a exécuté la requête de l'agent 1");

        // L'agent 2 signer et envoie la réponse
        ok = this.agent2.signerEtEnvoyer(docResASignerEtEnvoyer, "shared/requete_sql_agent1_response_signed.xml");

        if (ok) System.out.println("L'agent 2 a signé et envoyé la réponse");
        else System.out.println("La signature de la réponse a échoué, l'agent 2 ne l'a pas envoyé");

        // L'agent 1 reçoit et valide la signature de la réponse
        ok = this.agent1.recevoirEtValiderReponse("shared/requete_sql_agent1_response_signed.xml", GestionnaireCles.chargerClePubliqueDepuisFichier(this.agent2.getNomAgent()));

        if (ok) System.out.println("L'agent 1 a reçu la réponse et a validé la signature");
        else System.out.println("L'agent 1 a reçu la réponse mais n'a pas validé la signature");

        Document obtenu = GestionnaireDocumentsXML.chargerCodeXMLdepuisFichier("shared/requete_sql_agent1_response_signed.xml");

        System.out.println("\nVoici la réponse obtenue : \n\n" + GestionnaireDocumentsXML.getStringResultatDepuisXML(obtenu));

    }

    /**
     * Méthode de lancement du programme
     *
     * @param args arguments du programme
     */
    public static void main(String[] args) {
        Principale p = new Principale(args);
        p.lancerThreads();
        p.lancerProgramme();
        p.arreterThreads();
    }
}