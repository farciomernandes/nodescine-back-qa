package com.cine.sk.cinesk.infrastructure.exception;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resposta de erro padronizada")
public class ErrorResponse {

    @Schema(description = "Timestamp do erro", example = "2025-10-01T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de status HTTP", example = "500")
    private int status;

    @Schema(description = "Tipo de erro", example = "Erro Interno do Servidor")
    private String error;

    @Schema(description = "Mensagem de erro", example = "Detalhes do erro")
    private String message;

    @Schema(description = "Caminho da requisição", example = "uri=/api/endpoint")
    private String path;

    @Schema(description = "Causa raiz do erro", example = "Causa específica")
    private String cause;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters e Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
