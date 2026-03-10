package com.app.citas.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Negocio;

public interface NegocioRepository extends JpaRepository<Negocio,Long>{
    

    @Query("SELECT n FROM Negocio n where n.activo=true and n.idNegocio=:id")
    public Negocio encontrarNegocioById(@Param("id") Long id);

    @Query("SELECT n FROM Negocio n where n.telefono =:numero")
    public List<Negocio> encontrarNegocioByNumero(@Param("numero") String numero);
}
