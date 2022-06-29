package it.matlice.ingsw.view.stream;

public class StreamMessageFirstAccessCredentials extends AStreamMessage {

    private final String user;
    private final String password;

    public StreamMessageFirstAccessCredentials(StreamView view, String user, String password) {
        super(view);
        this.user = user;
        this.password = password;
    }

    @Override
    public String getMessage() {
        return String.format("Per il primo accesso le credenziali sono %s:%s", this.user, this.password);
    }
}
