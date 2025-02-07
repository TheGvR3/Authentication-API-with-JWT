package com.thcsOauth2.AutenticatorDemo.dto;

public class UpdateRequest {
    private String nome;
    private String cognome;
    private String password;

    public UpdateRequest() {}

    public UpdateRequest(String nome, String cognome, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 