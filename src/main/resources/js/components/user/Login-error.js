import React, { Component } from 'react';
import { Link } from "react-router-dom";

export default class Login extends Component {
    
  
    render() {
      return (
        <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
        backgroundSize: "cover" , padding: "50px", borderRadius: '5px', fontWeight: "bold", marginLeft: "-100"}}>
          <h1 style={{color: "#007bff"}}>Iniciar sesión</h1>
          <br></br>
          <h4 class='text-danger'>*Usuario o contraseña incorrectos. <Link to="/login">Intentar de nuevo</Link></h4>  
            <hr/>
            <p>¿No estás registrado? <Link to="/register">Registrarme</Link></p>
        </div>
      )
    }
  }
  