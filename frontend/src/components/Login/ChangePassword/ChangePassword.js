import React ,{useEffect} from 'react'
import classes from './ChangePassword.module.css';
import {useForm} from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {useParams} from "react-router-dom";
import AuthentificationService from '../../../services/AuthentificationService';
const ChangePassword = () => {
    
    const navigate = useNavigate();
    const {register,handleSubmit, formState: { errors },watch} = useForm({})
    const {token} = useParams()

    const onSubmit = handleSubmit((data) =>{
        console.log(data)
        const dto ={
            password : data.password,
            confirmPassword : data.confirmPassword,
            token : token
        }
        console.log(dto)
        AuthentificationService.changePassword(dto).then(resp=>{
              navigate(`/`);
        })
    })

    function back(){
        navigate(`/`);
    }

  return (
<div className={classes.page}>
    <div className={classes.login}>
    <h1>Choose a new Password</h1>
    <form onSubmit={onSubmit} className={classes.form}>
        <div className={classes.formItem}>
        <input
                {...register("password", {required: {value:true,message:'Password is required'},pattern:{value:  /^(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^&*])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{8,}$/,message:'Password should contain one upper case letter, one down case, one number and one special character!'}})}
                type="password" 
                placeholder="New password"
                 />
                 {
                   errors.password && <div className={classes.error}><label>{errors.password.message}</label></div>
                }
        </div>
        <div className={classes.formItem}>
        <input
                 {...register("confirmPassword", {
                    required: {value:true,message:'Password is required'},
                    validate: (val) => {
                      if (watch('password') != val) {
                        return "Your passwords do no match";
                      }
                    },
                   })}
                type="password" 
                placeholder="Retype new Password"
                 />
                 {
                   errors.confirmPassword && <div className={classes.error}><label>{errors.confirmPassword.message}</label></div>
                }
        </div>
        <button className={classes.buttonLogIn}>Reset password</button>
        <button onClick={back}  className={classes.buttonBack}>Back</button>
    </form>
</div>  
<div className={classes.image}></div>
</div>
  )
}

export default ChangePassword