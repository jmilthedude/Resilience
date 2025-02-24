import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Select} from "@mantine/core";
import {Account} from "../../types/account";
import AccountService from '../../api/services/AccountService';
import StyledTextInput from "../../components/StyledTextInput";
import {notifications} from "@mantine/notifications";

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
                notifications.show({
                    message: `Account edited successfully!`,
                    color: "resilience",
                    icon: null
                })
                setAccountData({id: "", name: "", accountNumber: "", type: "CHECKING"})
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <StyledTextInput
                    label={"Account Name"}
                    name="name"
                    placeholder="Name"
                    value={accountData.name}
                    onChange={handleChange}
                    required
                />
                <StyledTextInput
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
                    placeholder="Select Account Type"
                    value={accountData.type}
                    onChange={(value) =>
                        setAccountData((prev) => ({
                            ...prev,
                            type: value ? (value as "CHECKING" | "SAVINGS" | "CREDIT") : "CHECKING",
                        }))
                    }
                    data={[
                        { value: 'CHECKING', label: 'Checking' },
                        { value: 'SAVINGS', label: 'Savings' },
                        { value: 'CREDIT', label: 'Credit' },
                    ]}
                    required
                />
                <Button variant="filled" mt={"xl"} fullWidth color="resilience" radius="md" type="submit">
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default EditAccountPage;
