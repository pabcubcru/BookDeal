import React, { Component } from 'react';
import requestService from "../services/Request";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      requests: [],
      books1:[],
      books2:[]
    }
  }
    
  async componentDidMount() {
    const res = await requestService.listMyRequests()

    this.setState({requests:res.requests, books1:res.books1, books2:res.books2})
  }

    render() {
        return (
            <div>
                {this.state.requests.map((request, i) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "30px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px"}}>
                    {request.action == "INTERCAMBIO" ?
                      <h5><strong>INTERCAMBIO: {this.state.books1[i].title} por {this.state.books2[i].title}</strong></h5>
                    :
                      <h5><strong>COMPRA: {this.state.books2[i].title} por {this.state.books2[i].price} €</strong></h5>
                    }
                    {request.action == "INTERCAMBIO" ?
                      <center><a href={"/books/"+this.state.books1[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books1[i].image} 
                      style={{padding: '10px', margin:"0px 0px 0px 0px", width: '175px'}}></img> → </a><a href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                      style={{padding: '10px', margin:"0px 0px 0px 0px", width: '175px'}}></img></a></center>
                    :
                    <center><a href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", width: '175px'}}></img></a></center>
                    }
                    <h6 style={{float:"right"}}>{request.status}</h6>

                    </div>)
                })}
                {this.state.requests.length == 0 ?
                <p><strong>¿No tienes todavía peticiones? <a href="/" class="btn btn-primary">Realiza una</a></strong></p>
                :
                <p></p>}
            </div>
          );
    }

    async addFavouriteBook(id) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/")
      
    }
}
