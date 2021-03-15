import React, { Component } from 'react'; 
import { Link } from "react-router-dom";
import userService from "./services/User"



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
    if(this.state.isLogged != true){
      button1 = <Link class="nav-link" to="/register">Registrarme</Link>;
      button2 = <Link class="nav-link" to="/login">Iniciar sesión</Link>;
    } else {
      button1 = <a class="nav-link" href="/profile">Mi perfil</a>;
      button2 = <a class="nav-link" href="/logout">Cerrar sesión</a>;
    }
    return (
      <nav class="navbar navbar-expand-sm navbar-dark bg-dark rounded">
        <Link class="navbar-brand" to="/">InfoBooks</Link>
        {button1}
        {button2}
        
      </nav>
    )
  }
}

