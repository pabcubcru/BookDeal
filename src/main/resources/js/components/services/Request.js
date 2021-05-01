const baseUrl = "http://localhost:8080/requests"
import axios from "axios";
const request = {}

request.create = async(state) => {

    const dataPost = {
        username1:"",
        username2:"",
        idBook1: state.fieldIdBook1,
        idBook2:"",
        status:"",
        action:state.fieldAction,
        pay: state.fieldPay,
        comment: state.fieldComment
    }

    const urlPost = baseUrl+"/"+state.fieldIdBook2+"/new"
    const res = await axios.post(urlPost, dataPost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

request.listMyRequests = async(page) => {

    const urlGet = baseUrl+"/my-requests?page="+page
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

request.listReceivedRequests = async(page) => {

    const urlGet = baseUrl+"/received-requests?page="+page
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

request.cancel = async(id) => {

    const urlCancel = baseUrl+"/"+id+"/cancel"
    const res = await axios.get(urlCancel)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

request.delete = async(id) => {

    const urlDelete = baseUrl+"/"+id+"/delete"
    const res = await axios.delete(urlDelete)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

request.accept = async(id) => {

    const urlCancel = baseUrl+"/"+id+"/accept"
    const res = await axios.get(urlCancel)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

request.reject = async(id) => {

    const urlCancel = baseUrl+"/"+id+"/reject"
    const res = await axios.get(urlCancel)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

export default request