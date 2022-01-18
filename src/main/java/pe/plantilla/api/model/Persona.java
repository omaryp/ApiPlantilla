package pe.plantilla.api.model;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Persona extends BasicEntity {
	
	@NotBlank(message="Atributo nombres no debe ser vacío.")
	@NotNull(message="Atributo nombres no debe ser vacío.")
	private String nombres;
	
	@NotBlank(message="Atributo apellidos no debe ser vacío.")
	@NotNull(message="Atributo apellidos no debe ser nulo.")
	private String apellidos;
	
	@NotBlank(message="Atributo telefono no debe ser vacío.")
	@NotNull(message="Atributo telefono no debe ser nulo.")
	private String telefono;
	
	@NotBlank(message="Atributo correo no debe ser vacío.")
	@NotNull(message="Atributo correo no debe ser nulo.")
	@Email(message="Atributo correo debe ser válido")
	private String correo;
	
	@NotBlank(message="Atributo direccion no debe ser vacío.")
	@NotNull(message="Atributo direccion no debe ser nulo.")
	private String direccion;
	
	private Date fecha_nacimiento;
	
	@NotBlank(message="Atributo número de documento no debe ser vacío.")
	@NotNull(message="Atributo número de documento no debe ser nulo.")
	@Digits(fraction = 0, integer = 8, message="Atributo número de documento debe contener solo números")
	@Size(min = 8, max = 8,message="Atributo número de documento no es válido")
	private String dni;

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
}
