import React from 'react'
import classes from './ForgotPassword.module.css';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import AuthentificationService from '../../../services/AuthentificationService';
const ForgotPassword = (props) => {

    const navigate = useNavigate();
    const { register, handleSubmit, formState: { errors }, setValue } = useForm({})

    const onSubmit = handleSubmit((data) => {
        console.log(data.email)
        AuthentificationService.forgotPassword(data.email).then(resp => {
            setValue('email', '', { shouldValidate: false })
        })
    })
    return (
            <div className={classes.login}>
                <h1 className={classes.caption}>Forgot password?</h1>
                <form onSubmit={onSubmit} className={classes.form}>
                    <div className={classes.formItem}>
                        <input className={errors.email ? classes.errorInput : null}
                            {...register("email", { required: { value: true, message: 'Email is required' }, pattern: { value: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, message: 'Wrong email format' } })}
                            type="text"
                            placeholder="Email"
                        />
                        <div className={classes.error}>{errors.email ? errors.email.message : null }</div>
                    </div>
                    <div className={classes.buttons}>
                        <button className={classes.buttonLogIn}>Reset password</button>
                        <button onClick={() => props.changePage()}  className={classes.buttonBack}>Back</button>
                    </div>
                </form>
            </div>
    )
}

export default ForgotPassword