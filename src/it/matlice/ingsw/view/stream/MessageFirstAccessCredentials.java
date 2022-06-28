package it.matlice.ingsw.view.stream;

import it.matlice.ingsw.model.data.Settings;

import java.util.StringJoiner;

public class MessageFirstAccessCredentials extends AStreamMessage {

    private final String user;
    private final String password;

    public MessageFirstAccessCredentials(StreamView view, String user, String password) {
        super(view);
        this.user = user;
        this.password = password;
    }

    @Override
    public String getMessage() {
        return String.format("Per il primo accesso le credenziali sono %s:%s", this.user, this.password);
    }
}
