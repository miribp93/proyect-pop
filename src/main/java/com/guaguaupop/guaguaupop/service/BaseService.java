package com.guaguaupop.guaguaupop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public abstract class BaseService <T, ID, R extends JpaRepository<T, ID>>{

    @Autowired
    protected R repository;

    public T save(T t){
        return repository.save(t);
    }

    //La interfaz Optional se usa para manejar posibles valores null.
    public Optional<T> findById(ID id){
        return repository.findById(id);
    }

    //Devuelve una lista completa de todos los elementos de la bbdd
    public List<T> findAll(){
        return repository.findAll();
    }

    /*Devuelve una página de elementos.
    Pageable permite definir el número de elementos por página, la página actual y el ordenamiento.
    Manejar grandes conjuntos de datos, cargando solo una parte a la vez y mejorando el rendimiento.*/
    public Page<T> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    /*Borra el objeto específico t que se le pasa como
    parámetro cuando ya tienes una instancia del objeto*/
    public void delete(T t){
        repository.delete(t);
    }

    /*Borra el objeto basado en su id. Cuando tienes el ID y no la instancia del objeto*/
    public void deleteById(ID id){
        repository.deleteById(id);
    }
}
