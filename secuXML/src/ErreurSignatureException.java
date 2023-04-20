/**
 * Classe d'Erreur de Signature
 *
 * @author Thomas RZEPKA
 */
public class ErreurSignatureException extends RuntimeException {

    /**
     * Runtime exception levée en cas d'erreur de signature (signature invalide)
     * @param message message
     */
    public ErreurSignatureException(String message) {
        super(message);
    }
}
