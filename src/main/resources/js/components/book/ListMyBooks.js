import React, { Component } from 'react';
import bookService from "../services/Book";

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
    const res = await bookService.listMyBooks(page)

    this.setState({books:res.books, pages:res.pages, actualPage:parseInt(page)})
  }

    render() {
      return (
        <div >
            {this.state.books.length == 0 ?
                  <p><b>Actualmente no existen libros para mostrar.</b></p>
                :
                  <center>
                  {this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/me/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}</center>
                  :
                    <p></p>
                  }<br></br><br></br></center>
                }
              {this.state.books.map((book) => {
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
              <center style={{zIndex:"-1"}}>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
              {this.state.pages.map((page) => {
                return(
                  <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/me/"+page}>{page}</a>
                )
              })}
              {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}
              <br></br><br></br></center>
            :
              <p></p>
            }
        </div>
      );
    }
}