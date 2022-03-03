package it.matlice.ingsw.model;

import it.matlice.ingsw.auth.AuthData;
import it.matlice.ingsw.auth.exceptions.InvalidPasswordException;
import it.matlice.ingsw.auth.password.PasswordAuthMethod;
import it.matlice.ingsw.controller.Authentication;
import it.matlice.ingsw.controller.Controller;
import it.matlice.ingsw.controller.exceptions.InvalidUserException;
import it.matlice.ingsw.controller.exceptions.LoginInvalidException;
import it.matlice.ingsw.data.ConfiguratorUser;
import it.matlice.ingsw.data.User;
import it.matlice.ingsw.view.View;

import java.util.Arrays;
import java.util.List;

public class Model {

    private final View view;
    private final Controller controller;
    private Authentication currentUser;

    private final List<MenuAction> user_actions = Arrays.asList(
            new MenuAction("Change password", User.class, () -> this.changePassword()),
            new MenuAction("New Configurator", ConfiguratorUser.class, () -> true),
            new MenuAction("New Hierarchy", ConfiguratorUser.class, () -> true),
            new MenuAction("Show Hierarchy", ConfiguratorUser.class, () -> true),
            new MenuAction("Exit", User.class, () -> false)

    );

    public Model(View view, Controller c) {
        this.view = view;
        this.controller = c;
        c.setModel(this);
    }

    public boolean mainloop() {
        if (this.currentUser == null) {
            this.login();
            if (this.currentUser.getUser().getLastLoginTime() == null) {
                this.view.message("WARN", "You need to change your password");
                return this.changePassword();
            }
            this.controller.finalizeLogin(this.currentUser);
            return true;
        } else {
            var action = this.view.choose(this.user_actions.stream().filter(e -> e.isPermitted(this.currentUser.getUser())).toList());
            if (action != null)
                return action.getAction().run();
            else
                return true;
        }
    }

    public void forceChangePassword() {
        while (true) {
            try {
                this.view.changePassword();
                break;
            } catch (Exception e) {
                this.view.message("Errore", "La password deve essere cambiata al primo accesso.");
            }
        }
    }

    public AuthData getLoginData(String method){
        if(method.equals(PasswordAuthMethod.class.getName()))
            return PasswordAuthMethod.getAuthData(this.view.getPassword());
        else {
            this.view.message("Errore", "Nessun metodo di login disponibile per " + method);
            return null;
        }
    }

    public void login(){
        var username = this.view.getLoginUsername();
        try {
            this.currentUser = this.controller.authenticate(username);
            if(this.currentUser == null){
                this.view.message("Errore", "password errata");
            }
        } catch (InvalidUserException e) {
            this.view.message("Errore", "Invalid user");
        }
    }

    public boolean changePassword() {
        try {
            var psw = this.view.changePassword();
            this.controller.changePassword(this.currentUser, psw);
            return true;
        } catch (InvalidPasswordException e) {
            this.view.message("Errore", "La password non rispetta i requisiti di sicurezza");
        } catch (LoginInvalidException e) {
            this.view.message("Errore", "Il login non è più valido.");
        } catch (Exception e) {
            this.view.message("Errore", e.getMessage());
        }
        return true;
    }
}
