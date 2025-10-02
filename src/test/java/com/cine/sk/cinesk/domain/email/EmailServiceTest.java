package com.cine.sk.cinesk.domain.email;

import com.cine.sk.cinesk.domain.email.dto.EmailRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendSimpleEmail() {
        // Este teste pode ser executado manualmente para verificar o envio
        // Descomente a linha abaixo e adicione um email válido para testar
        // emailService.sendEmail("seu-email@teste.com", "Teste CineSK", "Este é um email de teste do serviço CineSK!");
        
        System.out.println("Teste configurado. Para testar o envio real, descomente a linha do sendEmail e adicione um email válido.");
    }

    @Test 
    void testSendTransactionConfirmationEmail() {
        // Este teste pode ser executado manualmente para verificar o envio
        // Descomente a linha abaixo e adicione um email válido para testar
        // emailService.sendTransactionConfirmationEmail("seu-email@teste.com", "João Silva", "TXN-12345");
        
        System.out.println("Teste de confirmação de transação configurado.");
    }

    @Test
    void testSendTransactionCompletedEmail() {
        // Este teste pode ser executado manualmente para verificar o envio
        // Descomente a linha abaixo e adicione um email válido para testar
        // emailService.sendTransactionCompletedEmail("seu-email@teste.com", "João Silva", "TXN-12345");
        
        System.out.println("Teste de transação completa configurado.");
    }
}