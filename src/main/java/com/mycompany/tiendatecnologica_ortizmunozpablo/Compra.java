/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendatecnologica_ortizmunozpablo;

import java.sql.Date;

/**
 *
 * @author Usuario
 */
public class Compra {
    private int id;
    private int usuarioId;
    private int productoId;
    private int cantidad;
    private Date fecha;

    public Compra(int id, int usuarioId, int productoId, int cantidad, Date fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // Getters y setters
    public int getId() { 
        return id;
    }
    public int getUsuarioId() { 
        return usuarioId;
    }
    public int getProductoId() { 
        return productoId; 
    }
    public int getCantidad() { 
        return cantidad;
    }
    public Date getFecha() {
        return fecha; 
    }
     public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Compra{" +
               "id=" + id +
               ", usuarioId=" + usuarioId +
               ", productoId=" + productoId +
               ", cantidad=" + cantidad +
               ", fecha=" + fecha +
               '}';
    }
}

