import { useState, useEffect } from 'react';

import Backdrop from '../Backdrop/Backdrop.js';
import CertificateInfo from '../CertificateInfo/CertificateInfo.js';
import classes from './CertificatesList.module.css';

function CertificatesList(props) {
    const [showCertificateInfo, setShowCertificateInfo] = useState(false);
    const [certificateId, setCertificateId] = useState('');
    const [isListEmpty, setIsListEmpty] = useState(true);

    useEffect(() => {
        props.certificates.forEach((certificate) => {
            if(certificate.type === props.selectedType) {
                setIsListEmpty(false);
            }
        })
    }, [props.certificates, props.selectedType]);

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
                    
                        return <div className={classes.certificate} key={certificate.subject}
                            onClick={() => showCertificateInfoHandler(certificate.id)}>
                            <img src={require('../../images/cert2.png')} alt='Certificate' />
                            <p>Subject: {certificate.subject}</p>
                            <p>Issuer: {certificate.issuer}</p>
                        </div>
                })
            }
            { props.certificates.length ==0 ? <p>There is nothing here...</p> : null }
            { showCertificateInfo ? <Backdrop click={closeCertificateInfoHandler}/> : null }
            { showCertificateInfo ? <CertificateInfo certificateId={certificateId} click={closeCertificateInfoHandler}/> : null }
        </div>
    );
}

export default CertificatesList;