import * as yup from "yup";

const nameRegex = /^[a-zA-ZšŠđĐžŽčČćĆ 0-9.]+$/;
const mediumPasswordRegex = /^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})/;
const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{10,})/;

const schema = yup.object().shape({
    commonName: 
        yup.string()
        .required("Common name is required.")
        .matches(nameRegex, "Common name should be letters, digits or dots only."),
    email: 
        yup.string()
        .email("Invalid e-mail address.")
        .required("Email is required."),
    organization: 
        yup.string()
        .required("Organization is required.")
        .matches(nameRegex, "Organization should be letters, digits or dots only."),
    organizationUnit: 
        yup.string()
        .required("Organization unit is required.")
        .matches(nameRegex, "Organization unit should be letters, digits or dots only."),
    countryCode: 
        yup.string()
        .required("Country code is required."),
    password:  
        yup.string()
        .required("Password is required.")
        .matches(mediumPasswordRegex, "Password is too weak.")
        .matches(strongPasswordRegex, "Password has medium strength."),
    confirmPassword: 
        yup.string()
        .oneOf([yup.ref("password")], "Passwords do not match.")
        .required("Please confirm your password."),
    isPrivate: 
        yup.boolean()
});

export default schema;