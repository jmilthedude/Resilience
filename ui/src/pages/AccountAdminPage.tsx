import {useEffect, useState} from "react";
import {
    ActionIcon,
    Button,
    Card,
    Group,
    Menu,
    MenuDropdown,
    MenuItem,
    MenuTarget,
    Modal,
    SimpleGrid,
    Text
} from "@mantine/core";
import AddAccountForm from "../form/account/AddAccountForm";
import EditAccountForm from "../form/account/EditAccountForm";
import {Account} from "../types/account";
import styles from "./AccountAdminPage.module.css";
import AccountService from '../api/services/AccountService';
import AddModalButton from "../components/AddModalButton";
import {FiEdit, FiMoreVertical, FiTrash2} from "react-icons/fi";
import {notifications} from "@mantine/notifications";

const AccountAdminPage = () => {
    const [accounts, setAccounts] = useState<Account[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isAddModalOpen, setAddModalOpen] = useState(false);
    const [isEditModalOpen, setEditModalOpen] = useState(false);
    const [selectedAccount, setSelectedAccount] = useState<Account | null>(null);
    const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
    const [accountToDelete, setAccountToDelete] = useState<string | null>(null);

    useEffect(() => {
        const fetchAccounts = () => {
            AccountService.listAccounts()
                .then((data: Account[]) => setAccounts(data))
                .catch((err) => setError(err.message || "An error occurred while fetching accounts"))
                .finally(() => setLoading(false));
        };
        fetchAccounts();
    }, []);

    const handleAddAccount = (newAccount: Account) => {
        setAccounts((prev) => [...prev, newAccount]);
        setAddModalOpen(false);
    };

    const handleEditAccount = (updatedAccount: Account) => {
        setAccounts((prev) => prev.map((account) => (account.id === updatedAccount.id ? updatedAccount : account)));
        setEditModalOpen(false);
        setSelectedAccount(null);
    };

    const handleDeleteClick = (accountId: string) => {
        setAccountToDelete(accountId);
        setConfirmDeleteModal(true);
    };

    const deleteAccount = async (accountId: string) => {
        AccountService.deleteAccount(accountId)
            .then(() => setAccounts((prev) => prev.filter((account) => account.id !== accountId)))
            .catch((err) => console.error(err.message || "An error occurred while trying to delete the account."))
            .finally(() => {
                setConfirmDeleteModal(false);
                notifications.show({
                    message: `Account deleted successfully!`,
                    color: "resilience",
                    icon: null
                })
            });
    };

    const renderAccountCards = () =>
        accounts.map((account) => (
            <Card key={account.id} shadow="sm" padding="lg" className={styles.card}>
                <Menu shadow="md" width={100} position="bottom-end">
                    <MenuTarget>
                        <ActionIcon
                            variant={"light"}
                            color={"rgba(128,128,128, 1)"}
                            style={{position: "absolute", top: "16px", right: "16px"}}
                        >
                            <FiMoreVertical/>
                        </ActionIcon>
                    </MenuTarget>
                    <MenuDropdown>
                        <MenuItem leftSection={<FiEdit/>} onClick={() => {
                            setSelectedAccount(account);
                            setEditModalOpen(true);
                        }}>
                            Edit
                        </MenuItem>
                        <MenuItem leftSection={<FiTrash2/>} color="red" onClick={() => handleDeleteClick(account.id)}>
                            Delete
                        </MenuItem>
                    </MenuDropdown>
                </Menu>
                <Group>
                    <div>
                        <Text fw={600} size="md">{account.name}</Text>
                        <Text size="md" c="dimmed">Account Number: {"**" + account.accountNumber.substring(account.accountNumber.length - 3)}</Text>
                        <Text size="md" c="dimmed">Account Type: {account.type}</Text>
                    </div>
                </Group>
            </Card>
        ));

    if (loading) return <div>Loading accounts...</div>;

    if (error) return <div>Error: {error}</div>;

    return (
        <div style={{margin: "0 auto", maxWidth: 800}}>
            <AddModalButton setAddModalOpen={setAddModalOpen} text={"Add Account"}/>
            <div className={styles.header}>
                <h1>Account Management</h1>
            </div>
            <div className={styles.scrollableContainer}>
                <SimpleGrid cols={accounts.length === 1 ? 1 : {sm: 1, lg: 2}} spacing="sm">{renderAccountCards()}</SimpleGrid>

                {/* Add Account Modal */}
                <Modal title={"Add Account"} opened={isAddModalOpen} onClose={() => setAddModalOpen(false)}>
                    <AddAccountForm onSuccess={handleAddAccount}/>
                </Modal>

                {/* Edit Account Modal */}
                {selectedAccount && (
                    <Modal title={"Edit Account"} opened={isEditModalOpen} onClose={() => {
                        setEditModalOpen(false);
                        setSelectedAccount(null);
                    }}>
                        <EditAccountForm account={selectedAccount} onSuccess={handleEditAccount}/>
                    </Modal>
                )}

                {/* Delete Confirmation Modal */}
                <Modal opened={confirmDeleteModal} onClose={() => setConfirmDeleteModal(false)}
                       title="Confirm Deletion">
                    <Text>Are you sure you want to delete this account?</Text>
                    <div style={{display: "flex", justifyContent: "flex-end", marginTop: 20}}>
                        <Button onClick={() => setConfirmDeleteModal(false)} style={{marginRight: 8}}>Cancel</Button>
                        <Button color="red" onClick={() => accountToDelete && deleteAccount(accountToDelete)}>Delete</Button>
                    </div>
                </Modal>
            </div>


        </div>

    );
};

export default AccountAdminPage;