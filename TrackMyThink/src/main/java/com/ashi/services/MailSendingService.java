package com.ashi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class MailSendingService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;
    
    public void sendContactMail(String fromEmail, String name, String messageContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(adminEmail);
            helper.setSubject("TrackMyThink • New Contact Message");
            helper.setFrom(adminEmail);

            String htmlContent = """
                <html>
                <body style="margin:0; padding:0; background-color:#f4f4f4; font-family:Segoe UI, sans-serif;">
                    <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="max-width:600px; margin:auto; background-color:#ffffff; border:1px solid #e0e0e0; border-radius:8px;">
                        <tr>
                            <td style="background-color:#1a73e8; padding:20px; text-align:center; color:#ffffff; font-size:24px; border-top-left-radius:8px; border-top-right-radius:8px;">
                                📬 TrackMyThink
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:30px;">
                                <h2 style="margin-top:0; color:#333333;">New Contact Form Submission</h2>
                                <p style="margin-bottom:10px;"><strong>Name:</strong> %s</p>
                                <p style="margin-bottom:10px;"><strong>Email:</strong> %s</p>
                                <p style="margin:20px 0; padding:15px; background-color:#f9f9f9; border-left:4px solid #1a73e8; font-style:italic;">
                                    %s
                                </p>
                                <p style="font-size:14px; color:#666;">This message was sent from the TrackMyThink website contact form.</p>
                            </td>
                        </tr>
                        <tr>
                            <td style="background-color:#f1f1f1; text-align:center; padding:15px; font-size:12px; color:#888; border-bottom-left-radius:8px; border-bottom-right-radius:8px;">
                                &copy; 2025 TrackMyThink. All rights reserved.
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(name, fromEmail, messageContent); // <-- Using String.format-style interpolation

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void sendRegisterSuccessMail(String toEmail, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("TrackMyThink • Registration Successful");
            helper.setFrom(adminEmail);

            String htmlContent = """
                <html>
                <body style="margin:0; padding:0; background-color:#f4f4f4; font-family:Segoe UI, sans-serif;">
                    <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="max-width:600px; margin:auto; background-color:#ffffff; border:1px solid #e0e0e0; border-radius:8px;">
                        <tr>
                            <td style="background-color:#1a73e8; padding:20px; text-align:center; color:#ffffff; font-size:24px; border-top-left-radius:8px; border-top-right-radius:8px;">
                                ✅ TrackMyThink
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:30px;">
                                <h2 style="margin-top:0; color:#333333;">Registration Successful</h2>
                                <p style="font-size:16px; color:#333;">Hello <strong>%s</strong>,</p>
                                <p style="font-size:15px; color:#333;">Thank you for registering with <strong>TrackMyThink</strong>! Your account has been successfully created and you can now log in to start using our services.</p>
                                <p style="margin:20px 0; padding:15px; background-color:#f9f9f9; border-left:4px solid #1a73e8;">
                                    🚀 Start tracking, learning, and growing with TrackMyThink.
                                </p>
                                <p style="font-size:14px; color:#666;">If you did not register on TrackMyThink, please ignore this email.</p>
                            </td>
                        </tr>
                        <tr>
                            <td style="background-color:#f1f1f1; text-align:center; padding:15px; font-size:12px; color:#888; border-bottom-left-radius:8px; border-bottom-right-radius:8px;">
                                &copy; 2025 TrackMyThink. All rights reserved.
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(name);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
