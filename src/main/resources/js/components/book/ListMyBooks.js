import React, { Component } from 'react';
import bookService from "../services/Book";
import "../ListBooks.css";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: [],
      pages:[],
      actualPage:0,
      numTotalPages:0
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;
    const res = await bookService.listMyBooks(page)

    this.setState({books:res.books, pages:res.pages, actualPage:parseInt(page), numTotalPages:parseInt(res.numTotalPages)})
  }

    render() {
      return (
        <div >
            {this.state.books.length == 0 ?
                  <p><b>Actualmente no existen libros para mostrar.</b></p>
                :
                  <center>
                  {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/books/all/0"}>{String("<<")}</a><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage-1)}>{String("<")}</a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/all/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage+1)}>{String(">")}</a><a class="btn btn-primary" href={"/books/all/"+parseInt(this.state.numTotalPages-1)}>{String(">>")}</a></span> : <p></p>}
                  </center>
                :
                  <p></p>
                }<br></br></center>
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
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/books/all/0"}>{String("<<")}</a><a class="btn btn-primary" style={{margin:"5px"}} href={"/books/all/"+parseInt(this.state.actualPage-1)}>{String("<")}</a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/books/all/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/books/all/"+parseInt(this.state.actualPage+1)}>{String(">")}</a><a class="btn btn-primary" href={"/books/all/"+parseInt(this.state.numTotalPages-1)}>{String(">>")}</a></span> : <p></p>}
                  <br></br><br></br></center>
                :
                  <p></p>
                }
        </div>
      );
    }
}