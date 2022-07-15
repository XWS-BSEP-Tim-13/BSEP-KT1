import axios from 'axios';

const token = localStorage.getItem("token-ls");

let instance;

if (!token)
    instance = axios.create({ baseURL: 'http://localhost:8081/' });
else
    instance = axios.create({ baseURL: 'http://localhost:8081/', headers: { Authorization: 'Bearer ' + token } });

export const axiosInstance = instance;