import classes from './CertificateInfoGeneral.module.css';

function CertificateInfoGeneral(props) {
    return (
        <div className={classes.page}>
            <div className={classes.title}>
                <img src={require('../../images/cert2.png')} alt='Certificate' className={classes.image} />
                <h2 className={classes.labelBold}>Certificate Information</h2>
            </div>
            <div className={classes.purposes}>
                <p className={classes.labelBold}>This certificate is intended for the following purpose(s):</p>
                <ul>
                    {props.certificate.purposes.map((purpose) => {
                        return <li>{purpose}</li>
                    })}
                </ul>
                <p className={classes.label}>* Refer to the certification authority's statement for details.</p>
            </div>
            <div className={classes.info}>
                <p className={classes.labelBold}>Issued to: </p>&ensp;
                <p className={classes.label}> {props.certificate.subject} </p>
            </div>
            <div className={classes.info}>
                <p className={classes.labelBold}>Issued by: </p>&ensp;
                <p className={classes.label}> {props.certificate.issuer} </p>
            </div>
            <div className={classes.info}>
                <p className={classes.labelBold}>Valid from </p>&ensp;
                <p className={classes.label}> {props.certificate.validFrom} </p>&ensp;
                <p className={classes.labelBold}>to</p>&ensp;
                <p className={classes.label}> {props.certificate.validTo} </p>
            </div>
        </div>
    );
}

export default CertificateInfoGeneral;