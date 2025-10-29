package com.cine.sk.cinesk.domain.transaction.webhook;

import com.cine.sk.cinesk.domain.transaction.Transaction;
import com.cine.sk.cinesk.domain.transaction.TransactionService;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final TransactionService transactionService;

    public void processWebhookPayload(String event, String id) {
        Transaction transaction = transactionService.findByTransactionId(id);
        switch (event) { //TODO: PAGAMENTO === COBRANCA
            case "PAYMENT_REFUNDED":
                transaction.setStatus(OrderStatusEnum.CANCELED);
                transactionService.save(transaction);
                break;
            case "PAYMENT_RECEIVED": //TODO: PAGAMENTO RECEBIDO
                transaction.setStatus(OrderStatusEnum.PAID);
                transactionService.save(transaction);
                break;
            case "PAYMENT_OVERDUE:": //TODO: PAGAMENTO VENCIDO
                transaction.setStatus(OrderStatusEnum.CANCELED);
                transactionService.save(transaction);
                break;
            case "PAYMENT_DELETED":  //TODO: PAGAMENTO CANCELADO
                transaction.setStatus(OrderStatusEnum.CANCELED);
                transactionService.save(transaction);
                break;
            case "PAYMENT_CREATED":
                transaction.setStatus(OrderStatusEnum.PENDING);
                transactionService.save(transaction);
                break;
            default:
                System.out.println("Este evento não é aceito " + event);
        }


    }
}
