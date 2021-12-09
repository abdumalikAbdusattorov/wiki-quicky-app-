package ssd.uz.wikiquickyapp.config;

import org.springframework.core.io.ClassPathResource;

import javax.swing.*;
import java.util.Properties;

public class InitConfig {

    public static boolean isStart() {
        Properties props = new Properties();
        try {
            props.load(new ClassPathResource("/application.properties").getInputStream());
            if (props.getProperty("spring.jpa.hibernate.ddl-auto").equals("update")) {
                return true;
            } else {
                String confirm = JOptionPane.showInputDialog("Xayolingni joyiga qo'y malumotlar o'chib ketishi mumkin   parol: (koinot) :");
                if (confirm != null && confirm.equals("koinot")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
