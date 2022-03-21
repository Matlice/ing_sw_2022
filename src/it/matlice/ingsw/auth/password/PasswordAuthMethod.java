package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.AuthMethod;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Rappresenta il metodo di autenticazione mediante password.
 * La classe è un wrapper di una classe PasswordAuthenticable.
 */
public class PasswordAuthMethod implements AuthMethod {

    private final PasswordAuthenticable user;
    private final Random random_source;

    public static final String MAC_ALGO = "HmacSHA256";
    public static final Pattern PASSWORD_SEC_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_()\\-=+])(?=.{8,}).*$");
    public static final int SALT_LENGTH = 64;

    public PasswordAuthMethod(PasswordAuthenticable user) {
        this.user = user;
        this.random_source = new SecureRandom();
    }

    /**
     * Metodo ausiliario che ritorna un istanza valida di authdata per la classe.
     *
     * @param password password di login
     * @return un'istanza di AuthData valida.
     */
    public static AuthData getAuthData(String password) {
        return new PasswordAuthData(password);
    }

    public static boolean isPasswordValid(String password){
        return PasswordAuthMethod.PASSWORD_SEC_REGEX.matcher(password).matches();
    }

    /**
     * questo metodo permette di impostare la password dell'utente
     *
     * @param password nuova password che rispetti i parametri impostati nella configurazione
     * @throws InvalidPasswordException nel caso in cui la password non rispetti la complessità richiesta
     */
    public void setPassword(String password, boolean skipComplexityCheck) throws InvalidPasswordException {
        if (!skipComplexityCheck && !isPasswordValid(password))
            throw new InvalidPasswordException();
        var salt = this.getNewSalt();
        this.user.setSalt(salt);
        this.user.setPassword(this.getPasswordHash(salt, password));
    }

    public void setPassword(String password) throws InvalidPasswordException {
        this.setPassword(password, false);
    }

    /**
     * la funzione effettua il procedimento di autenticazione in funzione della sua istanza.
     * È necessario che venga asserito che data sia di un tipo valido all'autenticazione con password
     *
     * @param data dati necessari all'autenticazione
     * @return true se l'autenticazione ha successo.
     */
    @Override
    public boolean performAuthentication(AuthData data) {
        assert data instanceof PasswordAuthData;
        return Arrays.equals(
                this.getPasswordHash(this.user.getPasswordSalt(), ((PasswordAuthData) data).password()),
                this.user.getPasswordHash()
        );
    }

    private byte @NotNull [] getNewSalt(){
        var salt = new byte[SALT_LENGTH];
        this.random_source.nextBytes(salt);
        return salt;
    }

    private byte[] getPasswordHash(byte[] salt, @NotNull String password){
        try {
            var mac = Mac.getInstance("HmacSHA256");
            var secret_key = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), MAC_ALGO);
            mac.init(secret_key);
            return mac.doFinal(salt);
        } catch (NoSuchAlgorithmException nsae){
            System.err.println("Cannot find mac algorithm provider");
            throw new RuntimeException(nsae);
        } catch (InvalidKeyException ike){
            throw new RuntimeException(ike);
        }

    }
}
