package frontend.common.ui;


import java.util.Observable;

/**
 * The RootRegistrationModel will act as the main model for the login
 *
 * @Version 07/03/2017
 * @Author Melisha Trout
 */

// Note: this model might be merged with RegistrationModel
public class RootRegistrationModel extends Observable {

    private static LoginModel loginModel = new LoginModel();

    public static LoginModel getLoginModel() {
        return loginModel;
    }

}
