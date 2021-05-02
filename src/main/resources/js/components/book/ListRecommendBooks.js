import React, { Component } from 'react';
import bookService from "../services/Book";
import userFavouriteBook from "../services/UserFavouriteBook";
import "../ListBooks.css";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: [],
      pages:[],
      actualPage:0,
      numTotalPages:0,
      isAdded:[]
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;
    const res = await bookService.recommendBooks(page)

    this.setState({books:res.books, pages:res.pages, actualPage:parseInt(page), numTotalPages:parseInt(res.numTotalPages), isAdded:res.isAdded})
  }

    render() {
      return (
        <div >
            {this.state.books.length == 0 ?
                  <div><p><b>No se encuentra ningún libro para recomendarle. Añada más géneros preferidos a su perfil. O añada libros a favoritos.<a class="btn btn-primary" href="/profile">Ir a perfil</a></b></p><br></br>
                  <br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br>
                  <br></br><br></br></div>
                :
                  <center>
                  {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/books/recommend/0"}>{String("<<")}</a><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/recommend/"+parseInt(this.state.actualPage-1)}>{String("<")}</a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/recommend/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/recommend/"+parseInt(this.state.actualPage+1)}>{String(">")}</a><a class="btn btn-primary" href={"/books/recommend/"+parseInt(this.state.numTotalPages-1)}>{String(">>")}</a></span> : <p></p>}
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
                            <a style={{zIndex:"-10"}}  href={"/books/"+book.id}><img class="book-card__img" src={book.image} /></a>
                          </div>
                          <div class="book-card__book-back"></div>
                          <div class="book-card__book-side"></div>
                        </div>
                      </div>
                      <div>
                        <div class="book-card__title">
                          {book.title} 
                          {this.state.isAdded[i] == false ? 
                              <a onClick={() => this.addFavouriteBook(book.id, this.state.actualPage)} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="http://assets.stickpng.com/images/5a02bfca18e87004f1ca4395.png"></img></a>
                            :                              
                            <a onClick={() => {this.deleteFavouriteBook(book.id, book.title, this.state.actualPage)}} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Coraz%C3%B3n.svg/1121px-Coraz%C3%B3n.svg.png"></img></a>
                            }
                        </div>
                        <div class="book-card__author">
                          {book.author}
                            </div>
                        <div class="book-card__author">
                        {book.action == "VENTA" ?
                          <span>{book.action} por {book.price} €</span>
                        :
                          book.action}
                        </div>
                                               
                      </div>
                    </div>
                  </main>)
            })}
            {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/books/recommend/0"}>{String("<<")}</a><a class="btn btn-primary" style={{margin:"5px"}} href={"/books/recommend/"+parseInt(this.state.actualPage-1)}>{String("<")}</a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/recommend/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/recommend/"+parseInt(this.state.actualPage+1)}>{String(">")}</a><a class="btn btn-primary" href={"/books/recommend/"+parseInt(this.state.numTotalPages-1)}>{String(">>")}</a></span> : <p></p>}
                  <br></br><br></br></center>
                :
                  <p></p>
                }
        </div>
      );
    }

    async addFavouriteBook(id, actualPage) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/books/recommend/"+actualPage)
      
    }

    async deleteFavouriteBook(id, title, actualPage) {
      const conf = confirm("¿Está seguro de que quiere eliminar "+title+" de favoritos?")
      if(conf) {
        const res = await userFavouriteBook.deleteFavouriteBook(id)
        window.location.replace("/books/recommend/"+actualPage)
      }
    }
}