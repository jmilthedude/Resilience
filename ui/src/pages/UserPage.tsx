import {useEffect, useState} from "react";
import {ActionIcon, Button, Card, Group, Modal, SimpleGrid, Text} from "@mantine/core";
import AddUserForm from "../form/AddUserForm";
import EditUserForm from "../form/EditUserForm";
import {User} from "../types/user";
import styles from "./UserPage.module.css";
import UserService from '../api/services/UserService';

const UserPage = () => {
    // State Management
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isAddModalOpen, setAddModalOpen] = useState(false);
    const [isEditModalOpen, setEditModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState<string | null>(null);

    // Fetch Users
    useEffect(() => {
        const fetchUsers = () => {
            UserService.listUsers()
                .then((data: User[]) => setUsers(data))
                .catch((err) => setError(err.message || "An error occurred while fetching users"))
                .finally(() => setLoading(false));
        };
        fetchUsers();
    }, []);

    // Handlers
    const handleAddUser = (newUser: User) => {
        setUsers((prev) => [...prev, newUser]);
        setAddModalOpen(false);
    };

    const handleEditUser = (updatedUser: User) => {
        setUsers((prev) => prev.map((user) => (user.id === updatedUser.id ? updatedUser : user)));
        setEditModalOpen(false);
        setSelectedUser(null);
    };

    const handleDeleteClick = (username: string) => {
        setUserToDelete(username);
        setConfirmDeleteModal(true);
    };

    const deleteUser = async (username: string) => {
        UserService.deleteUser(username)
            .then(() => setUsers((prev) => prev.filter((user) => user.username !== username)))
            .catch((err) => alert(err.message || "An error occurred while trying to delete the user."))
            .finally(() => setConfirmDeleteModal(false));
    };

    // Render Functions
    const renderUserCards = () =>
        users.map((user) => (
            <Card key={user.id} shadow="sm" padding="lg" className={styles.card}>
                <ActionIcon
                    color="red"
                    style={{position: "absolute", top: "16px", right: "16px"}}
                    onClick={() => handleDeleteClick(user.username)}
                >X</ActionIcon>
                <Group>
                    <div>
                        <Text fw={600} size="md">{user.name}</Text>
                        <Text size="md" c="dimmed">{user.username}</Text>
                        <Text size="xs" c="dimmed">{user.role}</Text>
                    </div>
                </Group>
                <Button
                    variant="light"
                    fullWidth
                    mt="md"
                    onClick={() => {
                        setSelectedUser(user);
                        setEditModalOpen(true);
                    }}
                >
                    Edit User
                </Button>
            </Card>
        ));

    // Error and Loading States
    if (loading) return <div>Loading users...</div>;

    if (error) return <div>Error: {error}</div>;

    // Main Return
    return (
        <div style={{maxWidth: 600, margin: "auto"}}>
            <div className={styles.header}>
                <h1>User Management</h1>
                <Button onClick={() => setAddModalOpen(true)} color="teal">
                    Add User
                </Button>
            </div>

            <SimpleGrid style={{marginLeft: "10px", marginRight: "10px"}} cols={{sm: 1, lg: 2}}
                        spacing="sm">{renderUserCards()}</SimpleGrid>

            {/* Add User Modal */}
            <Modal
                opened={isAddModalOpen}
                onClose={() => setAddModalOpen(false)}
            >
                <AddUserForm onSuccess={handleAddUser}/>
            </Modal>

            {/* Edit User Modal */}
            {selectedUser && (
                <Modal opened={isEditModalOpen} onClose={() => {
                    setEditModalOpen(false);
                    setSelectedUser(null);
                }}>
                    <EditUserForm user={selectedUser} onSuccess={handleEditUser}/>
                </Modal>
            )}

            {/* Delete Confirmation Modal */}
            <Modal opened={confirmDeleteModal} onClose={() => setConfirmDeleteModal(false)} title="Confirm Deletion">
                <Text>Are you sure you want to delete this user?</Text>
                <div style={{display: "flex", justifyContent: "flex-end", marginTop: 20}}>
                    <Button onClick={() => setConfirmDeleteModal(false)} style={{marginRight: 8}}>
                        Cancel
                    </Button>
                    <Button color="red" onClick={() => userToDelete && deleteUser(userToDelete)}>
                        Delete
                    </Button>
                </div>
            </Modal>
        </div>
    );
};

export default UserPage;