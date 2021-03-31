import React, { Component } from 'react';
import bookService from "../services/Book";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      books: []
    }
  }
    
  async componentDidMount() {
    const res = await bookService.listAll()
    this.setState({books:res.books})
  }

    render() {
        return (
            <div>
                {this.state.books.map((book) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "cover" ,  fontWeight: "bold", padding: "41px", marginBlock:"30px", margin:"0px 20px 20px 0px", display: 'inline-block'}}>
                    <center><div><img src={book.image} 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", float: 'left', width: '175px'}}></img></div>
                    <div>
                    <h4><strong>{book.title}</strong></h4>
                    <p>{book.genres}</p>
                    {book.action == "VENTA" ?
                        <p>{book.action} por {book.price} €</p>
                    :
                        <p>{book.action}</p>}</div></center>
                    </div>)
                })}
            </div>
          );
    }
}