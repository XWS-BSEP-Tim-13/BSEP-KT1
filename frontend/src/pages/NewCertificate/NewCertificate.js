import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCertificate } from "@fortawesome/free-solid-svg-icons";

import { useSelector } from "react-redux";
import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { axiosInstance } from "../../api/AxiosInstance";
import axios from "axios";

import classes from './NewCertificate.module.css';

function NewCertificate() {
    const [possibleSubjects, setPossibleSubjects] = useState([]);
    const [possibleIssuers, setPossibleIssuers] = useState([]);
    const [possibleIssuersCertificates, setPossibleIssuersCertificates] = useState([]);
    const [selectedSubject, setSelectedSubject] = useState({});
    const [purposes, setPurposes] = useState([]);

    const user = useSelector((state) => state.user.value);

    const navigate = useNavigate();

    const subjectEntityId = useRef();
    const issuerEntityId = useRef();
    const signerCertificateId = useRef();
    const validFrom = useRef();
    const expiringDate = useRef();
    const certificateType = useRef();

    useEffect(() => {
        axiosInstance.get(`http://localhost:8081/certification-entity/subjects`)
        .then((response) => {
            setPossibleSubjects(response.data);
        })

    }, []) // eslint-disable-line react-hooks/exhaustive-deps

    useEffect(() => {
        if (possibleIssuers.length > 0 && user.role !== 'ROLE_ADMIN') {
            const selectedIssuerEmail = user.email;
            const selectedIssuerObject = possibleIssuers.filter((issuer) => issuer.email == selectedIssuerEmail);
            setPossibleIssuersCertificates(selectedIssuerObject[0].certificates);
        }
    }, [possibleIssuers]) // eslint-disable-line react-hooks/exhaustive-deps

    function addNewCertificateHandler(event) {
        event.preventDefault();
        console.log(possibleIssuersCertificates);

        var newCertificate = {}
        if (!selectedSubject.rootForOrganizationExists) {
            newCertificate = {
                validFrom: validFrom.current.value,
                expiringDate: expiringDate.current.value,
                certificateType: "ROOT",
                purposes: purposes,
                subjectEntityId: subjectEntityId.current.value,
                signerCertificateId: 1
            }
        } else {
            newCertificate = {
                validFrom: validFrom.current.value,
                expiringDate: expiringDate.current.value,
                certificateType: certificateType.current.value,
                purposes: purposes,
                subjectEntityId: subjectEntityId.current.value,
                signerCertificateId: signerCertificateId.current.value
            }
        }

        console.log(newCertificate);
        axios.post(`http://localhost:8081/certificate/`, newCertificate, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                'Authorization': `Bearer ${user.accessToken}`
            },
        })
        .then(() => {
            navigate("/allCertificates");
        })
        .catch((error) => {
            console.log(error.data);
        });
    }

    function subjectSelectedHandler() {
        const selectedSubjectId = subjectEntityId.current.value;
        const selectedSubjectObject = possibleSubjects.filter((subject) => subject.id == selectedSubjectId);
        setSelectedSubject(selectedSubjectObject[0]);
        console.log(selectedSubject);

        if (selectedSubjectObject[0].rootForOrganizationExists) {
            axios.get(`http://localhost:8081/certification-entity/issuers/${selectedSubjectObject[0].organization}`, {
                headers: {
                    'Content-Type': 'application/json',
                    Accept: 'application/json',
                    'Authorization': `Bearer ${user.accessToken}`
                },
            }).then((response) => {
                setPossibleIssuers(response.data);
            })
        }
    }

    function issuerSelectedHandler() {
        const selectedIssuerId = issuerEntityId.current.value;
        const selectedIssuerObject = possibleIssuers.filter((issuer) => issuer.id == selectedIssuerId);
        setPossibleIssuersCertificates(selectedIssuerObject[0].certificates);
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
                        <select name="Subject" defaultValue="" ref={subjectEntityId} onChange={subjectSelectedHandler}>
                            <option value="" disabled>Subject</option>
                            {possibleSubjects.map((possibleSubject) => {
                                if ((user.role !== 'ROLE_ADMIN' && possibleSubject.rootForOrganizationExists && user.organization === possibleSubject.organization && possibleSubject.email !== user.email)
                                    || user.role === 'ROLE_ADMIN') {
                                    return <option key={possibleSubject.email} value={possibleSubject.id}>
                                        {possibleSubject.commonName} ({possibleSubject.email})
                                    </option>
                                } else return null;
                            })
                            }
                        </select>

                        {(user.role === 'ROLE_ADMIN' && selectedSubject.rootForOrganizationExists) ?
                            <select name="Issuer" defaultValue="" ref={issuerEntityId} onChange={issuerSelectedHandler}>
                                <option value="" disabled>Issuer</option>
                                {possibleIssuers.map((issuer) => {
                                    if(issuer.email !== selectedSubject.email) {
                                        return <option key={issuer.email} value={issuer.id}>
                                            {issuer.commonName} ({issuer.email})
                                        </option>;
                                    } else return null;
                                })
                                }
                            </select> : null}
                        {(user.role === 'ROLE_ADMIN' && !selectedSubject.rootForOrganizationExists) ?
                            <input type='text' disabled value="Issuer: Admin" /> : null
                        }
                        {user.role !== 'ROLE_ADMIN' ?
                            <input type='text' disabled value={`Issuer: ${user.commonName}`} /> : null
                        }

                        {(!selectedSubject.rootForOrganizationExists && user.role === 'ROLE_ADMIN') ?
                            <input type='text' disabled value="Signer Certificate: 1 (admin)" /> :
                            <select name="Signer Certificate" defaultValue="" ref={signerCertificateId}>
                                <option value="" disabled>Signer Certificate</option>
                                {possibleIssuersCertificates.map((certificate) => {
                                    return <option key={certificate.id} value={certificate.id}>
                                        SN {certificate.id} (type: {certificate.type})
                                    </option>;
                                    })
                                }
                            </select>
                        }

                        {(user.role === 'ROLE_ADMIN' && selectedSubject.rootForOrganizationExists) ?
                            <select name="CertificateType" defaultValue="" ref={certificateType}>
                                <option value="" disabled>Certificate type</option>
                                <option value="INTERMEDIATE">Intermediate</option>
                                <option value="END_ENTITY">End Entity</option>
                            </select> : null
                        }
                        {(user.role === 'ROLE_ADMIN' && !selectedSubject.rootForOrganizationExists) ?
                            <input type='text' disabled value="Certificate Type: Root" /> : null
                        }
                        {user.role !== 'ROLE_ADMIN' ?
                            <select name="CertificateType" defaultValue="" ref={certificateType}>
                                <option value="" disabled>Certificate type</option>
                                <option value="INTERMEDIATE">Intermediate</option>
                                <option value="END_ENTITY">End Entity</option>
                            </select> : null
                        }

                        <input type='text' required placeholder='Valid From' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")} ref={validFrom} />
                        <input type='text' required placeholder='Valid To' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")} ref={expiringDate} />
                    </div>
                    <div className={classes.formColumn}>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="provesIdentity" onChange={() => setPurposes([...purposes, "Proves the identity to a remote computer"])} />
                            <label htmlFor="provesIdentity">Proves the identity to a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="ensuresIdentity" onChange={() => setPurposes([...purposes, "Ensures the idnetity of a remote computer"])} />
                            <label htmlFor="ensuresIdentity">Ensures the idnetity of a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="encryptedData" onChange={() => setPurposes([...purposes, "Allows data on disk to be encrypted"])} />
                            <label htmlFor="encryptedData">Allows data on disk to be encrypted</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="emailMessages" onChange={() => setPurposes([...purposes, "Protects e-mail messages"])} />
                            <label htmlFor="emailMessages">Protects e-mail messages</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="secureCommunication" onChange={() => setPurposes([...purposes, "Allows secure communication on the Internet"])} />
                            <label htmlFor="secureCommunication">Allows secure communication on the Internet</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="signData" onChange={() => setPurposes([...purposes, "Allows data to be signed with the current time"])} />
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

