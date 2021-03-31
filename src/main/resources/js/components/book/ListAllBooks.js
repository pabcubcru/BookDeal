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
    const res = await bookService.listAllExceptMine()
    this.setState({books:res.books})
  }

    render() {
        return (
            <div>
                {this.state.books.map((book) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "41px", marginBlock:"30px", margin:"0px 20px 20px 0px", display: 'inline-flex'}}>
                    <center><div>
                    <h4><strong>{book.title}</strong></h4>{book.originalTitle != "" ? <h5>({book.originalTitle})</h5> : <p></p>}
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
                        <a href={"/books/"+book.id} class="btn btn-primary">Más detalles</a>
                        </center>
                    </div>)
                })}
            </div>
          );
    }
}