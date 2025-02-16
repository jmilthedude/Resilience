import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Input, Title} from "@mantine/core";
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
                alert("Account added successfully! id="+ response.id);
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
            <Title order={2} mb="lg">
                Add Account
            </Title>
            <form onSubmit={handleSubmit}>
                <Input autoComplete="new-name"
                       name="name"
                       radius="lg"
                       mb="sm"
                       type="text"
                       placeholder="Name"
                       value={accountData.name}
                       onChange={handleChange}
                       required
                />
                <Input autoComplete="new-accountNumber"
                       name="accountNumber"
                       radius="lg"
                       mb="sm"
                       type="text"
                       placeholder="Account Number"
                       value={accountData.accountNumber}
                       onChange={handleChange}
                       required
                />
                <Input autoComplete="new-type"
                       name="type"
                       radius="lg"
                       mb="sm"
                       type="text"
                       placeholder="Account Type"
                       value={accountData.type}
                       onChange={handleChange}
                       required
                />
                <Button variant="filled" fullWidth color="teal" radius="lg" type="submit">
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddAccountPage;
