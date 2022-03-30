//import { useEffect } from 'react';
//import axios from 'axios';
import { Route, Routes } from 'react-router-dom';

import Homepage from './pages/Homepage/Homepage';
import Login from './pages/Login/Login';

import './App.css';


function App() {

  /*useEffect(() => {
    axios.get('http://localhost:8081/')
    .then((res) => {
        console.log(res.data)
    })
  }, []);*/

  return (
    <div className="App">
        <Routes>
            <Route path='/' element={<Login />} />
            <Route path='/home' element={<Homepage />} />
        </Routes>
    </div>
  );
}

export default App;
