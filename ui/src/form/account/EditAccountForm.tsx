import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, TextInput} from "@mantine/core";
import {Account} from "../../types/account";
import AccountService from '../../api/services/AccountService';

interface EditAccountFormProps {
    account: Account,
    onSuccess?: (updatedAccount: Account) => void
}

const EditAccountPage = ({account, onSuccess}: EditAccountFormProps) => {
    const [accountData, setAccountData] = useState<Account>(account);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setAccountData((prev) => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        AccountService.updateAccount(accountData.id, accountData)
            .then(() => {
                onSuccess && onSuccess(accountData)
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
            })
            .finally(() => {
                setAccountData({id: "", name: "", accountNumber: "", type: "CHECKING"})
                alert("Account updated successfully!");
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <TextInput
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
                <TextInput
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
                <TextInput
                    label={"Account Type"}
                    name="type"
                    radius="md"
                    mb="sm"
                    type="text"
                    placeholder="Account Type"
                    value={accountData.type}
                    onChange={handleChange}
                />
                <Button variant="filled" fullWidth color="teal" radius="md" type="submit">
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default EditAccountPage;
