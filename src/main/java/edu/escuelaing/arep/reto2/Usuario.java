package edu.escuelaing.arep.reto2;

public class Usuario{
    int id_Usuario;
    String correo;
    String nombre;
    String apellido;

    public Usuario(int id, String correo, String nombre, String apellido) {
        this.id_Usuario=id;
        this.correo=correo;
        this.nombre=nombre;
        this.apellido=apellido;
    }
    
    public int getId(){
        return this.id_Usuario;
    }

    public String getCorreo(){
        return this.correo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getApellido(){
        return this.apellido;
    }

    public void setId(int id){
         this.id_Usuario = id;
    }

    public void setCorreo(String correo){
        this.correo = correo;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setApellido(String apellido){
        this.apellido = apellido;
    }
}