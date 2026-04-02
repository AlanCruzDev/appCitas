package com.app.citas.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u where u.activo =true and u.rol ='DUENO' and u.id =:id")
    public Usuario encontrarDuenosById(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u where u.activo =true and u.recibeCitas = true  AND u.rol = 'EMPLEADO' and  u.negocio.idNegocio =:id")
    public List<Usuario> encontrarEmpleadosBySucursal(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u where u.activo =true and u.recibeCitas = true  AND u.rol = 'EMPLEADO' and u.id =:idempleado")
    public Usuario empleadoById(@Param("idempleado") Long idempleado);

    @Query("SELECT u FROM Usuario u where u.activo and u.telefono =:telefono")
    public Usuario usuarioByTelefono(@Param("telefono") String telefono);

}
