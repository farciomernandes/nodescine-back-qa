package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Controller temporário apenas para testes de desenvolvimento.
 * Remover antes de colocar em produção.
 *
 * Rotas disponíveis:
 * POST /api/test/email?emailTo=seu-email@gmail.com
 * POST /api/test/email/transaction-confirmation?emailTo=seu-email@gmail.com&customerName=João Silva
 * POST /api/test/email/transaction-completed?emailTo=seu-email@gmail.com&customerName=João Silva
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @Value("${spring.mail.host:NOT_SET}")
    private String mailHost;

    @Value("${spring.mail.port:NOT_SET}")
    private String mailPort;

    @Value("${spring.mail.username:NOT_SET}")
    private String mailUsername;

    @Value("${spring.mail.password:NOT_SET}")
    private String mailPassword;

    @GetMapping("/")
    public String info() {
        return """
            Email Test Controller - Rotas disponíveis:
            
            1. GET /api/test/config - Verifica configurações de email
            2. POST /api/test/email?emailTo=seu-email@gmail.com
               - Envia um email de teste simples
            
            3. POST /api/test/email/transaction-confirmation?emailTo=seu-email@gmail.com&customerName=João Silva
               - Envia email de confirmação de transação
            
            4. POST /api/test/email/transaction-completed?emailTo=seu-email@gmail.com&customerName=João Silva
               - Envia email de transação completa
            
            Todas as rotas estão liberadas (sem autenticação necessária).
            """;
    }

    @GetMapping("/config")
    public String checkConfig() {
        return String.format("""
            === Configurações de Email ===
            
            ✅ Valores injetados corretamente:
            spring.mail.host: %s
            spring.mail.port: %s
            spring.mail.username: %s
            spring.mail.password: %s
            
            Status: Configurações carregadas com sucesso!
            Agora você pode testar o envio de email.
            """,
                mailHost,
                mailPort,
                mailUsername,
                mailPassword
        );
    }

    @PostMapping("/email")
    public String testEmail(@RequestParam String emailTo) {
        try {
            emailService.sendEmail(emailTo, "Teste CineSK - " + System.currentTimeMillis(),
                    "Este é um email de teste do serviço CineSK!\n\nEnviado em: " + new java.util.Date());
            return "✅ Email enviado com sucesso para: " + emailTo;
        } catch (Exception e) {
            return "❌ Erro ao enviar email: " + e.getMessage();
        }
    }

    @PostMapping("/email/transaction-confirmation")
    public String testTransactionConfirmation(@RequestParam String emailTo,
                                              @RequestParam(defaultValue = "João Silva") String customerName) {
        try {
            emailService.sendTransactionConfirmationEmail(emailTo, customerName, "TXN-TEST-" + System.currentTimeMillis());
            return "✅ Email de confirmação de transação enviado com sucesso para: " + emailTo;
        } catch (Exception e) {
            return "❌ Erro ao enviar email: " + e.getMessage();
        }
    }

    @PostMapping("/email/transaction-completed")
    public String testTransactionCompleted(@RequestParam String emailTo,
                                           @RequestParam(defaultValue = "João Silva") String customerName) {
        try {
            emailService.sendTransactionCompletedEmail(emailTo, customerName, "TXN-TEST-" + System.currentTimeMillis());
            return "✅ Email de transação completa enviado com sucesso para: " + emailTo;
        } catch (Exception e) {
            return "❌ Erro ao enviar email: " + e.getMessage();
        }
    }
}