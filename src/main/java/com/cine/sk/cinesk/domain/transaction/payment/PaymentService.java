package com.cine.sk.cinesk.domain.transaction.payment;

import com.cine.sk.cinesk.domain.transaction.payment.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final AsaasApiClient asaasApiClient;

    @Value("${ASAAS_API_KEY:test}")
    private String apiKey;

    private String OWNER_WALLET = "e0ec6a7b-eaad-4d43-a519-bb4211b0b41a";

    public ProcessPaymentResponse process(OrderDTO order, UserPaymentDTO user, PaymentDTO payment, AddressDTO address, String movieWallet) {
        try {
            String customerId = findOrCreateCustomer(user, address);
            AsaasPaymentResponse transactionResponse = createTransaction(customerId, order, user, payment, address, movieWallet);
            return buildProcessPaymentResponse(transactionResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falha ao processar pagamento com Asaas: " + e.getMessage(), e);
        }
    }

    private String findOrCreateCustomer(UserPaymentDTO user, AddressDTO address) {
        AsaasCustomerListResponse existingCustomers = asaasApiClient.findCustomerByEmail(apiKey, user.getEmail());

        if (existingCustomers != null && existingCustomers.getData() != null && !existingCustomers.getData().isEmpty()) {
            return existingCustomers.getData().get(0).getId();
        }

        AsaasCustomerResponse newCustomerData = new AsaasCustomerResponse();
        newCustomerData.setName(user.getName());
        newCustomerData.setEmail(user.getEmail());
        newCustomerData.setMobilePhone(user.getPhone());
        newCustomerData.setCpfCnpj(user.getCpf());

        AsaasCustomerResponse createdCustomer = asaasApiClient.createCustomer(apiKey, newCustomerData);
        return createdCustomer.getId();
    }

    private AsaasPaymentResponse createTransaction(String customerId, OrderDTO order, UserPaymentDTO user, PaymentDTO payment, AddressDTO address, String directorWallet) {
        AsaasPaymentRequest.AsaasPaymentRequestBuilder requestBuilder = AsaasPaymentRequest.builder()
                .customer(customerId)
                .billingType(payment.getMethod().name())
                .value(order.getTotal())
                .dueDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .description("CineSK - Pedido #" + order.getId())
                .externalReference(payment.getMethod().name() + " - " + order.getId());

        if (payment.getMethod() == PaymentMethodEnum.CREDIT_CARD) {
            String[] expiration = payment.getCreditCardExpiration().split("/");
            requestBuilder.creditCard(AsaasPaymentRequest.CreditCard.builder()
                    .holderName(payment.getCreditCardHolder())
                    .number(payment.getCreditCardNumber())
                    .expiryMonth(expiration[0])
                    .expiryYear(expiration[1])
                    .ccv(payment.getCreditCardSecurityCode())
                    .build());

            requestBuilder.creditCardHolderInfo(AsaasPaymentRequest.CreditCardHolderInfo.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .cpfCnpj(user.getCpf())
                    .postalCode(address.getZipCode())
                    .addressNumber(address.getNumber())
                    .addressComplement(address.getComplement())
                    .phone(user.getPhone())
                    .mobilePhone(user.getPhone())
                    .build());


            RequestSplit ownerSplit = RequestSplit.builder().walletId(OWNER_WALLET).percentualValue(80.0).build();
            RequestSplit directorSplit = RequestSplit.builder().walletId(directorWallet).percentualValue(20.0).build();
            List<RequestSplit> requestSplitList = new ArrayList<>();
            requestSplitList.add(ownerSplit);
            requestSplitList.add(directorSplit);
            requestBuilder.split(requestSplitList);
        }

        return asaasApiClient.createPayment(apiKey, requestBuilder.build());
    }

    public String createAccount(AsaasAccountRequest asaasAccount){
        AsaasAccountResponse asaasAccountResponse = asaasApiClient.createAccount(apiKey, asaasAccount);
        return asaasAccountResponse.getWalletId();
    }

    private ProcessPaymentResponse buildProcessPaymentResponse(AsaasPaymentResponse response) {
        Object transactionDetails;
        PaymentMethodEnum method = PaymentMethodEnum.valueOf(response.getBillingType());

        if (method == PaymentMethodEnum.PIX) {
            transactionDetails = PixTransactionDTO.builder()
                    .paymentInfo(buildBaseTransaction(response))
                    .encodedImage(response.getPixQrCode().getEncodedImage())
                    .payload(response.getPixQrCode().getPayload())
                    .build();
        } else if (method == PaymentMethodEnum.BOLETO) {
            transactionDetails = BoletoTransactionDTO.builder()
                    .paymentInfo(buildBaseTransaction(response))
                    .bankSlipUrl(response.getBankSlipUrl())
                    .build();
        } else {
            transactionDetails = CreditCardTransactionDTO.builder()
                    .paymentInfo(buildBaseTransaction(response))
                    .creditCard(CreditCardDTO.builder()
                            .creditCardNumber(response.getCreditCard().getCreditCardNumber())
                            .creditCardBrand(response.getCreditCard().getCreditCardBrand())
                            .creditCardToken(response.getCreditCard().getCreditCardToken())
                            .build())
                    .build();
        }

        return ProcessPaymentResponse.builder()
                .transactionId(response.getId())
                .status(OrderStatusEnum.PENDING)
                .transaction(transactionDetails)
                .build();
    }

    private BaseTransactionDTO buildBaseTransaction(AsaasPaymentResponse response) {
        return BaseTransactionDTO.builder()
                .id(response.getId())
                .value(response.getValue())
                .status(response.getStatus())
                .billingType(response.getBillingType())
                .externalReference(response.getExternalReference())
                .build();
    }
}