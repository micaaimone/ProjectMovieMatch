package com.example.demo.model.exceptions.ConfigExceptions;

import com.example.demo.model.exceptions.ContenidoExceptions.*;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ----esquema de como funciona
    Controller llama al Service → Service lanza una excepción
                                ↓
    GlobalExceptionHandler detecta y captura esa excepción
                                ↓
    Retorna una ResponseEntity con el status y mensaje que definiste
*/

    //error cuando no encuentra el contenido
    @ExceptionHandler(ContenidoNotFound.class)
    public ResponseEntity<ErrorDetalles> handleContenidoNotFound(ContenidoNotFound ex){
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    //error cuando no encuentra serie
    @ExceptionHandler(SerieNotFound.class)
    public ResponseEntity<ErrorDetalles> handleSerieNotFound(SerieNotFound ex){
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);    }

    //error cuando no encuentra serie
    @ExceptionHandler(PeliculaNotFound.class)
    public ResponseEntity<ErrorDetalles> handlePeliculaNotFound(PeliculaNotFound ex){
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);    }

    //error por entrada invalida
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetalles> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //error por reseña existente
    @ExceptionHandler(ReseniaAlredyExists.class)
    public ResponseEntity<ErrorDetalles> handleReseñaAlredyExistsException(ReseniaAlredyExists ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //error por reseña inexistente
    @ExceptionHandler(ReseniaNotFound.class)
    public ResponseEntity<ErrorDetalles> handleReseñaNotFoundException(ReseniaNotFound ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<String> manejarUsuarioYaExiste(UsuarioYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // para valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarValidaciones(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body("Error de validación: " + mensaje);
    }

}
