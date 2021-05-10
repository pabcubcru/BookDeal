import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import "./Pagination.css";
import "../ListBooks.css";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: [],
      isAdded: [],
      username: "",
      isAdded:false,
      pages:[],
      actualPage:0,
      numTotalPages:0,
      nearBooks: true,
      title:"",
      images: []
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;
    if(page) {
      this.setState({actualPage:parseInt(page)})
    } else {
      page = 0
    }

    const res = await bookService.listAllExceptMine(page)
    
    const username = await userService.getUsername()

    this.setState({books:res.books, username:username.username, isAdded:res.isAdded, pages:res.pages, numTotalPages:parseInt(res.numTotalPages), 
      nearBooks:res.nearBooks, title:res.title, images: res.urlImages})
  }

    render() {
        return (
            <div >
                {this.state.username != null ? 
                  <div><h1 style={{float:"left", color: "black"}}><b>{this.state.title}</b></h1><br></br><br></br></div>
                :
                  <div><h1 style={{float:"left", color: "black"}}><b>Todos los libros</b></h1><br></br><br></br></div>
                  }
                {this.state.books.length == 0 || this.state.nearBooks == false ?
                  <div>
                  <br></br>
                  <p><b>Actualmente no existen libros para mostrar{this.state.username != null ? ' cerca de usted' : ''}.</b></p>
                  {this.state.username != null ?
                    <h2 style={{float:"left", color: "black"}}><b>Algunas recomendaciones</b></h2>
                  :
                    <p></p>
                  }
                  <br></br><br></br><br></br>
                  </div>
                :
                  <center>
                  
                  {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><br></br><a class="btn btn-primary" href={"/books/all/0"}><b>{String("<<")}</b></a><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage-1)}><b>{String("<")}</b></a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/all/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage+1)}><b>{String(">")}</b></a><a class="btn btn-primary" href={"/books/all/"+parseInt(this.state.numTotalPages-1)}><b>{String(">>")}</b></a></span> : <p></p>}
                  </center>
                :
                  <p></p>
                }<br></br></center>
                }
                  {this.state.books.map((book, i) => {
                    return(
                      <main class="mainBooks">
                        <div class="book-card">
                          <div class="book-card__cover">
                            <div class="book-card__book">
                              <div class="book-card__book-front">
                                <a style={{zIndex:"-10"}}  href={"/books/"+book.id}><img class="book-card__img" src={this.state.images[i]} alt=""/></a>
                              </div>
                              <div class="book-card__book-back"></div>
                              <div class="book-card__book-side"></div>
                            </div>
                          </div>
                          <div>
                            <div class="book-card__title">
                              {book.title} 
                              {this.state.username != null ?
                            this.state.isAdded[i] == false ? 
                              <a onClick={() => this.addFavouriteBook(book.id, this.state.actualPage)} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://i.ibb.co/WktpFGx/No-Favorito.png"></img></a>
                            :                              
                            <a onClick={() => {this.deleteFavouriteBook(book.id, book.title, this.state.actualPage)}} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://i.ibb.co/xXKJXKS/Favorito.png"></img></a>
                          :
                            <p></p>
                          }
                            </div>
                            <div class="book-card__author">
                              {book.author}
                            </div>
                            <div class="book-card__author">
                              <span>{book.price} €</span>
                            </div>
                            
                            
                          </div>
                        </div>
                      </main>)
                })}
                {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/books/all/0"}><b>{String("<<")}</b></a><a class="btn btn-primary" style={{margin:"5px"}} href={"/books/all/"+parseInt(this.state.actualPage-1)}><b>{String("<")}</b></a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/all/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage+1)}><b>{String(">")}</b></a><a class="btn btn-primary" href={"/books/all/"+parseInt(this.state.numTotalPages-1)}><b>{String(">>")}</b></a></span> : <p></p>}
                  <br></br><br></br></center>
                :
                  <p></p>
                }
            </div>
          );
    }

    async addFavouriteBook(id, actualPage) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/books/all/"+actualPage)
      
    }

    async deleteFavouriteBook(id, title, actualPage) {
      const conf = confirm("¿Está seguro de que quiere eliminar "+title+" de favoritos?")
      if(conf) {
        const res = await userFavouriteBook.deleteFavouriteBook(id)
        window.location.replace("/books/all/"+actualPage)
      }
    }
}