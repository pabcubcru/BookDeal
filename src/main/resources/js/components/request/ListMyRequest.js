import React, { Component } from 'react';
import requestService from "../services/Request";

export default class List extends Component {

  constructor(){
    super();
    this.state = {
      requests: []
    }
  }
    
  async componentDidMount() {
    const res = await requestService.listMyRequests()

    this.setState({requests:res.requests})
  }

    render() {
        return (
            <div>
                <div>
                  <p style={{color: "#007bff"}}><strong>{this.state.messageCorrect}</strong></p><p class='text-danger'><strong>{this.state.messageError}</strong></p>
                </div>
                {this.state.requests.map((request) => {
                    return(
                    <div style={{backgroundImage: "url(https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg)", 
                    backgroundSize: "auto auto" ,  fontWeight: "bold", padding: "60px", paddingTop:"20px", marginBlock:"30px", margin:"0px 20px 20px 0px"}}>
                    <center>
                    <h5><strong>{request.username1}</strong></h5>
                    <p>{request.username2}</p>
                    <p>{request.idBook1}</p>
                        
                    </center>
                    </div>)
                })}
            </div>
          );
    }

    async addFavouriteBook(id) {
      const res = await userFavouriteBook.addFavouriteBook(id)
      window.location.replace("/")
      
    }
}
