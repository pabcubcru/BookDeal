import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: [],
      isAdded: [],
      username: "",
      isAdded:false
    }
  }
    
  async componentDidMount() {
    const res = await bookService.listAllExceptMine()
    
    const username = await userService.getUsername()

    this.setState({books:res.books, username:username.username, isAdded:res.isAdded})
  }

    render() {
        return (
            <div>
                  {this.state.books.map((book, i) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "60px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px", width: '333px',
                    display: 'inline-flex'}}>
                    <center><div>
                    <h5><strong>{book.title}</strong></h5>
                    <a href={"/books/"+book.id}><img src={book.image} 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", width: '175px'}}></img></a></div>
                    <div>
                    <br clear="left"></br>
                    <p><strong>Autor: </strong>{book.author}</p>
                    <p><strong>Editorial: </strong>{book.publisher}</p>
                    {book.action == "VENTA" ?
                        <p>{book.action} por {book.price} €</p>
                    :
                        <p>{book.action}</p>}</div>
                        <hr></hr>
                        <a href={"/books/"+book.id} class="btn btn-primary" style={{margin:"10px", marginTop:"0px"}}>Más detalles</a>
                        {this.state.username != null ?
                            this.state.isAdded[i] == false ? 
                              <a onClick={() => this.addFavouriteBook(book.id)} style={{color:"white"}} class="btn btn-primary">Añadir a favoritos</a>
                            :                              
                            <button style={{background:"#099C01",color:"white"}} class="btn btn-primary" disabled>Favorito</button>
                          :
                            <p></p>
                        }
                        </center>
                    </div>)
                })}
                {this.state.books.length == 0 ?
                  <p><b>Actualmente no existen libros para mostrar.</b></p>
                :
                  <p></p>
                }
            </div>
          );
    }

    async addFavouriteBook(id) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/")
      
    }
}