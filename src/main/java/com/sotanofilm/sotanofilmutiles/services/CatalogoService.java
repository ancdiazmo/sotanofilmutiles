package com.sotanofilm.sotanofilmutiles.services;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface CatalogoService {
    Resource verPelicula(String pelicula);
    List<Path> catalogo();
    Path crearArbolArchivosCatalogo() throws IOException;
}
