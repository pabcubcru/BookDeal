import React, { Component } from 'react';
import userFavouriteBook from "../services/UserFavouriteBook";
import "../ListBooks.css";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: [],
      pages:[],
      actualPage:0
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;
    if(page) {
      this.setState({actualPage:parseInt(page)})
    } else {
      page = 0
    }
    const res = await userFavouriteBook.findAllFavouritesBooks(page)
    this.setState({books:res.books, pages:res.pages})
  }

    render() {
        return (
            <div>
              {this.state.books.length == 0 ?
                <p><strong>¿No tienes todavía libros favoritos? <a href="/" class="btn btn-primary">Añade uno</a></strong></p>
                :
                <center>
                  {this.state.pages.length > 1 ? 
                    <center>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/favourites/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                    {this.state.pages.map((page) => {
                    return(
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/favourites/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/favourites/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}</center>
                  :
                  <p></p>
                  }<br></br></center>}
                {this.state.books.map((book) => {
                    return(
                      <main class="mainBooks">
                        <div class="book-card">
                          <div class="book-card__cover">
                            <div class="book-card__book">
                              <div class="book-card__book-front">
                                <a href={"/books/"+book.id}><img class="book-card__img" src={book.image} /></a>
                              </div>
                              <div class="book-card__book-back"></div>
                              <div class="book-card__book-side"></div>
                            </div>
                          </div>
                          <div>
                            <div class="book-card__title">
                              {book.title} <a onClick={() => {this.deleteFavouriteBook(book.id, book.title, this.state.actualPage)}} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Coraz%C3%B3n.svg/1121px-Coraz%C3%B3n.svg.png"></img></a>
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
                {this.state.books.length != 0 && this.state.pages.length > 1?
                <center><br></br>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/favourites/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                {this.state.pages.map((page) => {
                  return(
                    <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/favourites/"+page}>{page}</a>
                  )
                })}
                {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/favourites/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}
                </center>
                :
                  <p></p>
                }
            </div>
          );
    }

    async deleteFavouriteBook(id, title, actualPage) {
      const conf = confirm("¿Está seguro de que quiere eliminar "+title+" de favoritos?")
      if(conf) {
        const del = await userFavouriteBook.deleteFavouriteBook(id)
        window.location.replace("/favourites/"+actualPage)
      }
    }
}