import React, { Component } from 'react';
import bookService from "../services/Book";
import userService from "../services/User";
import userFavouriteBook from "../services/UserFavouriteBook";
import searchService from "../services/Search";
import "./Pagination.css";
import "../ListBooks.css";
import "./Search.css";

export default class List extends Component {

  constructor() {
    super();
    this.state = {
      books: [],
      isAdded: [],
      username: "",
      isAdded:false,
      pages:[],
      actualPage:0,
      query:"",
      searchResult:true,
      images: [],
      selectSearch: "",
      provinces:[],
      showBooks: false,
      fieldStartYear: null,
      fieldFinishYear: null,
      fieldPostalCode: null,
      fieldText: "",
      errorMessages:[],
      errorField: [],
      titles:[]
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;

    if(page != null) {
      const query = this.props.match.params.query;
      if(page) {
        this.setState({actualPage:parseInt(page)})
      } else {
        page = 0
      }

      const res = await searchService.searchBook(query, page)
    
      const username = await userService.getUsername()

      this.setState({books:res.books, username:username.username, query:query, isAdded:res.isAdded, pages:res.pages, numTotalPages:parseInt(res.numTotalPages), 
      searchResult:res.searchResult, images: res.urlImages, showBooks: true})
    }

    const p = await userService.getProvinces()
    const provinces = p.provinces

    this.setState({provinces:provinces})
  }

    render() {
        return (
            <div>
              {!this.state.showBooks ? 
              <div class="search-input">
              <div class="col-3">
                <select style={{float:"left"}} class="form-control chosen-select" id="selectStatus" value={this.state.selectSearch} 
                onChange={(event) => this.setState({selectSearch:event.target.value, errorMessages:[], errorField: []})}>
                  <option value="" disabled>Buscar por</option>
                  <option value="" style={{fontWeight:"bold"}} disabled>Libro</option>
                  <option value="book">Datos del libro</option>
                  <option value="publicationYear">Año de publicación</option>
                  <option value="rangeYears">Rango de años</option>
                  <option value="" style={{fontWeight:"bold"}} disabled>Localización</option>
                  <option value="postalCode">Código postal</option>
                  <option value="province">Provincia</option>
                </select>
              </div>

              {this.state.selectSearch == '' ? 
                <div class="col">
                  <input class="form-control" type="text" disabled/>
                  <p></p>
                </div>
              :<div></div>}

              {this.state.selectSearch == 'book' ? 
              <div class="col">
                <input class="form-control" type="search" list="titles" placeholder="Título, ISBN, autor, géneros, editorial, estado, ..." onChange={(event) => this.searchTitles(event.target.value)}/>
                <datalist id="titles">
                  {this.state.titles.map((title) => {
                    return(
                      <option value={title}></option>
                      )
                  })}
                </datalist>
                {this.state.errorField.indexOf("text") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("text")]}</b></p>
              :
                <p></p>
              }</div>
              :<p></p>}

              {this.state.selectSearch == 'publicationYear' ? 
              <div class="col">
                <input class="form-control" type="number" placeholder="Introduzca el año de publicación (YYYY)" onChange={(event) => this.setState({fieldStartYear:event.target.value})}/>
                {this.state.errorField.indexOf("startYear") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("startYear")]}</b></p>
              :
                <p></p>
              }</div>
              :<p></p>}

