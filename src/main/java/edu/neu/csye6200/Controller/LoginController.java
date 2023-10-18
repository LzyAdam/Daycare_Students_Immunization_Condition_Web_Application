package edu.neu.csye6200.Controller;

import edu.neu.csye6200.Model.DayCare;
import edu.neu.csye6200.View.LoginView;
import edu.neu.csye6200.View.StartScreen;

import javax.swing.*;
import java.awt.*;

public class LoginController
{
    // Credentials stored in "resources/credentials"

    private final DayCare dayCare;
    private final LoginView loginView;

    public LoginController(DayCare dayCare, LoginView loginView)
    {
        this.dayCare = dayCare;
        this.loginView = loginView;
    }

    public void validateLogin(String username, String password)
    {
        if (dayCare.validateUser(username, password))
        {
            JPanel startScreen = new StartScreen(dayCare);

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(loginView);
            topFrame.setSize(800, 600);
            topFrame.setLocationRelativeTo(null);
            topFrame.setResizable(true);

            loginView.getParent().add("StartScreen", startScreen);
            CardLayout layout = (CardLayout) loginView.getParent().getLayout();
            layout.next(loginView.getParent());
        }
        else
        {
            JOptionPane.showMessageDialog(loginView,
                    "username/password incorrect!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
