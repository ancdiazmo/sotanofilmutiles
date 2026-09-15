package com.sotanofilm.sotanofilmutiles.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Value("${sotanofilm.carpeta.catalogo}")
    private String carpetaCatalogo;
    @Value("${sotanofilm.nombre.archivo.catalogo}")
    private String nombreArchivoArbolCatalogo;

    private static int numeroDeEjecucion = 0;
    private static final String PELICULA = "";

    public CatalogoServiceImpl () {

    }

    @Override
    public Resource verPelicula (String pelicula) {
        this.contarEjecuciones();
        Resource videoResource = null;
        Path filePath = Paths.get(carpetaCatalogo + "/" + pelicula);

        if (!Files.exists(filePath))
            return videoResource;

        videoResource = new FileSystemResource(filePath);
        return videoResource;
    }

    @Override
    public List<Path> catalogo() {
        List<Path> resultado = new ArrayList<>();
        try {
            Path rutaCatalogo = Paths.get(this.carpetaCatalogo);
            resultado = Files.walk(rutaCatalogo).collect(Collectors.toList());
            resultado = resultado.stream().filter(ruta -> ruta.toString().contains(".mp4") ||
                    ruta.toString().contains(".mkv")).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Error al tratar de obtener el catalogo metodo catalogo, clase ReproductorVideoServiceImpl");
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public Path crearArbolArchivosCatalogo () throws IOException {
        Path rutaPath = Paths.get(carpetaCatalogo);
        this.validateDirectory(rutaPath);
        List<String> tree = this.construirArbol(rutaPath);
        Path outputFile = rutaPath.resolve(carpetaCatalogo + nombreArchivoArbolCatalogo);
        Files.write(outputFile, tree, StandardCharsets.UTF_8);
        return outputFile;
    }

    private void contarEjecuciones () {
        log.info("{} - Ejecucion del endpoint..", numeroDeEjecucion);
        numeroDeEjecucion++;
    }

    private void validateDirectory (Path directorio) {
        if (directorio == null) {
            throw new IllegalArgumentException("La ruta no puede ser null.");
        }
        if (!Files.isDirectory(directorio)) {
            throw new IllegalArgumentException("La ruta indicada no corresponde a una carpeta.");
        }
    }

    private List<String> construirArbol(Path raiz) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add(raiz.getFileName().toString());
        agregarHijo(raiz, "", lineas);
        return lineas;
    }

    private void agregarHijo(Path directorio, String indentation, List<String> lineas) throws IOException {
        List<Path> hijos = obtenerHijosOrdenados(directorio);
        int indice = 0;
        for (Path hijo : hijos) {
            boolean esUltimo = indice == hijos.size() - 1;
            String conector = esUltimo ? "└── " : "├── ";
            lineas.add(indentation + conector + hijo.getFileName() + ", " + this.obtenerTamano(hijo));
            if (Files.isDirectory(hijo)) {
                String nextIndentation = indentation + (esUltimo ? "    " : "│   ");
                agregarHijo(hijo, nextIndentation, lineas);
            }
            indice++;
        }
    }

    private String obtenerTamano (Path path) {
        String resultado = "";
        try {
            String tamanoStr = Long.toString(Math.round(Files.size(path) / (1024.0 * 1024.0)));
            if (!"0".equals(tamanoStr))
                resultado = "Tamano: " + tamanoStr + " Mb";

        } catch (IOException e) { log.warn("No se pudo obtener el tamano del archivo.."); }
        return resultado;
    }

    private List<Path> obtenerHijosOrdenados(Path directorio) throws IOException {
        List<Path> hijos = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio)) {
            for (Path child : stream) {
                hijos.add(child);
            }
        }
        hijos.sort(Comparator.comparingInt(this::obtenerIdenficadorPelicula));
        return hijos;
    }

    /**
     * Este metodo recibe una ruta del tipo:
     * C:\SotanoFilm\SotanoFilmCatalogo\Catalogo-V2\7-HistoriasCruzadas
     * Devuelve el identicador de la carpeta para el ejemplo 7, utilizando el "-", como punto de referencia
     * */
    private Integer obtenerIdenficadorPelicula (Path path) {
        int resultado = 0;
        if (Files.isDirectory(path)) {
            String nombreCarpeta = path.getFileName().toString();
            int posicionGuion = nombreCarpeta.indexOf('-');
            if (posicionGuion != -1) { //Aca correji, un error de Gemini, (Posdata, los codigos
                // es mejor hacerlos yo, la IA, no programa tanto la verdad..., si mensionar que nos demoras mares,
                // para encontrar y solucionar un erro sensillo, luego termiamos perchando asquerosamente.)
                String resultadoStr = nombreCarpeta.substring(0, posicionGuion);
                resultado = Integer.parseInt(resultadoStr);
            }
        }
        return resultado;
    }
}
