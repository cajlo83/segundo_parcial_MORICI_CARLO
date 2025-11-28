package segundo_parcial_morici_carlo;

import java.io.*;

public class ArchivoTxt {

    private final String ruta;

    public ArchivoTxt(String ruta) {
        this.ruta = ruta;
    }

    // Guarda una cadena de texto en el archivo, sobreescribe si existe
    public void guardar(String texto) throws IOException {
        FileWriter fw = new FileWriter(ruta);   //  sobreescribe
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(texto);
        bw.close();
    }

    // leer todo el contenido como string
    public String cargar() throws IOException {
        File f = new File(ruta);
        if (!f.exists()) {
            return null; // a veces conviene cambiar a ""
        }

        BufferedReader br = new BufferedReader(new FileReader(ruta));
        StringBuilder sb = new StringBuilder();
        String linea;

        while ((linea = br.readLine()) != null) {
            sb.append(linea).append("\n");
        }

        br.close();
        return sb.toString();
    }
}
