package com.palmaactiva.jaminas.io;

import com.palmaactiva.jaminas.lib.JuegoMinas;
import com.palmaactiva.jaminas.lib.MejoresPuntuaciones;
import com.palmaactiva.jaminas.lib.Puntuacion;
import com.palmaactiva.jaminas.lib.models.JuegoMinasSerializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class Datos implements ProveedorDatos {

    public static final String CARPETA = "jaminas";
    public static final String EXTENSION_PUNTUACIONES = "jmx";
    public static final String EXTENSION_JUEGO = "jmg";
    private static Datos INSTANCIA;

    public static Datos getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new Datos();
        }
        return INSTANCIA;
    }

    protected static String getArchivoPuntuaciones() {
        return getDatosDir() + "/puntuaciones." + EXTENSION_PUNTUACIONES;
    }

    protected static String getDatosDir() {
        String path;
        switch (getSistemaOperativo()) {
            case WINDOWS:
                path = System.getenv("APPDATA");
                path = Paths.get(path, CARPETA).toString();
                break;
            default:
                path = System.getProperty("user.home");
                path = Paths.get(path, "." + CARPETA).toString();
        }
        return path;
    }

    private static void comprobarDirectorioDatos() {
        try {
            String datosDir = getDatosDir();
            if (!Files.exists(Paths.get(datosDir))) {
                Files.createDirectories(Paths.get(datosDir));
            }
        } catch (IOException ex) {
            Logger.getLogger(MejoresPuntuaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SistemaOperativo getSistemaOperativo() {
        String nombreSO = System.getProperty("os.name").toLowerCase();
        if (nombreSO.contains("win")) {
            return SistemaOperativo.WINDOWS;
        } else if (nombreSO.contains("mac")) {
            return SistemaOperativo.MACOS;
        } else {
            return SistemaOperativo.LINUX;
        }
    }

    private static String comprobarExtension(String archivo, String extension) {
        int posPunto = archivo.lastIndexOf(".");
        if (posPunto < 0) {
            return archivo + "." + extension;
        }
        return archivo.substring(0, posPunto) + "." + extension;
    }

    private Datos() {

    }

    @Override
    public List<Puntuacion> cargarPuntuaciones() {
        Datos.comprobarDirectorioDatos();
        try (ObjectInputStream lector = new ObjectInputStream(new FileInputStream(Datos.getArchivoPuntuaciones()))) {
            Puntuacion[] puntuacionesGuardadas = (Puntuacion[]) lector.readObject();
            if (puntuacionesGuardadas != null && puntuacionesGuardadas.length > 0) {
                return Arrays.asList(puntuacionesGuardadas);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MejoresPuntuaciones.class.getName()).log(Level.INFO, "No se han podido cargar mejores puntuaciones.");
        }
        return null;
    }

    @Override
    public void guardarPuntuaciones(Collection<Puntuacion> puntuaciones) {
        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(Datos.getArchivoPuntuaciones()))) {
            Puntuacion[] arrayPuntuaciones = puntuaciones.toArray(new Puntuacion[puntuaciones.size()]);
            escritor.writeObject(arrayPuntuaciones);
        } catch (IOException ex) {
            Logger.getLogger(MejoresPuntuaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guardarPartida(JuegoMinas juegoMinas, String archivo) {
        archivo = Datos.comprobarExtension(archivo, EXTENSION_JUEGO);
        JuegoMinasSerializable partidaGuardable = new JuegoMinasSerializable(juegoMinas);
        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo))) {
            escritor.writeObject(partidaGuardable);
        } catch (IOException ex) {
            Logger.getLogger(Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JuegoMinas cargarPartida(String archivo) {
        try (ObjectInputStream lector = new ObjectInputStream(new FileInputStream(archivo))) {
            JuegoMinasSerializable juegoCargado = (JuegoMinasSerializable) lector.readObject();
            return JuegoMinas.fromSerializable(juegoCargado);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Datos.class.getName()).log(Level.INFO, "No se han podido cargar la partida.");
        }
        return null;
    }

    public enum SistemaOperativo {
        LINUX,
        WINDOWS,
        MACOS
    }
}
