import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import "./Get.css";

export default class Get extends Component {

    constructor(){
        super();
        this.state = {
            book: "",
            username:"",
            genres:"",
            isAdded:"",
            alreadyRequest:"",
            hasRequestAccepted:""
        }
    }

    async componentDidMount() {
        const id = this.props.match.params.id;
        const b = await bookService.getBook(id)

        const username = await userService.getUsername()
                          
        this.setState({book:b.book, username:username.username, genres:b.book.genres, isAdded:b.isAdded, alreadyRequest:b.alreadyRequest, hasRequestAccepted:b.hasRequestAccepted})
    }

    render() {
        return(
            
<div>
<div class="cover">
  <div class="book">
    <label for="page-1" class="book__page book__page--1">
      <img src={this.state.book.image} alt=""/>
    </label>

    <label for="page-2" class="book__page book__page--4">
      <img src={this.state.book.image} alt=""/>
    </label>

    <input type="radio" name="page" id="page-1" />

    <input type="radio" name="page" id="page-2" />
    <label class="book__page book__page--2">
      <div class="book__page-front">
        <div class="page__content">
          <h1 class="page__content-book-title">{this.state.book.title}</h1>
          <h2 class="page__content-author">{this.state.book.author}</h2>

        {this.state.book.originalTitle != "" && this.state.book.originalTitle != this.state.book.title? 
          <p class="page__content-credits">
            Título original
            <span>{this.state.book.originalTitle}</span>
          </p>
          :
          <p></p>
        }
          <p class="page__content-credits">
            Editorial
            <span>{this.state.book.publisher}</span>
          </p>

          <p class="page__content-credits">
            Géneros
            <span>{this.state.genres.replaceAll("_", " ").replaceAll(",", ", ")}</span>
          </p>

          <p class="page__content-credits">
            Año de publicación
            <span>{this.state.book.publicationYear}</span>
          </p>

          <p class="page__content-credits">
            ISBN
            <span>{this.state.book.isbn}</span>
          </p>

          <p class="page__content-credits">
            ¿Qué quiere hacer?
            <span>{this.state.book.action == "VENTA" ?
                <p> {this.state.book.action} por {this.state.book.price} €</p>
            :
                <p> {this.state.book.action}</p>} </span>
          </p>

          <p class="page__content-credits">
            Estado
            <span>{this.state.book.status}</span>
          </p>

          <div class="page__content-copyright">
            <p>Publicado por {this.state.book.username}</p>
          </div>
        </div>
      </div>
      <div class="book__page-back">
        <div class="page__content">
          <h1 class="page__content-title">Sinopsis</h1>
          <div class="page__content-blockquote">
            <p class="page__content-blockquote-text">{this.state.book.description}</p>
         </div>

          <div class="page__number">2</div>
        </div>
      </div>
    </label></div>
    </div>
    <center>{this.state.username == this.state.book.username ? 
                <center><br></br><button onClick={() => this.goToEdit(this.state.book.id)} style={{margin:"10px"}} class="btn btn-primary" disabled={this.state.hasRequestAccepted == true}>Editar</button>
                <button onClick={() => this.deleteBook(this.state.book.id)} style={{background:"red", color:"white"}} class="btn btn-primary" disabled={this.state.hasRequestAccepted == true}>Eliminar</button>
                {this.state.hasRequestAccepted ? 
                    <p class='text-danger'><b>*Tiene una petición ACEPTADA</b></p>
                :
                    <p></p>
                }</center>
            :
                <p></p>
            }

            {this.state.username != null && this.state.username != this.state.book.username ?
                !this.state.isAdded ? 
                    <center>
                    <a onClick={() => this.addFavouriteBook(this.state.book.id)} style={{color:"white", margin:"10px"}} class="btn btn-primary">Añadir a favoritos</a></center>
                :
                    <center>
                    <a onClick={() => {this.deleteFavouriteBook(this.state.book.id, this.state.book.title)}} style={{background:"red", color:"white"}} class="btn btn-primary">Eliminar de favoritos</a></center>
            :                              
                <p></p>
            }   

            {this.state.username != this.state.book.username && this.state.username != null ?
                !this.state.alreadyRequest ?
                <center>
                    {!this.state.hasRequestAccepted ?
                        <a href={"/requests/"+this.state.book.id+"/add"} style={{color:"white", margin:"10px"}} class="btn btn-primary">Realizar petición</a>
                    :
                        <center><button style={{color:"white", margin:"10px"}} class="btn btn-primary" disabled>Realizar petición</button>
                        <p class='text-danger'>*Ya tiene una petición ACEPTADA</p></center>
                    }
                </center>
                :
                <button style={{background:"#099C01",color:"white", margin:"10px"}} class="btn btn-primary" disabled>Petición realizada</button>
            :
                <p></p>
            }</center>
    </div>
    )
    }

    async deleteBook(id) {
        const conf = confirm("¿Está seguro de que quiere eliminarlo? Esta acción no es reversible.")
        if(conf) {
          const res = await bookService.delete(id)
          window.location.replace("/books/me/0")
        }
      }

    async addFavouriteBook(id) {
        const res = await userFavouriteBook.addFavouriteBook(id)
        window.location.replace("/books/"+id)
    
    }

    async deleteFavouriteBook(id, title) {
        const conf = confirm("¿Está seguro de que quiere eliminar "+title+" de favoritos?")
        if(conf) {
          const res = await userFavouriteBook.deleteFavouriteBook(id)
          window.location.replace("/books/"+id)
        }
      }

    async goToEdit(id) {
      window.location.replace('/books/'+id+'/edit')
    }

      
}