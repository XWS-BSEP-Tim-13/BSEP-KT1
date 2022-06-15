import React, { useState } from 'react'
import {useDispatch} from 'react-redux';
import { login } from '../../../features/user'
import { useNavigate } from 'react-router-dom';
import classes from "./PasswordlessCode.module.css"
import schema from '../../../validationSchemas/VerifyEmailValidationSchema'
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { passwordlessLogin } from '../../../api/PasswordlessApi';

const PasswordlessCode = ({navigateToLogin, email}) => {
    const [error, setError] = useState('')
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(schema)
    });

    function onLogin(data) {
        setError("");
        const loginRequest = {
            email: email,
            code: data.code
        }
        passwordlessLogin(loginRequest).then((res) => {
            localStorage.setItem("token-ls", res.data.token);
            dispatch(login(res.data))
            navigate('/home');
        }).catch((err) => {
            console.log(err.response.data);
            if (err.response.data.message === 'bad code')
                setError("Incorrect code.");
            if (err.response.data.message === 'expired code')
                setError("Code expired.");
        })
    }

    return (
        <div className={classes.login}>
            <h1>Verify your email</h1>
            <p className={classes.subtitle}>
                Six digit code has been sent to specified email address.
            </p>
            <form className={classes.form} onSubmit={handleSubmit(onLogin)}>
                <div className={classes.formItem}>
                    <input type='text' value={email} disabled />
                </div>
                <div className={classes.formItem}>
                    <input type='password' required placeholder='6-digit code' {...register("code")} />
                </div>
                <div className={classes.errorMessage}>
                    {error}
                </div>
                <div className={classes.errorMessage}>{errors.code?.message} </div>
                <input type="submit" className={classes.buttonLogIn} value="Log In" />
                <a href='/#' className={classes.backToLogin} onClick={() => navigateToLogin()}>
                    Back to login?
                </a>
            </form>
        </div>
    )
}

export default PasswordlessCode