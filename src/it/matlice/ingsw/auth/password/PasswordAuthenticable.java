package it.matlice.ingsw.auth.password;

import it.matlice.ingsw.auth.Authenticable;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;

/**
 * L'interfaccia rappresenta una classe che può essere autenticata mediante il metodo
 * PasswordAuthMethod.
 * <p>
 * La gestione della password avvene mediante la verifica dell'hash (salted) della password,
 * rendendo così necessario il salvataggio si hash e salt a livello di struttura dati.
 */
public interface PasswordAuthenticable extends Authenticable {

    /**
     * Il metodo ritorna l'hash della password salvato precedentemente.
     * l'hash è computato secondo la seguente espressione:
     * \[ passwordHash = base64enc(hmac_{sha256}(key=password, data=salt)) \]
     *
     * @return l'hash della password salvato in precedenza
     */
    byte[] getPasswordHash();

    /**
     * @return ritorna il salt, generato casualmente, utilizzato per "salare la password"
     */
    byte[] getPasswordSalt();

    /**
     * Aggiorna la password associata all'utente __rigenerando__ il salt
     *
     * @param password nuova password
     */
    void setPassword(byte[] password) throws InvalidPasswordException;
    void setSalt(byte[] salt) throws InvalidPasswordException;
}
