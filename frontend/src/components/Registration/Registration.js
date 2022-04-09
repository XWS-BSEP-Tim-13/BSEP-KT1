import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useState } from 'react';
import axios from 'axios';

import classes from './Registration.module.css';

function Registration(props) {
    const navigate = useNavigate();

    const [countries, setCountries] = useState([]);

    useEffect(() => {
        axios.get('http://api.worldbank.org/v2/country?format=json')
            .then((response) => {
                setCountries(response.data[1]);
            })
    }, [])

    function submitHandler(event) {
        event.preventDefault();
        console.log('Submited');
        navigate('/home');
    }

    return (
        <div className={classes.login}>
            <h1>Register now!</h1>
            <form onSubmit={submitHandler} className={classes.form}>

                <div className={classes.formItem}>
                    <input type='text' required placeholder='Common name' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Email' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Organization' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Organization Unit' />
                </div>
                <select name="CountryCode" defaultValue="" className={classes.select}>
                    <option value="" disabled>Country Code</option>
                    { countries.map((country) => {
                        return <option key={country.id} value={country.id} >{country.iso2Code} ({country.name})</option>;
                    })}
                </select>
                <div className={classes.formItem}>
                    <input type='password' required placeholder='Password' />
                </div>
                <div className={classes.formItem}>
                    <div className={classes.checkbox}>
                        <input type='checkbox' id="isSubsystem" />
                        <label htmlFor="isSubsystem">Registration of a subsystem?</label>
                    </div>
                </div>

                <button className={classes.buttonLogIn}>Register</button>
                <a href="/#" className={classes.registerLink} onClick={() => props.changePage(true)}>
                    Already have an account? Log in here!
                </a>
            </form>
        </div>
    );
}

export default Registration;