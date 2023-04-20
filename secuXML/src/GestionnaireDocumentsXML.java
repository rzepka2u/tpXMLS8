import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Classe de gestion des documents XML pour la création et l'enregistrement de résultats de requêtes SQL
 *
 * @author Thomas RZEPKA
 */
public class GestionnaireDocumentsXML {

    /**
     * Méthode qui permet de lire du code XML depuis un fichier
     *
     * @param cheminFichier chemin du fichier
     * @return document XML lu
     */
    public static Document chargerCodeXMLdepuisFichier(String cheminFichier) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(new File(cheminFichier));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode qui permet d'enregistrer du code XML dans un fichier
     *
     * @param doc           document XML a enregistrer
     * @param cheminFichier chemin du fichier
     * @return true si l'enregistrement s'est bien passé
     */
    public static boolean enregistrerCodeXMLenFichier(Document doc, String cheminFichier) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(cheminFichier));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Méthode qui permet d'extraire les requêtes SELECT en SQL depuis un document XML
     *
     * @param doc document XML source
     * @return une chaine représentant la requête SQL
     */
    public static String extraireSQLdepuisXML(Document doc) {
        // Lecture de la balise SELECT
        Element select = (Element) doc.getElementsByTagName("SELECT").item(0);

        // Lecture des balises CHAMP de la balise CHAMPS
        Element champs = (Element) doc.getElementsByTagName("CHAMPS").item(0);
        NodeList champ = champs.getElementsByTagName("CHAMP");
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < champ.getLength(); i++) {
            sb1.append(champ.item(i).getTextContent());
            if (i < champ.getLength() - 1) {
                sb1.append(", ");
            }
        }

        // Lecture des balises TABLE de la balise TABLES
        Element tables = (Element) doc.getElementsByTagName("TABLES").item(0);
        NodeList table = tables.getElementsByTagName("TABLE");
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < table.getLength(); i++) {
            sb2.append(table.item(i).getTextContent());
            if (i < table.getLength() - 1) {
                sb2.append(", ");
            }
        }

        // Lecture de la balise CONDITION
        String condition;
        try {
            condition = select.getElementsByTagName("CONDITION").item(0).getTextContent();
        } catch (NullPointerException e) {
            condition = "";
        }

        // Si la balise n'existe pas, ou est vide, on l'ignore
        if (!condition.equals(" ") && !condition.equals(""))
            return String.format("SELECT %s FROM %s WHERE %s", sb1, sb2, condition);
        else return String.format("SELECT %s FROM %s", sb1, sb2);
    }

    /**
     * Méthode qui permet de convertir un résultat SQL en un fichier XML
     *
     * @param resultSet            résultat SQL
     * @param preciserColonneCHAMP true pour préciser le nom de la colonne à la place de la balise "CHAMP"
     * @return le document XML obtenu (il faut ensuite l'enregistrer dans un fichier)
     * @throws ParserConfigurationException si la création du document XML échoue
     * @throws SQLException                 si le resultSet est incorrect
     */
    public static Document conversionResultSQLversXML(ResultSet resultSet, boolean preciserColonneCHAMP) throws ParserConfigurationException, SQLException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        // Création de la balise RESULTAT
        Element resultat = doc.createElement("RESULTAT");
        doc.appendChild(resultat);

        // Création de la balise TUPLES
        Element tuples = doc.createElement("TUPLES");
        resultat.appendChild(tuples);

        // Récupération des informations sur le résultat
        ResultSetMetaData rsMetaData = resultSet.getMetaData();

        // Récupération du nombre de colonnes à traiter
        int nbColonne = rsMetaData.getColumnCount();

        // Création de chaque balise TUPLE de TUPLES
        while (resultSet.next()) {
            Element tuple = doc.createElement("TUPLE");
            tuples.appendChild(tuple);

            // Création de chaque balise CHAMP du TUPLE
            for (int i = 1; i <= nbColonne; i++) {
                // Récupération du nom de la colonne
                String nomColonne = "CHAMP";
                if (preciserColonneCHAMP) nomColonne = rsMetaData.getColumnName(i);

                Element e = doc.createElement(nomColonne);
                e.setTextContent(resultSet.getString(i));
                tuple.appendChild(e);
            }
        }
        return doc;
    }

    /**
     * Méthode qui permet de signer un document XML
     *
     * @param doc         document XML source à signer
     * @param clePrivee   clé privée utilisée pour signer
     * @param clePublique clé publique inscrite dans le fichier qui sera utilisée pour vérifier la signature
     * @return le nouveau Document avec la signature
     * @throws InvalidAlgorithmParameterException si des paramètres de signature sont invalides
     * @throws NoSuchAlgorithmException           si l'algorithme de signature est introuvable
     * @throws KeyException                       si la clé est invalide
     * @throws MarshalException                   si l'ajout de signature est impossible
     * @throws XMLSignatureException              si la création de la signature échoue
     */
    public static Document signerDocumentXML(Document doc, PrivateKey clePrivee, PublicKey clePublique) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, KeyException, MarshalException, XMLSignatureException {
        // Récupération d'un XMLSignatureFactory
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

        // Création des références et transformations nécessaires à insérer
        Reference reference = signatureFactory.newReference("", signatureFactory.newDigestMethod(DigestMethod.SHA256, null), Collections.singletonList(signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);

        // Création du SignedInfo du document
        SignedInfo signedInfo = signatureFactory.newSignedInfo(signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null), Collections.singletonList(reference));

        // Création du KeyInfo du document
        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        KeyValue keyValue = keyInfoFactory.newKeyValue(clePublique);
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));

        // Création de l'objet XMLSignature et ajout au document
        XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
        DOMSignContext signContext = new DOMSignContext(clePrivee, doc.getDocumentElement());
        signature.sign(signContext);

        return doc;
    }

    /**
     * Méthode qui permet de vérifier la signature d'un document XML
     *
     * @param doc         document XML à vérifier
     * @param clePublique clé publique permettant la vérification de signature
     * @return true si la signature est valide
     * @throws MarshalException      si la vérification de signature est impossible
     * @throws XMLSignatureException si la validation de la signature échoue
     */
    public static boolean validerSignatureXML(Document doc, PublicKey clePublique) throws MarshalException, XMLSignatureException {
        NodeList nodeList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

        if (nodeList.getLength() == 0) {
            throw new ErreurSignatureException("Le document n'est pas signé");
        }

        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

        DOMValidateContext validateContext = new DOMValidateContext(clePublique, nodeList.item(0));
        XMLSignature signature = signatureFactory.unmarshalXMLSignature(validateContext);

        return signature.validate(validateContext);
    }

    /**
     * Méthode qui permet de récupérer un String contenant le résultat d'une requête
     *
     * @param doc document XML source
     * @return un String
     */
    public static String getStringResultatDepuisXML(Document doc) {
        StringBuilder resultat = new StringBuilder();

        NodeList listeResultats = doc.getElementsByTagName("RESULTAT");

        if (listeResultats.getLength() > 0) {
            Element elemResultat = (Element) listeResultats.item(0);
            NodeList listeTuples = elemResultat.getElementsByTagName("TUPLE");

            for (int i = 0; i < listeTuples.getLength(); i++) {
                Node noeudTuple = listeTuples.item(i);
                NodeList listeChamps = noeudTuple.getChildNodes();

                resultat.append("Tuple ").append(i + 1).append(":\n");

                for (int j = 0; j < listeChamps.getLength(); j++) {
                    Node noeudChamp = listeChamps.item(j);

                    if (noeudChamp.getNodeType() == Node.ELEMENT_NODE) {
                        Element elemChamp = (Element) noeudChamp;
                        String nomChamp = elemChamp.getTagName();
                        String valeurChamp = elemChamp.getTextContent();
                        resultat.append(nomChamp).append(": ").append(valeurChamp).append("\n");
                    }
                }
                resultat.append("\n");
            }
        }
        return resultat.toString();
    }

    /**
     * Méthode qui permet de récupérer l'ensemble du document XML sous forme de String
     * A des fins de debugs uniquement
     *
     * @param doc document XML source
     * @return un String
     */
    public static String getAllStringFromXML(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            return stringWriter.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}

