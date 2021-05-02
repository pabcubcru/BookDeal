import React, { Component } from 'react';
import requestService from "../services/Request";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      requests: [],
      books1:[],
      books2:[],
      users:[],
      pages:[],
      actualPage:0
    }
  }
    
  async componentDidMount() {
    const page = this.props.match.params.page;
    if(page) {
      this.setState({actualPage:parseInt(page)})
    } else {
      page = 0
    }

    const res = await requestService.listReceivedRequests(page)

    this.setState({requests:res.requests, books1:res.books1, books2:res.books2, users:res.users, pages:res.pages, numTotalPages:parseInt(res.numTotalPages)})
  }

    render() {
        return (
            <div>
              <h1 style={{float:"left", color: "black"}}><b>Peticiones recibidas</b></h1><br></br><br></br><br></br>
              {this.state.requests.length == 0 ?
                <div><p><strong>¿No has recibido peticiones todavía? <a href="/books/new" class="btn btn-primary">¡Añade un libro!</a></strong></p><br></br>
                <br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br>
                <br></br></div>
                :
                <center>
                  
                  {this.state.pages.length > 1 ? 
                    <center><br></br>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/requests/received/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                    {this.state.pages.map((page) => {
                    return(
                      <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/requests/received/"+page}>{page}</a>
                    )
                  })}
                  {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/requests/received/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}</center>
                  :
                  <p></p>
                  }<br></br></center>}
                {this.state.requests.map((request, i) => {
                    return(
                      <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                      backgroundSize: "auto auto", padding: "15px", paddingTop:"20px", marginBlock:"15px", margin:"0px 12px 30px 0px", borderRadius: '5px', width: "32%", display:"inline-grid"}}>

                    {request.action == "INTERCAMBIO" ?
                      <center><h4><strong><u>INTERCAMBIO</u><br></br></strong></h4></center>
                    :
                      <center><h4><strong><u>VENTA POR {request.pay} €</u><br></br></strong></h4></center>
                    }

                    {request.action == "INTERCAMBIO" ?
                      <center><a style={{color:"black"}} href={"/books/"+this.state.books2[i].id}><img style={{height:"200px", width:"140px", padding: '10px', margin:"0px 0px 0px 0px"}} src={this.state.books2[i].image} ></img><strong>
                        ⇄  </strong></a><a href={"/books/"+this.state.books1[i].id}><img style={{height:"200px", width:"140px", padding: '10px', margin:"0px 0px 0px 0px"}} src={this.state.books1[i].image}></img></a><br></br></center>
                    :
                    <center><a href={"/books/"+this.state.books2[i].id}><img style={{height:"100px", width:"100px"}} src={this.state.books2[i].image} 
                    style={{padding: '10px', margin:"0px 0px 0px 0px", width: '120px'}}></img></a> 
                    </center>
                    }
                    <center>

                    {request.action == "COMPRA" ?
                      <p><strong>Precio original: </strong>{this.state.books2[i].price} €</p>
                    :
                      <p></p>
                    }

                    {request.comment != "" ?
                      <p><strong>Comentario adicional: </strong>{request.comment}</p>
                    :
                      <p></p>
                    }
                    {request.status != "ACEPTADA" ?
                      <p><strong>Petición enviada por: </strong>{request.username1}</p>
                    :
                      <p></p>
                    }
                    <p><strong>Estado: </strong>{request.status}</p>
                    
                    </center>
                    
                    {request.status == "PENDIENTE" ? 
                      <center><a onClick={() => this.acceptRequest(request.id, this.state.books2[i].title, this.state.actualPage)} style={{background:"#099C01",color:"white", margin:"10px"}} class="btn btn-primary"><strong>Aceptar</strong></a>
                      <a onClick={() => this.rejectRequest(request.id, this.state.actualPage)} style={{background:"red", color:"white"}} class="btn btn-primary"><strong>Rechazar</strong></a></center>
                    :
                      request.status == "ACEPTADA" ?
                        <center><p><strong><i><u>Datos de contacto con {request.username1}</u></i></strong></p>
                        <p><strong>Número de teléfono: </strong>{this.state.users[i].phone}</p>
                        <p><strong> Email: </strong>{this.state.users[i].email}</p></center>
                      :
                        <p></p>
                      }
                    </div>)
                })}

                {this.state.pages.length > 1 ? 
                  <center>{this.state.actualPage != 0 ? <a class="btn btn-primary" href={"/requests/received/"+parseInt(this.state.actualPage-1)}>Anterior</a> : <p></p>}
                  {this.state.pages.map((page) => {
                  return(
                    <a style={{color:this.state.actualPage == page ? "white" : "black", backgroundColor:this.state.actualPage == page ? "#007bff" : ""}} class="pag" href={"/requests/received/"+page}>{page}</a>
                  )
                })}
                {this.state.actualPage != this.state.pages.length-1 ? <a class="btn btn-primary" href={"/requests/received/"+parseInt(this.state.actualPage+1)}>Siguiente</a> : <p></p>}</center>
                :
                <p></p>
                }<br></br>
            </div>
          );
    }

    async acceptRequest(id, title, actualPage) {
      const conf = confirm("¿Está seguro de que quiere aceptarla? Las otras peticiones a " + title + " se rechazarán.")
      if(conf) {
        const res = await requestService.accept(id)
        window.location.replace("/requests/received/"+actualPage)
      }
    }

    async rejectRequest(id, actualPage) {
      const conf = confirm("¿Está seguro de que quiere rechazarla? Esta acción no es reversible.")
      if(conf) {
        const res = await requestService.reject(id)
        window.location.replace("/requests/received/"+actualPage)
      }
    }
}
