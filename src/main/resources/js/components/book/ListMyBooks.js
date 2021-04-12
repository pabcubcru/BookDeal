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
            <div>
                {this.state.books.map((book) => {
                    return(
                      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                      backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "60px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px", width: '333px',
                      height:"700px", display: 'inline-flex'}}>
                      <center><div>
                      <h5><strong>{book.title}</strong></h5>
                      <a href={"/books/"+book.id}><img style={{height:"100px", width:"100px"}} src={book.image} 
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
                {this.state.books.length == 0 ?
                <p><strong>¿No has subido libros todavía? <a href="/books/new" class="btn btn-primary">Añade uno</a></strong></p>
                :
                <center><br></br>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      <a style={{color:this.state.actualPage == page ? "red" : "black"}} class="pag" href={"/books/me/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/books/me/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}
                  </center>
                }
            </div>
          );
    }
}