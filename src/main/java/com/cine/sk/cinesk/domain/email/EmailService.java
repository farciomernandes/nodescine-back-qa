package com.cine.sk.cinesk.domain.email;

import com.cine.sk.cinesk.domain.email.dto.EmailRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;
    
    @Value("${email.retry.max-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${email.retry.delay-ms:1000}")
    private long retryDelayMs;

    public void sendEmail(EmailRequestDTO emailRequest) {
        sendEmail(emailRequest.emailTo(), emailRequest.subject(), emailRequest.text());
    }

    public void sendEmail(String emailTo, String subject, String text) {
        int attempt = 1;
        boolean sent = false;

        while (attempt <= maxRetryAttempts && !sent) {
            try {
                log.info("Tentativa {} de {}: Enviando email para {} com assunto: {}", 
                        attempt, maxRetryAttempts, emailTo, subject);
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(emailFrom);
                message.setTo(emailTo);
                message.setSubject(subject);
                message.setText(text);
                
                emailSender.send(message);
                
                sent = true;
                log.info("Email enviado com sucesso para: {} na tentativa {}", emailTo, attempt);
                
            } catch (MailException e) {
                log.warn("Erro na tentativa {} de {} ao enviar email para {}: {}", 
                        attempt, maxRetryAttempts, emailTo, e.getMessage());
                
                if (attempt == maxRetryAttempts) {
                    log.error("Falha ao enviar email para {} após {} tentativas. Último erro: {}", 
                            emailTo, maxRetryAttempts, e.getMessage());
                } else {
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Thread interrompida durante retry delay");
                        break;
                    }
                }
                
                attempt++;
            }
        }
    }

    public void sendTransactionConfirmationEmail(String emailTo, String customerName, String transactionId) {
        String subject = "Confirmação de Transação - Nordescine";
        String text = String.format(
                "Olá %s,\n\n" +
                "Sua transação foi processada com sucesso!\n\n" +
                "ID da Transação: %s\n\n" +
                "Obrigado por escolher o Nordescine!\n\n" +
                "Atenciosamente,\n" +
                "Equipe Nordescine",
                customerName, transactionId
        );
        
        sendEmail(emailTo, subject, text);
    }

    public void sendTransactionCompletedEmail(String emailTo, String customerName, String transactionId) {
        String subject = "Transação Finalizada - Nordescine";
        String text = String.format(
                "Olá %s,\n\n" +
                "Sua transação foi finalizada com sucesso!\n\n" +
                "ID da Transação: %s\n\n" +
                "Aproveite nossos serviços!\n\n" +
                "Atenciosamente,\n" +
                "Equipe Nordescine",
                customerName, transactionId
        );
        
        sendEmail(emailTo, subject, text);
    }
}