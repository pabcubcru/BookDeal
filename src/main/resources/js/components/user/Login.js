import React, { Component } from 'react';
import { Link } from "react-router-dom";


export default class Login extends Component {

    render() {
      return (
        <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
        backgroundSize: "cover" , padding: "50px", fontWeight: "bold", marginLeft: "-100"}}>
          <h1 style={{color: "#007bff"}}>Iniciar sesión</h1>
          <br></br>
          <form method="post" actiom="/login">
            <div class="form-group row">
              <label for="username" class="col-sm-2 col-form-label">Nombre de usuario</label>
              <div class="col-sm-10">
                <input type="text" class="form-control" id= "username" name="username"/>
              </div>
            </div>
    
            <div class="form-group row">
              <label for="password" class="col-sm-2 col-form-label">Contraseña</label>
              <div class="col-sm-10">
                <input type="password" class="form-control" id= "password" name="password"/>
              </div>
            </div>
    
            <div class="form-group row">
              <div class="col-sm-6">
                <button class="btn btn-primary" type="submit">Acceder</button>
              </div>
            </div>
            </form>
            <hr/>
            <p>¿No estás registrado? <Link to="/register">Registrarme</Link></p>


          
        </div>
        
      )
    }
  }
  