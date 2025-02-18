import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, TextInput} from "@mantine/core";
import {Account} from "../../types/account";
import AccountService from '../../api/services/AccountService';

interface AddAccountFormProps {
    onSuccess?: (newAccount: Account) => void
}

const AddAccountPage = ({onSuccess}: AddAccountFormProps) => {
    const [accountData, setAccountData] = useState<Account>({id: "", name: "", accountNumber: "", type: "CHECKING"});

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setAccountData((prev) => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        AccountService.addAccount(accountData)
            .then(response => {
                onSuccess && onSuccess(response);
                alert("Account added successfully! id=" + response.id);
                setAccountData({id: "", name: "", accountNumber: "", type: "CHECKING"});
            })
            .catch(error => {
                let data = error.response.data;
                if (data && data.message && data.details) {
                    const errorMessage = data.message || "An error occurred.";
                    const errorDetails = data.details ? data.details.join("\n") : "";

                    alert(`${errorMessage}\n${errorDetails}`);
                } else {
                    alert("An unexpected error occurred. Please try again.");
                }
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, margin: "auto", textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <TextInput autoComplete="new-name"
                           label={"Account Name"}
                           name="name"
                           radius="md"
                           mb="sm"
                           type="text"
                           placeholder="Name"
                           value={accountData.name}
                           onChange={handleChange}
                           required
                />
                <TextInput autoComplete="new-accountNumber"
                           label={"Account Number"}
                           name="accountNumber"
                           radius="md"
                           mb="sm"
                           type="text"
                           placeholder="Account Number"
                           value={accountData.accountNumber}
                           onChange={handleChange}
                           required
                />
                <TextInput autoComplete="new-type"
                            label={"Account Type"}
                           name="type"
                           radius="md"
                           mb="sm"
                           type="text"
                           placeholder="Account Type"
                           value={accountData.type}
                           onChange={handleChange}
                           required
                />
                <Button variant="filled" fullWidth color="teal" radius="md" type="submit" style={{marginTop: "40px"}}>
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddAccountPage;
