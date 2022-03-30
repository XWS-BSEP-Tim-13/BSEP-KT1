import { useNavigate } from 'react-router-dom';

import classes from './Login.module.css';


function Login(props) {
    const navigate = useNavigate();

    function submitHandler(event) {
        event.preventDefault();
        console.log('Submited');
        navigate('/home');
    }

    return (
        <div className={classes.login}>
            <h1>Log in</h1>
            <form onSubmit={submitHandler} className={classes.form}>

                <div className={classes.formItem}>
                    <input type='text' required placeholder='Username' />
                </div>
                <div className={classes.formItem}>
                    <input type='password' required placeholder='Password' />
                </div>

                <button className={classes.buttonLogIn}>Log in</button>
                <a href='/#' className={classes.registerLink} onClick={() => props.changePage(false)}>
                    Don't have an account? Register here.
                </a>
            </form>
        </div>
    );
}

export default Login;