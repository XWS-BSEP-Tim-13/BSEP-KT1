import { useNavigate } from 'react-router-dom';

import classes from './Registration.module.css';

function Registration(props) {
    const navigate = useNavigate();

    function submitHandler(event) {
        event.preventDefault();
        console.log('Submited');
        navigate('/home');
    }

    return(
        <div className={classes.login}>
            <h1>Register now!</h1>
            <form onSubmit={submitHandler} className={classes.form}>

                <div className={classes.formItem}>
                    <input type='text' required placeholder='First name' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Last name' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Age' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Address' />
                </div>
                <div className={classes.formItem}>
                    <input type='text' required placeholder='Company' />
                </div>
                <div className={classes.formItem}>
                    <input type='password' required placeholder='Password' />
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