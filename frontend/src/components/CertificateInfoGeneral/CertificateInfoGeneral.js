import classes from './CertificateInfoGeneral.module.css';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CertificationService from '../../services/CertificationService';

function CertificateInfoGeneral(props) {

    function downloadCertificate() {
        CertificationService.downloadCertificate(props.certificate.id)
            .then(resp => {

            })
    }
    function getDate(timestamp) {
        let date = new Date(timestamp);
        if (isNaN(timestamp)) date = new Date()

        var month = date.getMonth() + 1;
        var day = date.getDate();
        month = (month < 10 ? "0" : "") + month;
        day = (day < 10 ? "0" : "") + day;


        var str = day + "-" + month + "-" + date.getFullYear();


        return str;
    }

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
                        return <li key={purpose}>{purpose}</li>
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
                <p className={classes.label}> {getDate(props.certificate.validFrom)} </p>&ensp;
                <p className={classes.labelBold}>to</p>&ensp;
                <p className={classes.label}> {getDate(props.certificate.validTo)} </p>
            </div>
            <div className={classes.download}>
                <FontAwesomeIcon icon={faDownload} className={classes.downloadIcon} onClick={downloadCertificate} />
            </div>
        </div>
    );
}

export default CertificateInfoGeneral;