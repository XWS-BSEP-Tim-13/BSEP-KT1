import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useState } from "react";
import { login } from "../../features/user"

import classes from "./Login.module.css";
import AuthentificationService from "../../services/AuthentificationService";


function Login(props) {
    const [loginError, setLoginError] = useState("");

    const dispatch = useDispatch();
    const navigate = useNavigate();

    
    function submitHandler(event) {
        event.preventDefault();
        
        const loginDto = {
            email: event.target[0].value,
            password: event.target[1].value
        }
        
        AuthentificationService.login(loginDto)
        .then((response) => {
            dispatch(login(response.data));
            navigate("/home");
        })
        .catch(() => {
            setLoginError("Wrong username or password! Try again.");
        })
    }

    return (
        <div className={classes.login}>
            <h1>Log in</h1>
            <form onSubmit={submitHandler} className={classes.form}>
                <div className={classes.formItem}>
                    <input type="text" required placeholder="Username" />
                </div>
                <div className={classes.formItem}>
                    <input type="password" required placeholder="Password" />
                </div>
                <p className={classes.errorMessage}>{loginError}</p>

                <button className={classes.buttonLogIn}>Log in</button>
                <a href="/#" className={classes.registerLink} onClick={() => props.changePage(false)} >
                    Don't have an account? Register here.
                </a>
            </form>
        </div>
    );
}

export default Login;
