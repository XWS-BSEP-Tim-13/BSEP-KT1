import { useState } from 'react';
import { useSelector } from 'react-redux';

import CertificatesList from '../../components/CertificatesList/CertificatesList';
import classes from './AllCertificates.module.css';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

var certificates = [
    {
        id: 1,
        subject: 'Subject 1',
        issuer: 'Issuer 1',
        type: 'root'
    },
    {
        id: 2,
        subject: 'Subject 2',
        issuer: 'Issuer 2',
        type: 'root'
    },
    {
        id: 3,
        subject: 'Subject 3',
        issuer: 'Issuer 3',
        type: 'root'
    },
    {
        id: 4,
        subject: 'Subject 4',
        issuer: 'Issuer 4',
        type: 'ca'
    },
    {
        id: 5,
        subject: 'Subject 5',
        issuer: 'Issuer 5',
        type: 'ca'
    },
    {
        id: 6,
        subject: 'Subject 6',
        issuer: 'Issuer 6',
        type: 'ca'
    },
    {
        id: 7,
        subject: 'Subject 7',
        issuer: 'Issuer 7',
        type: 'ee'
    },
    {
        id: 8,
        subject: 'Subject 8',
        issuer: 'Issuer 8',
        type: 'ee'
    },
    {
        id: 9,
        subject: 'Subject 9',
        issuer: 'Issuer 9',
        type: 'ee'
    }
]

function AllCertificates() {
    const [selectedType, setSelectedType] = useState('ee');

    const user = useSelector((state) => state.user.value);

    return (
        <div className={classes.page}>
            <div className={classes.options}>
                <button className={selectedType === 'ee' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('ee')}>
                    End-Entity
                </button>
                <button className={selectedType === 'ca' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('ca')}>
                    CA
                </button>
                <button className={selectedType === 'root' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('root')}>
                    Root
                </button>
            </div>
            <div className={classes.searchOptions}>
                <div className={classes.search}>
                    { user.role === 'ROLE_ADMIN' ? 
                        <input type="text" placeholder="Search Subject..." className={classes.searchInput}></input> :
                        <input type="text" placeholder="Search Serial Number..." className={classes.searchInput}></input>
                    }
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div> 
                <div className={classes.search}>
                    <input type="text" placeholder="Search Issuer..." className={classes.searchInput}></input>
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div>
                <div className={classes.search}>
                    <input type="text" placeholder="Search Organization..." className={classes.searchInput}></input>
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div>
            </div>
            <div>
                <CertificatesList certificates={certificates} selectedType={selectedType} />
            </div>
        </div>
    );
}

export default AllCertificates;
