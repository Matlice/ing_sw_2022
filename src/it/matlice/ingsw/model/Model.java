package it.matlice.ingsw.model;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.controller.Authentication;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.controller.exceptions.InvalidUserException;
import it.matlice.ingsw.controller.exceptions.LoginInvalidException;
import it.matlice.ingsw.view.View;

public class Model {

    private View view;
    private Authentication currentUser;

    private static Model instance;
    private Model(View view) {
        this.view = view;
    }

    public static Model getInstance() {
        return instance;
    }

    public static Model startInstance(View view){
        assert instance == null;
        instance = new Model(view);
        return instance;
    }

    //Controller to Model ==================================================================
    public void forceChangePassword(){
        while(true) {
            try {
                this.view.changePassword();
                break;
            } catch (Exception e){
                view.message("Errore", "La password deve essere cambiata al primo accesso.");
            }
        }
    }

    public AuthData getLoginData(String method){
        if(method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(view.getPassword());
        else {
            view.message("Errore", "Nessun metodo di login disponibile per " + method);
            return null;
        }
    }

    public void login(){
        var username = view.getLoginUsername();
        try {
            this.currentUser = Controller.getInstance().authenticate(username);
            if(this.currentUser == null){
                view.message("Errore", "password errata");
            }
        } catch (InvalidUserException e) {
            view.message("Errore", "Invalid user");
        }
    }

    //Model to Controller ==================================================================
    public void changePassword(String password){
        try {
            Controller.getInstance().changePassword(currentUser, password);
        } catch (InvalidPasswordException e){
            view.message("Errore", "La password non rispetta i requisiti di sicurezza");
        } catch (LoginInvalidException e){
            view.message("Errore", "Il login non è più valido.");
        }
    }
}
