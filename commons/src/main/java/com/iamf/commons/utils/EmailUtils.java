package com.iamf.commons.utils;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    public static Boolean validarCorreo(String email) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public static Boolean hasMXRecord(String domain) {
        try {
            Record[] records = new Lookup(domain, Type.MX).run();
            return records != null && records.length > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isEmailDeliverable(String email) {
        String host = "smtp." + email.split("@")[1];
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.connectiontimeout", "10000"); // 10 segundos de tiempo de espera

        Session session = Session.getInstance(properties);

        try (Store store = session.getStore("smtp")) {
            store.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isValidEmail(String email) {
        if (!validarCorreo(email)) {
            return false;
        }

        String domain = email.split("@")[1];
        if (!hasMXRecord(domain)) {
            return false;
        }

        // La verificación SMTP es opcional
//         if (!isEmailDeliverable(email)) {
//             return false;
//         }

        return true;
    }

}
