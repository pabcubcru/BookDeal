import React, { Component } from 'react'; 
import { Link } from "react-router-dom";
import userService from "./services/User";
import "./Nav.css";


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
      button1 = <a class="nav-link ml-auto" href="/login">Iniciar sesión</a>;
      button2 = <a class="nav-link" href="/register">Registrarme</a>;
    } else {
      button1 = <a  class="nav-link" href="/profile">Mi perfil</a>;
      button2 = <a  class="nav-link " href="/logout">Cerrar sesión</a>;
    }

    return (
      <nav style={{marginBottom:"30px"}}>
        <ul class="menu">
          <li ><a href="/">InfoBooks</a></li>
          {this.state.isLogged ? 
            <div><li ><a  href="#">Libros</a>
            <ul>
                <li ><a  href="/books/all/0">Todos</a></li>
                <li ><a  href="/books/me/0">Mis libros</a></li>
                <li ><a  href="/favourites/0">Favoritos</a></li>
                <li ><a  href="/books/new">Añadir libro</a></li>
            </ul>
          </li>
          <li ><a  href="#">Peticiones</a>
            <ul>
                <li ><a  href="/requests/me">Enviadas</a></li>
                <li ><a  href="/requests/received">Recibidas</a></li>
            </ul>
          </li>
          <li style={{float:"right"}}><a href="#" >Perfil</a>
            <ul>
                <li >{button1}</li>
                <li >{button2}</li>
            </ul>
          </li>
          </div>
        :
          <div><li style={{float:"left"}}><a  href="/books/all/0">Libros</a></li>
          <li style={{float:"right"}}>{button2}</li>
          <li style={{float:"right"}}>{button1}</li></div>
        }  
          
        </ul>
      </nav>
    )
  }
}

