import { useState } from 'react';

import classes from './CertificateInfoDetails.module.css';

function CertificateInfoDetails(props) {
    const [selectedField, setSelectedField] = useState(null);
    const [value, setValue] = useState('');

    function selectFieldHandler(field, value) {
        setSelectedField(field);
        setValue(value);
    }

    return (
        <div className={classes.page}>
            <p className={classes.labelBold}>Fields:</p>
            <div className={classes.fields}>
                <div className={classes.fieldsColumn}>
                    <button className={selectedField === 'serialNumber' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('serialNumber', props.certificate.serialNumber)} >Serial Number</button>
                    <button className={selectedField === 'signatureAlg' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('signatureAlg', props.certificate.signatureAlgorithm)} >Signature Algorithm</button>
                    <button className={selectedField === 'signatureHash' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('signatureHash', props.certificate.signatureHashAlgorithm)} >Signature Hash Algorithm</button>
                    <button className={selectedField === 'publicKey' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('publicKey', props.certificate.publicKey)} >Public Key</button>
                </div>
                <div className={classes.fieldsColumn}>
                    <button className={selectedField === 'issuer' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('issuer', props.certificate.issuer)} >Issuer</button>
                    <button className={selectedField === 'subject' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('subject', props.certificate.subject)} >Subject</button>
                    <button className={selectedField === 'validFrom' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('validFrom', props.certificate.validFrom)} >Valid From</button>
                    <button className={selectedField === 'validTo' ? classes.fieldSelected : classes.field}
                        onClick={() => selectFieldHandler('validTo', props.certificate.validTo)} >Valid To</button>
                </div>
            </div>
            <p className={classes.labelBold}>Value:</p>
            <div className={classes.value}>
                {value}
            </div>
        </div>
    );
}

export default CertificateInfoDetails;