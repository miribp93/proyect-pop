package com.guaguaupop.guaguaupop.exception;

public class NewUserWithDifferentPasswordsException extends RuntimeException{

    private static final long serialVersionUID = -7753753884371159222L;

    public NewUserWithDifferentPasswordsException(){
        super("Las contraseñas no coinciden");
    }
}
