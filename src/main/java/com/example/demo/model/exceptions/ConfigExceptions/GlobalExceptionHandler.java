package com.example.demo.model.exceptions.ConfigExceptions;

import com.example.demo.Seguridad.DTO.AuthErrorDTO;
import com.example.demo.model.exceptions.AmistadExceptions.UsuariosNoSonAmigos;
import com.example.demo.model.exceptions.ContenidoExceptions.*;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoYaAgregadoException;
import com.example.demo.model.exceptions.LikeExceptions.LikeAlreadyExistsException;
import com.example.demo.model.exceptions.AmistadExceptions.SolicitudAlreadyExistsException;
import com.example.demo.model.exceptions.AmistadExceptions.SolicitudNotFound;
import com.example.demo.model.exceptions.ListasExceptions.ListAlreadyExistsException;
import com.example.demo.model.exceptions.ListasExceptions.ListaNotFoundException;
import com.example.demo.model.exceptions.SuscripcionException.*;
import com.example.demo.model.exceptions.UsuarioExceptions.EstadoInvalidoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEsAdminException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

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
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    //error cuando no encuentra serie
    @ExceptionHandler(PeliculaNotFound.class)
    public ResponseEntity<ErrorDetalles> handlePeliculaNotFound(PeliculaNotFound ex){
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    //error para cuando el contenido ya fue dado de alta
    @ExceptionHandler(ContenidoYaAgregadoException.class)
    public ResponseEntity<ErrorDetalles> handlerYaAgregado(ContenidoYaAgregadoException ex)
    {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //error por body invalido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetalles> handleInvalidBody(HttpMessageNotReadableException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //error por entrada invalida
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetalles> handleIllegalArgumentException(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    //error por reseña existente
    @ExceptionHandler(ReseniaAlredyExists.class)
    public ResponseEntity<ErrorDetalles> handleReseñaAlredyExistsException(ReseniaAlredyExists ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    //error por reseña inexistente
    @ExceptionHandler(ReseniaNotFound.class)
    public ResponseEntity<ErrorDetalles> handleReseñaNotFoundException(ReseniaNotFound ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }


    //usuario no encontrado
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorDetalles> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    //usuario ya existe
    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorDetalles> manejarUsuarioYaExiste(UsuarioYaExisteException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    // para valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetalles> manejarValidaciones(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                "Error de validación: " + mensaje,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    //listas--------------------------------------

    @ExceptionHandler(ListaNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarListaNotFound(ListaNotFoundException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ListAlreadyExistsException.class)
    public ResponseEntity<ErrorDetalles> manejarListaAlreadyExists(ListAlreadyExistsException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }




    //suscripciones------------------

    //sub no encontrada
    @ExceptionHandler(SubNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarSubNotFound(SubNotFoundException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    //sub ya existente
    @ExceptionHandler(SubAlreadyExistException.class)
    public ResponseEntity<ErrorDetalles> manejarSubAlreadyExist(SubAlreadyExistException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //plan no encontrado
    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarPlanNotFound(PlanNotFoundException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    //oferta no encontrada
    @ExceptionHandler(OfertaNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarOfertaNotFound(OfertaNotFoundException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PagoNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarPagoNotFound(PagoNotFoundException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthErrorDTO> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request
    ) {
        AuthErrorDTO errorDTO = new AuthErrorDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AuthErrorDTO> handleUsernameNotFound(
            UsernameNotFoundException ex,
            WebRequest request
    ) {
        AuthErrorDTO errorDTO = new AuthErrorDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }


    //solicitudes------------------


    //para cuando una solicitud ya exista
    @ExceptionHandler(SolicitudAlreadyExistsException.class)
    public ResponseEntity<ErrorDetalles> solicitudAlreadyExists(SolicitudAlreadyExistsException ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    //para cuando una solicitud no exista
    @ExceptionHandler(SolicitudNotFound.class)
    public ResponseEntity<ErrorDetalles> solicitudNotFound(SolicitudNotFound ex) {
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    // like
    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<ErrorDetalles> handlerLikeAlreadyExists(LikeAlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    //amigos y grupos
    @ExceptionHandler(UsuariosNoSonAmigos.class)
    public ResponseEntity<ErrorDetalles> handlerUsuariosNoSonAmigos(UsuariosNoSonAmigos ex)
    {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    @ExceptionHandler(UsuarioNoEsAdminException.class)
    public ResponseEntity<ErrorDetalles> handlerUsuariosNoSonAmigos(UsuarioNoEsAdminException ex)
    {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ResponseEntity<ErrorDetalles> handlerEstadoInvalidoException(EstadoInvalidoException ex)
    {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDetalles errorDetalles = new ErrorDetalles(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetalles, status);
    }



}
