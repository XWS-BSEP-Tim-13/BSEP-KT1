import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCertificate } from "@fortawesome/free-solid-svg-icons";

import { useSelector } from "react-redux";
import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import classes from './NewCertificate.module.css';

function NewCertificate() {
    const [possibleSubjects, setPossibleSubjects] = useState([]);
    const [purposes, setPurposes] = useState([]);

    const user = useSelector((state) => state.user.value);

    const navigate = useNavigate();

    const subjectEntityId = useRef();
    const signerCertificateId = useRef();
    const validFrom = useRef();
    const expiringDate = useRef();
    const certificateType = useRef();

    useEffect(() => {
        console.log(user)
        axios.get(`http://localhost:8081/certificate/subjects`, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                'Authorization': `Bearer ${user.accessToken}`
            },
        }).then((response) => {
            setPossibleSubjects(response.data);
        })
    }, []);

    function addNewCertificateHandler(event) {
        event.preventDefault();
        
        const newCertificate = {
            validFrom: validFrom.current.value,
            expiringDate: expiringDate.current.value,
            certificateType: certificateType.current.value,
            purposes: purposes,
            subjectEntityId: subjectEntityId.current.value,
            signerCertificateId: signerCertificateId.current.value
        }

        console.log(newCertificate);

        navigate("/allCertificates");
    }

    return (
        <div className={classes.page}>
            <div className={classes.title}>
                <FontAwesomeIcon icon={faCertificate} />
                <h1 className={classes.titleText}>Add New Certificate</h1>
            </div>

            <form className={classes.form} onSubmit={addNewCertificateHandler}>
                <div className={classes.formRow}>
                    <div className={classes.formColumn}>
                        <select name="Subject" defaultValue="" ref={subjectEntityId}>
                            <option value="" disabled>Subject</option>
                            {   possibleSubjects.map((possibleSubject) => {
                                    if((user.role !== 'ROLE_ADMIN' && possibleSubject.rootForOrganizationExists && user.organization === possibleSubject.organization)
                                        || user.role === 'ROLE_ADMIN') {
                                            return <option key={possibleSubject.email} value={possibleSubject.id}>
                                                {possibleSubject.commonName} ({possibleSubject.email})
                                            </option>
                                    } else return null;
                                }) 
                            }
                        </select>

                        {user.role === 'ROLE_ADMIN' ?
                            <select name="Issuer" defaultValue="">
                                <option value="" disabled>Issuer</option>
                                <option value="first">First option</option>
                                <option value="second">Second option</option>
                                <option value="third">Third option</option>
                            </select> :
                            <input type='text' disabled value={`Issuer: ${user.commonName}`}/>
                        }

                        <select name="Signer Certificate" defaultValue=""  ref={signerCertificateId}>
                            <option value="" disabled>Signer Certificate</option>
                            <option value="first">First option</option>
                            <option value="second">Second option</option>
                            <option value="third">Third option</option>
                        </select>

                        <select name="CertificateType" defaultValue="" ref={certificateType}>
                            <option value="" disabled>Certificate type</option>
                            {user.role === 'ROLE_ADMIN' ?
                                <option value="SELF_SIGNED">Root</option> : null
                            }
                            <option value="INTERMEDIATE">Intermediate</option>
                            <option value="END_ENTITY">End Entity</option>
                        </select>

                        <input type='text' required placeholder='Valid From' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")} ref={validFrom}/>
                        <input type='text' required placeholder='Valid To' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")} ref={expiringDate}/>
                    </div>
                    <div className={classes.formColumn}>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="provesIdentity" onChange={() => setPurposes(purposes.push("Proves the identity to a remote computer"))}/>
                            <label htmlFor="provesIdentity">Proves the identity to a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="ensuresIdentity" onChange={() => setPurposes([...purposes, "Ensures the idnetity of a remote computer"])}/>
                            <label htmlFor="ensuresIdentity">Ensures the idnetity of a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="encryptedData" onChange={() => setPurposes([...purposes, "Allows data on disk to be encrypted"])}/>
                            <label htmlFor="encryptedData">Allows data on disk to be encrypted</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="emailMessages" onChange={() => setPurposes([...purposes, "Protects e-mail messages"])}/>
                            <label htmlFor="emailMessages">Protects e-mail messages</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="secureCommunication" onChange={() => setPurposes([...purposes, "Allows secure communication on the Internet"])}/>
                            <label htmlFor="secureCommunication">Allows secure communication on the Internet</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="signData" onChange={() => setPurposes([...purposes, "Allows data to be signed with the current time"])}/>
                            <label htmlFor="signData">Allows data to be signed with the current time</label>
                        </div>
                    </div>
                </div>
                <button className={classes.button}>Create!</button>
            </form>
        </div>
    );
}

export default NewCertificate;

