import * as yup from "yup";

const codeRegex = /^([0-9]{6})$/;

const schema = yup.object().shape({
    code: yup.string()
        .required("Code is required.")
        .matches(codeRegex, "Code should be six digits."),
});

export default schema;