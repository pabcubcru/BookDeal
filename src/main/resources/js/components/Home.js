import React, { Component } from 'react';
import bookService from "./services/Book";

export default class Home extends Component {

  constructor(){
    super();
    this.state = {
      books: []
    }
  }
    
  async componentDidMount() {
    const res = bookService.findAll()
    this.setState({books:res})
  }

  render() {
    return (
      /*<div>
         {
          this.state.books.map((book) => {
            return(
              <p>{book.title}</p>
            )
          })
        }
      </div>*/
      <p>Hola</p>
      
    )
  }
}