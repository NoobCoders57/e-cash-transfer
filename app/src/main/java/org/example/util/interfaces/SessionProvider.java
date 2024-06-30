package org.example.util.interfaces;

import javax.mail.Authenticator;
import javax.mail.Session;
import java.util.Properties;

public interface SessionProvider {
    Session getSession(Properties properties, Authenticator authenticator);
}
