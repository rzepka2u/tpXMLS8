import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Classe de gestion des clés privée et publique
 *
 * @author Thomas RZEPKA
 */
public class GestionnaireCles {

    /**
     * Méthode qui permet de générer des clés de chiffrement asymétrique
     * @param algorithme Algorithme à utiliser (RSA recommandé)
     * @param tailleCle Taille de la clé (minimum 2048 recommandé)
     * @return la paire de clés
     * @throws NoSuchAlgorithmException Si l'algorithme demandé n'est pas trouvé
     */
    public static KeyPair genererCles(String algorithme, int tailleCle) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithme);
        keyPairGenerator.initialize(tailleCle);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Methode qui permet d'enregistrer une clé publique dans un fichier qui sera rendu public.
     * @param nomAgent nom de l'agent, qui sera utilisé pour nommer la clé
     * @param publicKey clé publique à enregistrer
     */
    public static void enregistrerClePubliqueVersFichier(String nomAgent, PublicKey publicKey) {
        try {
            // Ouverture (création) du fichier
            FileOutputStream fos = new FileOutputStream("shared/"+nomAgent+"_publicKey.der");
            // Écriture dans le fichier
            fos.write(publicKey.getEncoded());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui permet de récupérer la clé publique d'un agent, stockée dans le dossier partagé
     * @param nomAgent nom de l'agent
     * @return la clé publique de l'agent demandé
     */
    public static PublicKey chargerClePubliqueDepuisFichier(String nomAgent) {
        PublicKey clePub = null;
        try {
            // Préparation des données et fichiers
            KeyFactory kf = KeyFactory.getInstance("RSA");
            File fichierClePub = new File("shared/" + nomAgent + "_publicKey.der");
            FileInputStream publicKeyStream = new FileInputStream(fichierClePub);

            // Lecture du fichier
            byte[] publicKeyBytes = new byte[(int) fichierClePub.length()];
            publicKeyStream.read(publicKeyBytes);
            publicKeyStream.close();

            // Régénération de la clé publique sous forme de variable en mémoire
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            clePub = kf.generatePublic(publicKeySpec);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return clePub;
    }
}
