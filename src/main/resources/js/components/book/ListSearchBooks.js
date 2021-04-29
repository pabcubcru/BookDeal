import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import searchService from "../services/Search";
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
      query:""
    }
  }
    
  async componentDidMount() {
    const query = this.props.match.params.query;

    const res = await searchService.searchBook(query)
    
    const username = await userService.getUsername()

    this.setState({books:res.books, username:username.username, query:query})
  }

    render() {
        return (
            <div >{this.state.books.length == 0 ?
                <p><b>No se encontraron coincidencias para '{this.state.query}'</b></p>
                :
                <p></p>
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
            </div>
          );
    }
}