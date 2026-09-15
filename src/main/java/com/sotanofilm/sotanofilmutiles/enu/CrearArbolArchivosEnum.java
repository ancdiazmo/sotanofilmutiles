package com.sotanofilm.sotanofilmutiles.enu;

public enum CrearArbolArchivosEnum {

    TIPO_CATALOGO("catalogo"),
    TIPO_BACKUP_CATALOGO("backupCatalogo");

    private String valor;

    private CrearArbolArchivosEnum (String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
