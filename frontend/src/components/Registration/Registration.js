import { useNavigate } from 'react-router-dom';
import { useEffect, useState, useRef } from 'react';
import { useDispatch } from "react-redux";
import { login } from "../../features/user"
import axios from 'axios';

import RegistrationService from "../../services/RegistrationService";
import AuthentificationService from "../../services/AuthentificationService";

import classes from './Registration.module.css';

function Registration(props) {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [countries, setCountries] = useState([]);

    const commonNameInputRef = useRef();
    const emailInputRef = useRef();
    const organizationInputRef = useRef();
    const organizationUnitInputRef = useRef();
    const countryCodeInputRef = useRef();
    const passwordInputRef = useRef();
    const isSubsystemInputRef = useRef();

    useEffect(() => {
        axios.get('http://api.worldbank.org/v2/country?format=json')
            .then((response) => {
                setCountries(response.data[1]);
            })
    }, [])

    function registrationHandler(event) {
        event.preventDefault();

        const registrationEntityDTO = {
            commonName: commonNameInputRef.current.value,
            email: emailInputRef.current.value,
            organization: organizationInputRef.current.value,
            organizationUnit: organizationUnitInputRef.current.value,
            countryCode: countryCodeInputRef.current.value,
            password: passwordInputRef.current.value,
            role: ''
        }

        if(isSubsystemInputRef.current.checked) {
            registrationEntityDTO.role = 'ROLE_SUBSYSTEM';
            console.log(isSubsystemInputRef.current.checked)
        } else {
            registrationEntityDTO.role = 'ROLE_USER';
        }

        RegistrationService.register(registrationEntityDTO)
        .then(() => {
            AuthentificationService.login({
                email: registrationEntityDTO.email,
                password: registrationEntityDTO.password
            })
            .then((response) => {
                dispatch(login(response.data));
                navigate('/home');
            })
        })
        .catch(() => {
            console.log("Server error.")
        })
    }

    return (
        <div className={classes.login}>
            <h1>Register now!</h1>
            <form onSubmit={registrationHandler} className={classes.form}>

                <div className={classes.formItem}>
                    <input type='text' required placeholder='Common name' ref={commonNameInputRef}/>
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Email' ref={emailInputRef}/>
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Organization' ref={organizationInputRef}/>
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Organization Unit' ref={organizationUnitInputRef}/>
                </div>

                <select name="CountryCode" defaultValue="" className={classes.select} ref={countryCodeInputRef}>
                    <option value="" disabled>Country Code</option>
                    { countries.map((country) => {
                        return <option key={country.id} value={country.iso2Code} >{country.iso2Code} ({country.name})</option>;
                    })}
                </select>

                <div className={classes.formItem}>
                    <input type='password' required placeholder='Password' ref={passwordInputRef}/>
                </div>
                <div className={classes.formItem}>
                    <div className={classes.checkbox}>
                        <input type='checkbox' id="isSubsystem" ref={isSubsystemInputRef}/>
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