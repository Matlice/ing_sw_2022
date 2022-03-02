package it.matlice.ingsw.view;

public interface View {

    void changePassword() throws Exception;

    void message(String title, String text);
    String getLoginUsername();
    String getPassword();

}
