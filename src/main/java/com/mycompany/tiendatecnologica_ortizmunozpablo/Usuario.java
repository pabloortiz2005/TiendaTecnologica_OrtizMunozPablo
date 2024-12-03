/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendatecnologica_ortizmunozpablo;

/**
 *
 * @author Usuario
 */
public class Usuario {
    private String nombre;
    private int id;
    private String email;
    private String calle;
    private String numero;
    private String ciudad;
    private String pais;

    // Constructor
    public Usuario(String nombre, int id, String email, String calle, String numero, String ciudad, String pais) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters y setters
    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public String getEmail() { 
        return email;
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getCalle() { 
        return calle; 
    }
    public void setCalle(String calle) { 
        this.calle = calle;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) { 
        this.numero = numero;
    }
    public String getCiudad() { 
        return ciudad;
    }
    public void setCiudad(String ciudad) { 
        this.ciudad = ciudad;
    }
    public String getPais() { 
        return pais;
    }
    public void setPais(String pais) { 
        this.pais = pais; 
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "nombre='" + nombre + '\'' +
               ", id=" + id +
               ", email='" + email + '\'' +
               ", calle='" + calle + '\'' +
               ", numero='" + numero + '\'' +
               ", ciudad='" + ciudad + '\'' +
               ", pais='" + pais + '\'' +
               '}';
    }
}

