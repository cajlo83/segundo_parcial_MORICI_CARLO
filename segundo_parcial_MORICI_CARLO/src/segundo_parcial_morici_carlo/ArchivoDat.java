package segundo_parcial_morici_carlo;

import java.io.*;

public class ArchivoDat {

    private final String ruta;

    public ArchivoDat(String ruta) {
        this.ruta = ruta;
    }

    //para guardar 
    //LO
    //QUE
    //SEAAAAAA (mientras sea serializable)
    //benditos comodines
    public void guardar(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta));
        oos.writeObject(obj);
        oos.close();
    }

    //para cargar 
    //LO
    //QUE
    //SEAAAAAA
    //benditos comodines
    public Object cargar() throws IOException, ClassNotFoundException {
        File f = new File(ruta);

        System.out.println("Buscando archivo en: " + f.getAbsolutePath());

        if (!f.exists()) {
            return null;
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}
