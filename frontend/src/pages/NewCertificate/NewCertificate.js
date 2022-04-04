import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faKey } from "@fortawesome/free-solid-svg-icons";

import classes from './NewCertificate.module.css';

function NewCertificate() {

    return (
        <div className={classes.page}>
            <div className={classes.title}>
                <FontAwesomeIcon icon={faKey} />
                <h1 className={classes.titleText}>Add New Certificate</h1>
            </div>

            <form className={classes.form}>
                <div className={classes.formRow}>
                    <div className={classes.formColumn}>
                        <input type='text' required placeholder='Subject' />
                        <select name="Issuer" defaultValue="">
                            <option value="" disabled>Issuer</option>
                            <option value="first">First option</option>
                            <option value="second">Second option</option>
                            <option value="third">Third option</option>
                        </select>
                        <input type='text' required placeholder='Valid From' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")}/>
                        <input type='text' required placeholder='Valid To' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")}/>
                    </div>
                    <div className={classes.formColumn}>
                        <input type='text' required placeholder='Signature Algorithm' />
                        <input type='text' required placeholder='Signature Hash Algorithm' />
                        <input type='text' required placeholder='Public Key' />
                        <input type='text' required placeholder='Signature' />
                    </div>
                </div>
                <button className={classes.button}>Create!</button>
            </form>
        </div>
    );
}

export default NewCertificate;