              {this.state.selectSearch == 'rangeYears' ?
                <div class="range-years">
                  <div class="col"> 
                  <input class="form-control" type="number" placeholder="Introduzca el año inicial (YYYY)" onChange={(event) => this.setState({fieldStartYear:event.target.value})}/>
                  {this.state.errorField.indexOf("startYear") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("startYear")]}</b></p>
              :
                <p></p>
              }</div>
                  <div class="col">
                  <input class="form-control" type="number" placeholder="Introduzca el año final (YYYY)" onChange={(event) => this.setState({fieldFinishYear:event.target.value})}/>
                  {this.state.errorField.indexOf("finishYear") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("finishYear")]}</b></p>
              :
                <p></p>
              }</div>
                  </div>
              :<p></p>}

              {this.state.selectSearch == 'postalCode' ? 
              <div class="col">
                <input class="form-control" type="number" placeholder="Introduzca el código postal de 5 dígitos" onChange={(event) => this.setState({fieldPostalCode:event.target.value})}/>
                {this.state.errorField.indexOf("postalCode") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("postalCode")]}</b></p>
              :
                <p></p>
              }</div>
              :<p></p>}

              {this.state.selectSearch == 'province' ? 
              <div class="col">
                <select class="form-control chosen-select" id="selectProvince" value={this.state.fieldText} onChange={(event) => this.setState({fieldText:event.target.value})}>
                <option value="">Despliega para ver las opciones</option>
                {this.state.provinces.map((province) => {
                  return (
                    <option value={province}>{province.replaceAll("_", " ")}</option>
                  )
                })}
                </select>
                {this.state.errorField.indexOf("text") != -1 ? 
                <p class='text-danger'><b>{this.state.errorMessages[this.state.errorField.indexOf("text")]}</b></p>
              :
                <p></p>
              }</div>
              :<div><p></p><br></br></div>}
              <button style={{marginRight:"15px", height:"38px"}} class="btn btn-primary" type="button" onClick={() => this.postSearch()} disabled={this.state.selectSearch == ''}> Buscar <i class="fa fa-search"></i></button>
              <br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br>
              </div>
              :
              <div></div>}

              {this.state.showBooks ? 
              <div>
              <h1 style={{float:"left", color: "black"}}><b>Resultados para '{this.state.query.replace("-", " al ")}'</b></h1><br></br><br></br><br></br>
              {this.state.books.length == 0 || this.state.searchResult == false ?
                <div>
                  <p><b>No se encontraron coincidencias para '{this.state.query}'</b></p>
                  {this.state.books.length > 0 ? 
                    <h2 style={{float:"left", color: "black"}}><b>Algunas recomendaciones</b></h2>
                  :
                    <p></p>
                  }
                  <br></br><br></br><br></br>
                  </div>
                :
                  <center>
                  
                  {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center><br></br>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/search/0/"+this.state.query}><b>{String("<<")}</b></a><a style={{margin:"5px"}} class="btn btn-primary" href={"/search/"+parseInt(this.state.actualPage-1)+"/"+this.state.query}><b>{String("<")}</b></a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/search/"+page+"/"+this.state.query}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/search/"+parseInt(this.state.actualPage+1)+"/"+this.state.query}><b>{String(">")}</b></a><a class="btn btn-primary" href={"/search/"+parseInt(this.state.numTotalPages-1)+"/"+this.state.query}><b>{String(">>")}</b></a></span> : <p></p>}
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
                                <a style={{zIndex:"-10"}}  href={"/books/"+book.id}><img class="book-card__img" src={this.state.images[i]} /></a>
                              </div>
                              <div class="book-card__book-back"></div>
                              <div class="book-card__book-side"></div>
                            </div>
                          </div>
                          <div>
                            <div class="book-card__title">
                              {book.title} 
                              {this.state.username != null ?
                            this.state.isAdded[i] == false ? 
                              <a onClick={() => this.addFavouriteBook(book.id, this.state.query)} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://i.ibb.co/WktpFGx/No-Favorito.png"></img></a>
                            :                              
                            <a onClick={() => {this.deleteFavouriteBook(book.id, book.title, this.state.query)}} style={{float:"right"}}><img style={{height:"25px", width:"25px"}} src="https://i.ibb.co/xXKJXKS/Favorito.png"></img></a>
                          :
                            <p></p>
                          }
                            </div>
                            <div class="book-card__author">
                              {book.author}
                            </div>
                            <div class="book-card__author">
                              <span>{book.price} €</span>
                            </div>                           
                          </div>
                        </div>
                      </main>)
                })}
                {this.state.books.length != 0 && this.state.pages.length > 1 ?
                  <center>{this.state.actualPage != 0 ? <span><a class="btn btn-primary" href={"/search/0/"+this.state.query}>{String("<<")}</a><a class="btn btn-primary" style={{margin:"5px"}} href={"/search/"+parseInt(this.state.actualPage-1)+"/"+this.state.query}>{String("<")}</a></span> : <p></p>}
                  {this.state.pages.map((page) => {
                    return(
                      
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/search/"+page+"/"+this.state.query}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.numTotalPages-1 ? <span><a style={{margin:"5px"}} class="btn btn-primary" href={"/search/"+parseInt(this.state.actualPage+1)+"/"+this.state.query}>{String(">")}</a><a class="btn btn-primary" href={"/search/"+parseInt(this.state.numTotalPages-1)+"/"+this.state.query}>{String(">>")}</a></span> : <p></p>}
                  <br></br><br></br></center>
                :
                  <p></p>
                }
                </div>
                :
                <div><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br></div>
              }
            </div>
          );
    }

    async addFavouriteBook(id, query, page) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/search/"+page+"/"+query)
      
    }

    async deleteFavouriteBook(id, title, query, page) {
      const conf = confirm("¿Está seguro de que quiere eliminar "+title+" de favoritos?")
      if(conf) {
        const res = await userFavouriteBook.deleteFavouriteBook(id)
        window.location.replace("/search/"+page+"/"+query)
      }
    }

    async postSearch() {
      const res = await searchService.postSearch(this.state)
      if (res.success) {
        window.location.replace("/search/0/"+res.query)
      } else {
        const errFields = []
        const errMess = []
        const error = res.errors
        error.map((itemerror)=>{
        errFields.push(itemerror.field)
        errMess.push(itemerror.defaultMessage)
        })
        this.setState({errorField:errFields, errorMessages:errMess})
      }
    }

    async searchTitles(query) {
      if(query != '') {
        const res = await searchService.searchTitles(query)
        this.setState({titles:res.titles, fieldText:query})
      }
    }
}