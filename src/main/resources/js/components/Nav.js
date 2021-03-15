import React, { Component } from 'react'; 
import { Link } from "react-router-dom";



export default class Nav extends Component {

  render() {
    return (
      <nav class="navbar navbar-expand-sm navbar-dark bg-dark rounded">
        <Link class="navbar-brand" to="/">InfoBooks</Link>
        <Link class="nav-link" to="/register">Registrarme</Link>
        <Link class="nav-link" to="/login">Iniciar sesión</Link>
        <a class="nav-link" href="/logout">Cerrar sesión</a>
      </nav>
    )
  }
}

