import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
//import axios from 'axios';
import { Route, Routes } from 'react-router-dom';

import Homepage from './pages/Homepage/Homepage';
import Mainpage from './pages/Mainpage/Mainpage';
import Navbar from './components/Navbar/Navbar';

import './App.css';



function App() {
    const location = useLocation();

    useEffect(() => {
        /*axios.get('http://localhost:8081/')
            .then((res) => {
                console.log(res.data)
            })*/
    }, [])

    return (
        <div className="App">
            { location.pathname !== '/' ? <Navbar /> : null }
            <Routes>
                <Route path='/' element={<Mainpage />} />
                <Route path='/home' element={<Homepage />} />
            </Routes>
        </div>
    );
}

export default App;
