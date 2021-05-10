import React, { Component } from 'react'; 
import { Link } from "react-router-dom";
import userService from "./services/User";
import "./Nav.css";


export default class Nav extends Component {

  constructor(){
    super();
    this.state = {
      isLogged: false,
      isAdmin: false,
      query:""
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
      button1 = <a class="nav-link ml-auto" href="/login">Iniciar sesión <i class="fa fa-sign-in" aria-hidden="true"></i></a>;
      button2 = <a class="nav-link" href="/register">Registro <i class="fa fa-user-plus"></i></a>;
    } else {
      button1 = <a  class="nav-link" href="/profile">Mi perfil</a>;
      button2 = <a  class="nav-link " href="/logout">Cerrar sesión</a>;
    }

    const info = "Se pueden realizar diferentes tipos de búsquedas: ------------------------------------------------------ "+
    "1) Escriba las palabras clave separadas por un espacio. ------------------------------------------------ "+
    "2) Escriba sólo un número sin espacios, para buscar por año o precio del libro, o por código postal del usuario propietario. "+
    "------------------------------------------ 3) Puedes buscar por un rango de años, separandolos por un guión y sin espacios (YYYY-YYYY)."

    return (
      <nav style={{marginBottom:"30px"}}>
        <div class="desktop">
        <ul class="menu">
          <li ><a href="/">InfoBooks <i class="fa fa-home"></i></a></li>
          {this.state.isLogged ? 
            <div><li><a  href="#">Libros <i class="fa fa-book"></i></a>
            <ul>
                <li ><a  href="/books/new">Añadir libro</a></li>
                <li ><a  href="/books/me/0">Mis libros</a></li>
                <li ><a  href="/favourites/0">Mis favoritos</a></li>
                <li ><a  href="/books/recommend/0">Recomendaciones</a></li>
                <li ><a  href="/books/all/0">Cerca de mí</a></li>
                <li ><a  href="#">Filtrar por</a>
                  <ul>
                    <li ><a  href="/books/all/0/postCode">Mi cod. postal</a></li>
                    <li ><a  href="/books/all/0/province">Mi provincia</a></li>
                    <li ><a  href="/books/all/0/genres">Mis géneros preferidos</a></li>
                  </ul>
                </li>
            </ul>
          </li>
          <li ><a  href="#">Peticiones <i class="fa fa-handshake-o" aria-hidden="true"></i></a>
            <ul>
                <li ><a  href="/requests/me/0">Enviadas</a></li>
                <li ><a  href="/requests/received/0">Recibidas</a></li>
            </ul>
          </li>
          <li style={{float:"right"}}><a href="#" >Perfil <i class="fa fa-user-circle"></i></a>
            <ul>
                <li >{button1}</li>
                <li >{button2}</li>
            </ul>
          </li>
          <li style={{float:"right"}}><div class="search-box">
            <input class="text" type="text" placeholder="Título, ISBN, autor, ..." onChange={(event) => this.setState({query:event.target.value})}/>
            <dfn data-info={info}><i style={{top: "50%"}} class="fa fa-info-circle"></i></dfn>
          <button type="button" onClick={() => this.search(this.state.query)} disabled={this.state.query.trim() == ''}> <i class="fa fa-search"></i></button></div></li>
          </div>
        :
          <div><li style={{float:"left"}}><a  href="/books/all/0">Libros <i class="fa fa-book"></i></a></li>
          <li style={{float:"right"}}>{button2}</li>
          <li style={{float:"right"}}>{button1}</li>
          <li style={{float:"right"}}><div class="search-box">
            <input class="text" type="text" placeholder="Título, ISBN, autor, ..." onChange={(event) => this.setState({query:event.target.value})}/>
            <dfn data-info={info}><i style={{top: "50%"}} class="fa fa-info-circle"></i></dfn>
          <button type="button" onClick={() => this.search(this.state.query)} disabled={this.state.query.trim() == ''}> <i class="fa fa-search"></i></button></div></li>
          </div>
        }  
          
        </ul>
        </div>

        <div class="mobile">
        <ul class="menu">
          <li ><a href="/"> <i class="fa fa-home"></i></a></li>
          {this.state.isLogged ? 
            <div><li><a  href="#"> <i class="fa fa-book"></i></a>
            <ul>
                <li ><a  href="/books/new">Añadir libro</a></li>
                <li ><a  href="/books/me/0">Mis libros</a></li>
                <li ><a  href="/favourites/0">Mis favoritos</a></li>
                <li ><a  href="/books/recommend/0">Recomendaciones</a></li>
                <li ><a  href="/books/all/0">Cerca de mí</a></li>
                <li ><a  href="#">Filtrar por</a>
                  <ul>
                    <li ><a  href="/books/all/0/postCode">Mi cod. postal</a></li>
                    <li ><a  href="/books/all/0/province">Mi provincia</a></li>
                    <li ><a  href="/books/all/0/genres">Mis géneros</a></li>
                  </ul>
                </li>
            </ul>
          </li>
          <li ><a  href="#"><i class="fa fa-handshake-o" aria-hidden="true"></i></a>
            <ul>
                <li ><a  href="/requests/me/0">Enviadas</a></li>
                <li ><a  href="/requests/received/0">Recibidas</a></li>
            </ul>
          </li>
          <li style={{float:"right"}}><a href="#" ><i class="fa fa-user-circle"></i></a>
            <ul>
                <li >{button1}</li>
                <li >{button2}</li>
            </ul>
          </li>
          <li style={{float:"right"}}><a href="#" ><i class="fa fa-search"></i></a>
            <ul >
              <li class="search-li" style={{backgroundColor:"transparent"}}>
                <div class="search">
                <input class="text" type="text" placeholder="Título, ISBN, año, ..."/>
                <dfn data-info={info}><i style={{top: "50%"}} class="fa fa-info-circle"></i></dfn>
                <button> <i class="fa fa-search"></i></button>
                </div>
              </li>
            </ul>
          </li>
          </div>
        :
          <div><li style={{float:"left"}}><a  href="/books/all/0"><i class="fa fa-book"></i></a></li>
          <li style={{float:"right"}}><a class="nav-link" href="/register"><i class="fa fa-user-plus"></i></a></li>
          <li style={{float:"right"}}><a class="nav-link ml-auto" href="/login"><i class="fa fa-sign-in" aria-hidden="true"></i></a></li>
          <li style={{float:"right"}}><a href="#" ><i class="fa fa-search"></i></a>
            <ul >
              <li class="search-li" style={{backgroundColor:"transparent"}}>
                <div class="search">
                <input class="text" type="text" placeholder="Título, ISBN, año, ..."/>
                <dfn data-info={info}><i style={{top: "50%"}} class="fa fa-info-circle"></i></dfn>
                <button> <i class="fa fa-search"></i></button>
                </div>
              </li>
            </ul>
          </li>
          </div>
        }  
          
        </ul>
        </div>
        
      </nav>
    )
  }

  async search(query) {
    window.location.replace("/search/0/"+query)
  }
}