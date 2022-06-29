package it.matlice.ingsw.view.stream;

public abstract class AStreamMessage implements IStreamMessage {

    public StreamView getView() {
        return this.view;
    }

    private StreamView view;

    public AStreamMessage(StreamView view) {
        this.view = view;
    }

    public void show() {
        this.view.println(this.getMessage());
    }
    public abstract String getMessage();

}
