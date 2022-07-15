import { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import RegistrationService from "../../services/RegistrationService";
import { useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import schema from '../../validationSchemas/RegisterValidationSchema';

import classes from './Registration.module.css';

function Registration(props) {

    const [countries, setCountries] = useState([]);

    const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(schema)
    });

    const [serverError, setServerError] = useState(false);

    const isSubsystemInputRef = useRef();

    useEffect(() => {
        axios.get('https://api.worldbank.org/v2/country?format=json')
            .then((response) => {
                setCountries(response.data[1]);
            })
    }, [])

    function submitHandler(data) {

        const registrationEntityDTO = {
            commonName: data.commonName,
            email: data.email,
            organization: data.organization,
            organizationUnit: data.organizationUnit,
            countryCode: data.countryCode,
            password: data.password,
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
            props.navigateToLinkSent();
        })
        .catch((error) => {
            if (error.response.data.message.includes("User with this email already exists")) {
                setServerError(true);
            }
        })
    }

    return (
        <div className={classes.login}>
            <h1>Register now!</h1>
            <form onSubmit={handleSubmit(submitHandler)} className={classes.form}>

                <div className={classes.formItem}>
                    <input type='text' placeholder='Common name' {...register("commonName")}/>
                </div>
                <div className={classes.errorMessage}>{errors.commonName?.message}</div>

                <div className={classes.formItem}>
                    <input type='text' placeholder='Email' {...register("email")}/>
                </div>
                <div className={classes.errorMessage}>{errors.email?.message}</div>

                <div className={classes.formItem}>
                    <input type='text' placeholder='Organization' {...register("organization")}/>
                </div>
                <div className={classes.errorMessage}>{errors.organization?.message}</div>

                <div className={classes.formItem}>
                    <input type='text' placeholder='Organization Unit' {...register("organizationUnit")}/>
                </div>
                <div className={classes.errorMessage}>{errors.organizationUnit?.message}</div>

                <select name="CountryCode" defaultValue="" className={classes.select} {...register("countryCode")} >
                    <option value="" disabled>Country Code</option>
                    { countries.map((country) => {
                        return <option key={country.id} value={country.iso2Code} >{country.iso2Code} ({country.name})</option>;
                    })}
                </select>
                <div className={classes.errorMessage}>{errors.countryCode?.message}</div>

                <div className={`${classes.formItem} ${classes.password}`}>
                    <input type='password' placeholder='Password' {...register("password")}/>
                    <div className={classes.tooltip}>Strong password must be at least 10 characters long, including at least 1 uppercase letter, 1 lowercase letter, 1 numeric character and 1 special character.</div>
                </div>
                <div className={classes.errorMessage}>{errors.password?.message}</div>

                <div className={classes.formItem}>
                    <input type='password' placeholder='Confirm Password' {...register("confirmPassword")}/>
                </div>
                <div className={classes.errorMessage}>{errors.confirmPassword?.message}</div>

                <div className={classes.formItem}>
                    <div className={classes.checkbox}>
                        <input type='checkbox' id="isSubsystem" ref={isSubsystemInputRef}/>
                        <label htmlFor="isSubsystem">Registration of a subsystem?</label>
                    </div>
                </div>

                <div className={classes.errorMessage}>{serverError ? "User with this email already exists!" : null}</div>

                <button className={classes.buttonLogIn}>Register</button>

                <a href="/#" className={classes.registerLink} onClick={() => props.navigateToLogin()}>
                    Already have an account? Log in here!
                </a>
            </form>
        </div>
    );
}

export default Registration;