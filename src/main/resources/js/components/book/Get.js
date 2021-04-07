import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";

export default class Get extends Component {

    constructor(){
        super();
        this.state = {
            book: "",
            username:"",
            genres:"",
            isAdded:"",
            alreadyRequest:""
        }
    }

    async componentDidMount() {
        const id = this.props.match.params.id;
        const b = await bookService.getBook(id)

        const username = await userService.getUsername()
                          
        this.setState({book:b.book, username:username.username, genres:b.book.genres, isAdded:b.isAdded, alreadyRequest:b.alreadyRequest})
    }

    render() {
        return(
            <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
            backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "41px", marginBlock:"30px", margin:"0px 150px 0px 150px"}}>
            <center><div>
            <h1><strong>{this.state.book.title}</strong></h1>{this.state.book.originalTitle != "" ? <h3> ({this.state.book.originalTitle})</h3> : <p></p>}
            <img src={this.state.book.image} 
            style={{padding: '10px', width: '250px'}}></img></div>
            <div>
            <br clear="left"></br>
            <h6><strong>Autor:</strong> {this.state.book.author}</h6>
            <h6><strong>Editorial:</strong> {this.state.book.publisher}</h6>
            <h6><strong>ISBN:</strong> {this.state.book.isbn}</h6>
            <h6><strong>Publicado en:</strong> {this.state.book.publicationYear}</h6>
            <h6><strong>Sinopsis: </strong>{this.state.book.description}</h6>
            <h6><strong>Géneros:</strong> {this.state.genres.replaceAll("_", " ").replaceAll(",", ", ")}</h6>
            {this.state.book.action == "VENTA" ?
                <h6><strong>¿Qué quiere hacer?</strong> {this.state.book.action} por {this.state.book.price} €</h6>
            :
                <h6><strong>¿Qué quiere hacer?</strong> {this.state.book.action}</h6>} 
                </div>
            {this.state.username == this.state.book.username ? 
                
                <center><br></br><hr></hr><a href={'/books/'+this.state.book.id+'/edit'} style={{margin:"10px"}} class="btn btn-primary">Editar</a>
                <a onClick={() => this.deleteBook(this.state.book.id)} style={{background:"red", color:"white"}} class="btn btn-primary">Eliminar</a></center>
            :
                !this.state.isAdded ? 
                    <center><h6><strong>Publicado por:</strong> {this.state.book.username}</h6><br></br><hr></hr>
                    <a onClick={() => this.addFavouriteBook(this.state.book.id)} style={{color:"white", margin:"10px"}} class="btn btn-primary">Añadir a favoritos</a></center>
                :                              
                    <center><h6><strong>Publicado por:</strong> {this.state.book.username}</h6><br></br><hr></hr>
                    <button style={{background:"#099C01",color:"white", margin:"10px"}} class="btn btn-primary" disabled>Favorito</button></center>
                
            }
            {this.state.username != this.state.book.username ?
                !this.state.alreadyRequest ?
                <a href={"/requests/"+this.state.book.id+"/add"} style={{color:"white", margin:"10px"}} class="btn btn-primary">Realizar petición</a>
                :
                <button style={{background:"#099C01",color:"white", margin:"10px"}} class="btn btn-primary" disabled>Petición realizada</button>
            :
                <p></p>
            }
            </center>
            </div>
        )
    }

    async deleteBook(id) {
        const conf = confirm("¿Está seguro de que quiere eliminarlo? Esta acción no es reversible.")
        if(conf) {
          const res = await bookService.delete(id)
          window.location.replace("/books/me")
        }
      }

    async addFavouriteBook(id) {
        const res = await userFavouriteBook.addFavouriteBook(id)
        window.location.replace("/books/"+id)
    
    }
}