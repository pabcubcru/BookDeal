import React, { Component } from 'react'; 
import { Link } from "react-router-dom";
import userService from "./services/User";



export default class Nav extends Component {

  constructor(){
    super();
    this.state = {
      isLogged: false,
      isAdmin: false
    }
  }

  async componentDidMount(){
    const res = await userService.getPrincipal()
    this.setState({
      isLogged: res.isLogged,
      isAdmin: res.isAdmin
    })
  }

  render() {
    let button1;
    let button2;
    let button3;
    let button4;
    let button5;
    if(this.state.isLogged != true){
      button1 = <Link class="nav-link ml-auto" to="/login">Iniciar sesión</Link>;
      button2 = <Link class="nav-link" to="/register">Registrarme</Link>;
    } else {
      button1 = <a class="nav-link" href="/profile">Mi perfil</a>;
      button2 = <a class="nav-link " href="/logout">Cerrar sesión</a>;
      button3 = <a class="nav-link " href="/books/new">Añadir libro</a>;
      button4 = <a class="nav-link " href="/books/all">Libros</a>;
      button5 = <a class="nav-link ml-auto" href="/books/me">Mis libros</a>;
    }
    return (
      <nav class="navbar navbar-expand-sm navbar-dark bg-dark rounded w-100">
        <Link class="navbar-brand" to="/">InfoBooks</Link>
        {button3}
        {button4}
        {button5}
        {button1}
        {button2}
        
      </nav>
    )
  }
}

