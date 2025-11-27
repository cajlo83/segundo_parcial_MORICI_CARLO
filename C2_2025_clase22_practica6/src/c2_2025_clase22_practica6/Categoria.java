package c2_2025_clase22_practica6;

import java.io.Serializable;

public enum Categoria {
    HIGIENE("higiene"),
    SALUD("salud"),
    COMIDA("comida");

    private final String valor;

    private Categoria(String valor){
        this.valor = valor;
    }

    public String getValor(){
        return valor;
    }

    //buscar un enum por su valor de texto
    public static Categoria fromValor(String valor) {
        for (Categoria c : Categoria.values()) {
            if (c.valor.equalsIgnoreCase(valor)) {
                return c;
            }
        }
        return null;
    }

    // para facilidad con chiceBox
    @Override
    public String toString() {
        return valor;
    }
}
