import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Select} from "@mantine/core";
import {Account} from "../../types/account";
import AccountService from '../../api/services/AccountService';
import StyledTextInput from "../../components/StyledTextInput";
import {notifications} from "@mantine/notifications";

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
                notifications.show({
                    message: `Account added successfully! id=${response.id}`,
                    color: "resilience",
                    icon: null
                })
                setAccountData({id: "", name: "", accountNumber: "", type: "CHECKING"});
            })
            .catch(error => {
                let data = error.response.data;
                if (data && data.message && data.details) {
                    const errorMessage = data.message || "An error occurred.";
                    const errorDetails = data.details ? data.details.join("\n") : "";
                    console.error(`${errorMessage}\n${errorDetails}`);
                } else {
                    console.error("An unexpected error occurred. Please try again.");
                }
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, margin: "auto", textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <StyledTextInput
                    autoComplete="new-name"
                    label={"Account Name"}
                    name="name"
                    placeholder="Name"
                    value={accountData.name}
                    onChange={handleChange}
                    required
                />
                <StyledTextInput
                    autoComplete="new-accountNumber"
                    label={"Account Number"}
                    name="accountNumber"
                    placeholder="Account Number"
                    value={accountData.accountNumber}
                    onChange={handleChange}
                    required
                />
                <Select
                    label={"Account Type"}
                    name="type"
                    radius="md"
                    mb="sm"
                    placeholder="Select Account Type"
                    value={accountData.type}
                    onChange={(value) =>
                        setAccountData((prev) => ({
                            ...prev,
                            type: value ? (value as "CHECKING" | "SAVINGS" | "CREDIT") : "CHECKING",
                        }))
                    }
                    data={[
                        {value: 'CHECKING', label: 'Checking'},
                        {value: 'SAVINGS', label: 'Savings'},
                        {value: 'CREDIT', label: 'Credit'},
                    ]}
                    required
                />
                <Button variant="filled" fullWidth color="resilience" radius="md" type="submit" style={{marginTop: "40px"}}>
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddAccountPage;
