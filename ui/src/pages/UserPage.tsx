import {useEffect, useState} from "react";
import {
    ActionIcon,
    Button,
    Card,
    Group,
    MenuDropdown,
    MenuItem,
    Modal,
    SimpleGrid,
    Text
} from "@mantine/core";
import {Menu, MenuTarget} from "@mantine/core";
import AddUserForm from "../form/AddUserForm";
import EditUserForm from "../form/EditUserForm";
import {User} from "../types/user";
import styles from "./UserPage.module.css";
import UserService from '../api/services/UserService';
import AddUserButton from "../components/AddUserButton";
import {FiEdit, FiMoreVertical, FiTrash2} from "react-icons/fi";

const UserPage = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isAddModalOpen, setAddModalOpen] = useState(false);
    const [isEditModalOpen, setEditModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState<string | null>(null);

    useEffect(() => {
        const fetchUsers = () => {
            UserService.listUsers()
                .then((data: User[]) => setUsers(data))
                .catch((err) => setError(err.message || "An error occurred while fetching users"))
                .finally(() => setLoading(false));
        };
        fetchUsers();
    }, []);

    const handleAddUser = (newUser: User) => {
        setUsers((prev) => [...prev, newUser]);
        setAddModalOpen(false);
    };

    const handleEditUser = (updatedUser: User) => {
        setUsers((prev) => prev.map((user) => (user.id === updatedUser.id ? updatedUser : user)));
        setEditModalOpen(false);
        setSelectedUser(null);
    };

    const handleDeleteClick = (userId: string) => {
        setUserToDelete(userId);
        setConfirmDeleteModal(true);
    };

    const deleteUser = async (userId: string) => {
        UserService.deleteUser(userId)
            .then(() => setUsers((prev) => prev.filter((user) => user.id !== userId)))
            .catch((err) => alert(err.message || "An error occurred while trying to delete the user."))
            .finally(() => {
                setConfirmDeleteModal(false);
                alert("User deleted successfully!");
            });
    };

    const renderUserCards = () =>
        users.map((user) => (
            <Card key={user.id} shadow="sm" padding="lg" className={styles.card}>
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
                            setSelectedUser(user);
                            setEditModalOpen(true);
                        }}>
                            Edit
                        </MenuItem>
                        <MenuItem leftSection={<FiTrash2/>} color="red" onClick={() => handleDeleteClick(user.id)}>
                            Delete
                        </MenuItem>
                    </MenuDropdown>
                </Menu>
                <Group>
                    <div>
                        <Text fw={600} size="md">{user.name}</Text>
                        <Text size="md" c="dimmed">Username: {user.username}</Text>
                        <Text size="md" c="dimmed">Role: {user.role}</Text>
                    </div>
                </Group>
            </Card>
        ));

    if (loading) return <div>Loading users...</div>;

    if (error) return <div>Error: {error}</div>;

    return (
        <div style={{margin: "0 auto", maxWidth: 800}}>
            <div className={styles.header}>
                <h1>User Management</h1>
            </div>
            <AddUserButton setAddModalOpen={setAddModalOpen}/>
            <div className={styles.scrollableContainer}>
                <SimpleGrid cols={{sm: 1, lg: 2}} spacing="sm">{renderUserCards()}</SimpleGrid>

                {/* Add User Modal */}
                <Modal opened={isAddModalOpen} onClose={() => setAddModalOpen(false)}>
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
                <Modal opened={confirmDeleteModal} onClose={() => setConfirmDeleteModal(false)}
                       title="Confirm Deletion">
                    <Text>Are you sure you want to delete this user?</Text>
                    <div style={{display: "flex", justifyContent: "flex-end", marginTop: 20}}>
                        <Button onClick={() => setConfirmDeleteModal(false)} style={{marginRight: 8}}>Cancel</Button>
                        <Button color="red" onClick={() => userToDelete && deleteUser(userToDelete)}>Delete</Button>
                    </div>
                </Modal>
            </div>


        </div>

    );
};

export default UserPage;