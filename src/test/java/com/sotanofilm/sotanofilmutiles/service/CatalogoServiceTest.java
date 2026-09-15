package com.sotanofilm.sotanofilmutiles.service;

import com.sotanofilm.sotanofilmutiles.services.CatalogoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;

@SpringBootTest
class CatalogoServiceTest {

    @Autowired
    private CatalogoService catalogoService;

    @Test
    void crearArbolArchivosCatalogoTest_OK () throws IOException {
        //Arrange
        //Act
        Path resultado = this.catalogoService.crearArbolArchivosCatalogo();
        //Assert
        Assertions.assertNotNull(resultado);
    }
}
