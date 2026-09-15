package com.sotanofilm.sotanofilmutiles.controller;

import com.sotanofilm.sotanofilmutiles.services.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/CatalogoController")
public class CatalogoController { //Cambio de prueba

    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController (CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping(value = "/verPelicula", produces = "video/mp4")
    public Resource verPelicula(@RequestParam  String pelicula) {
        return this.catalogoService.verPelicula(pelicula);
    }

    @GetMapping(value = "/catalogo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Path> catalogo () {
        return this.catalogoService.catalogo();
    }

    /**@PostMapping(value = "/crearArbolArchivosCatalogo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String crearArbolArchivosCatalogo () throws IOException {
        String resultado = "";
        Path resultadoPath = this.catalogoService.crearArbolArchivosCatalogo();
        resultado = resultadoPath.toString();
        return resultado;
    }*/
}
