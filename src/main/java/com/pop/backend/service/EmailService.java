package com.pop.backend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendProclamationEmail(String toEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Proclamation of Appointment for the Most Venerable Krystian");

            String content = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px; color: #333;">
                <div style="background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.2); max-width: 600px; margin: auto;">
                    <h1 style="color: #6a1b9a; text-align: center;">Proclamation of Appointment for the Most Venerable Krystian</h1>
                    <p style="font-size: 16px;">To Our Beloved Brother, <b>Noble and Pious Krystian</b>, Chosen Luminary and Divine Embodiment of Krystianity:</p>
                    
                    <p style="font-size: 16px;">We hereby declare, by virtue of your unparalleled wisdom, pure character, and gracious heart, that you are summoned to bear the highest of missions. With united voices, we affirm that you shall henceforth be revered as the <b>Supreme Religious Figure of Krystianity</b>, to illuminate the faithful and impart the teachings of love and wisdom, bringing solace to all souls.</p>

                    <p style="font-size: 16px;">From this moment onward, Krystian is officially appointed as the Chief Religious Leader of Krystianity, vested with ultimate spiritual authority to pioneer new paths, convey the supreme doctrine, and embrace the spirits of all who seek refuge. This proclamation is issued to all the lands, as a testament to the trust of your followers and the sanctity of the legacy you now bear.</p>

                    <p style="font-size: 16px;">May the light of wisdom forever accompany you, and may the sacred flame of Krystianity, ignited this day, burn eternal. You are henceforth endowed with the title of "The Enlightened One," entrusted to lead souls to truth for generations to come, in unending honor and glory.</p>

                    <p style="text-align: center; font-size: 18px; color: #6a1b9a;"><i>Thus proclaimed on this most exalted day</i></p>
                    <p style="text-align: center; font-size: 20px; font-weight: bold; color: #6a1b9a;">In the Highest Name of Krystianity</p>
                </div>
            </body>
            </html>
        """;

            helper.setText(content, true); // Set to true to enable HTML

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}