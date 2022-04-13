import { useState } from 'react';
import { useSelector } from 'react-redux';

import Backdrop from '../Backdrop/Backdrop.js';
import CertificateInfo from '../CertificateInfo/CertificateInfo.js';
import classes from './CertificatesList.module.css';

function CertificatesList(props) {
    const [showCertificateInfo, setShowCertificateInfo] = useState(false);
    const [certificateId, setCertificateId] = useState('');

    const user = useSelector((state) => state.user.value);

    function showCertificateInfoHandler(id) {
        setCertificateId(id);
        setShowCertificateInfo(true);
    }

    function closeCertificateInfoHandler() {
        setShowCertificateInfo(false);
    }

    return (
        <div className={classes.certificates}>
            {
                props.certificates.map((certificate) => {
                        if((user.role !== 'ROLE_ADMIN' && user.email === certificate.subjectEmail) || user.role === 'ROLE_ADMIN') {
                            return <div className={classes.certificate} key={certificate.subject}
                                onClick={() => showCertificateInfoHandler(certificate.id)}>
                                <img src={require('../../images/cert2.png')} alt='Certificate' />
                                <p>Subject: {certificate.subject}</p>
                                <p>Issuer: {certificate.issuer}</p>
                            </div>
                        } else return null;
                })
            }
            { props.certificates.length === 0 ? <p>There is nothing here...</p> : null }
            { showCertificateInfo ? <Backdrop click={closeCertificateInfoHandler}/> : null }
            { showCertificateInfo ? <CertificateInfo certificateId={certificateId} click={closeCertificateInfoHandler}/> : null }
        </div>
    );
}

export default CertificatesList;