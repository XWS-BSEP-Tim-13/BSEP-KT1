import classes from './AllCertificates.module.css';

var certificates = [
    {
        subject: 'Subject 1',
        issuer: 'Issuer 1'
    },
    {
        subject: 'Subject 2',
        issuer: 'Issuer 2'
    },
    {
        subject: 'Subject 3',
        issuer: 'Issuer 3'
    },
    {
        subject: 'Subject 4',
        issuer: 'Issuer 4'
    },
    {
        subject: 'Subject 5',
        issuer: 'Issuer 5'
    },
    {
        subject: 'Subject 6',
        issuer: 'Issuer 6'
    },
    {
        subject: 'Subject 7',
        issuer: 'Issuer 7'
    },
    {
        subject: 'Subject 8',
        issuer: 'Issuer 8'
    },
    {
        subject: 'Subject 9',
        issuer: 'Issuer 9'
    }
]

function AllCertificates() {
    return (
        <div className={classes.page}>
            <div className={classes.options}>
                <button className={classes.option}>End-Entity</button>
                <button className={classes.option}>CA</button>
                <button className={classes.option}>Root</button>
            </div>
            <div className={classes.certificates}>
                {
                    certificates.map((certificate) => {
                        return <div className={classes.certificate} key={certificate.subject}>
                            <img src={require('../../images/certificate.png')} alt='Certificate'/>
                            <p>Subject: {certificate.subject}</p>
                            <p>Issuer: {certificate.issuer}</p>
                        </div>
                    })
                }
            </div>
        </div>
    );
}

export default AllCertificates;
