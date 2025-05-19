package com.example.juego;

import java.io.Serializable;

public class Desafio implements Serializable {
    private int id;
    private String titulo;
    private String codigoIncompleto;
    private String respuesta;
    private String pista;
    private int dificultad;
    private boolean completado;

    public Desafio() {
    }

    public Desafio(int id, String titulo, String codigoIncompleto, String respuesta, String pista, int dificultad) {
        this.id = id;
        this.titulo = titulo;
        this.codigoIncompleto = codigoIncompleto;
        this.respuesta = respuesta;
        this.pista = pista;
        this.dificultad = dificultad;
        this.completado = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCodigoIncompleto() { return codigoIncompleto; }
    public void setCodigoIncompleto(String codigoIncompleto) { this.codigoIncompleto = codigoIncompleto; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public String getPista() { return pista; }
    public void setPista(String pista) { this.pista = pista; }

    public int getDificultad() { return dificultad; }
    public void setDificultad(int dificultad) { this.dificultad = dificultad; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}