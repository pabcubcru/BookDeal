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
    const res = await bookService.listMyBooks()
    this.setState({books:res.books})
  }

    render() {
        return (
            <div>
                {this.state.books.map((book) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "cover" ,  fontWeight: "bold", padding: "41px", marginBlock:"30px", margin:"0px 20px 20px 0px", display: 'inline-block'}}>
                    <center><img src="https://www.xlsemanal.com/wp-content/uploads/sites/3/2020/03/todo-lo-que-un-libro-de-papel-puede-hacer-por-tu-cerebro-abrelo-1024x576.jpg" 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", float: 'left', width: '250px'}}></img>
                    <h4><strong>{book.title}</strong></h4>
                    <p>{book.action}</p>
                    <p>{book.genres}</p>
                    {book.action == "VENDER" ?
                        <p>{book.price} €</p>
                    :
                        <p></p>}</center>
                    </div>)
                })}
                {this.state.books.length == 0 ?
                <p><strong>¿No has subido libros todavía? <a href="/books/new">Añadelo ahora</a></strong></p>
                :
                <p></p>}
            </div>
          );
    }
}