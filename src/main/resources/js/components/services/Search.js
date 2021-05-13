const baseUrl = "http://localhost:8080/search";
import axios from "axios";
const search = {};

search.searchBook = async (query, page, type) => {
  const urlGet = baseUrl + "/q/" + type + "/" + query + "?page=" + page;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

search.searchTitles = async (query) => {
  const urlGet = baseUrl + "/titles/" + query;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

search.postSearch = async (state) => {
  const dataPost = {
    number1: state.fieldNumber1,
    number2: state.fieldNumber2,
    type: state.selectSearch,
    text: state.fieldText,
  };

  const res = await axios
    .post(baseUrl, dataPost)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

search.findLast = async () => {
  const urlGet = baseUrl + "/last";
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

export default search;
