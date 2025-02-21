import {useEffect, useState} from "react";
import {ActionIcon, Box, Button, Card, Group, LoadingOverlay, Menu, MenuDropdown, MenuItem, MenuTarget, Modal, Table, Text} from "@mantine/core";
import AddUserForm from "../form/user/AddUserForm";
import EditUserForm from "../form/user/EditUserForm";
import {User} from "../types/user";
import styles from "./UserAdminPage.module.css";
import UserService from '../api/services/UserService';
import AddModalButton from "../components/AddModalButton";
import {FiEdit, FiMoreVertical, FiTrash2} from "react-icons/fi";

const UserAdminPage = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isAddModalOpen, setAddModalOpen] = useState(false);
    const [isEditModalOpen, setEditModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [confirmDeleteModal, setConfirmDeleteModal] = useState(false);

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

    const handleDeleteClick = (user: User) => {
        setSelectedUser(user);
        setConfirmDeleteModal(true);
    };

    const deleteUser = async (user: User) => {
        UserService.deleteUser(user.id)
            .then(() => setUsers((prev) => prev.filter((u) => u.id !== user.id)))
            .catch((err) => alert(err.message || "An error occurred while trying to delete the user."))
            .finally(() => {
                setConfirmDeleteModal(false);
                alert("User deleted successfully!");
            });
    };

    if (error) return <div>Error: {error}</div>;

    function createEditMenu(user: User) {
        return <Menu shadow="md" width={100}>
            <MenuTarget>
                <ActionIcon variant={"light"} color={"rgba(128,128,128, 1)"}>
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
                <MenuItem leftSection={<FiTrash2/>} color="red"
                          onClick={() => handleDeleteClick(user)}>
                    Delete
                </MenuItem>
            </MenuDropdown>
        </Menu>;
    }

    function fillUserTable() {
        return <>
            {users.map((user) => (
                <Table.Tr key={user.id}>
                    <Table.Td width={"40%"}>{user.name}</Table.Td>
                    <Table.Td width={"40%"}>{user.username}</Table.Td>
                    <Table.Td width={"10%"}>{user.role}</Table.Td>
                    <Table.Td width={"10%"} style={{textAlign: "right"}}>
                        {createEditMenu(user)}
                    </Table.Td>
                </Table.Tr>
            ))}
        </>;
    }

    return (
        <div style={{margin: "0 auto", maxWidth: 800}}>
            <div className={styles.header}>
                <h1>User Management</h1>
            </div>

            <AddModalButton setAddModalOpen={setAddModalOpen} text="Add User"/>
            <Box pos={"relative"}>
                <LoadingOverlay visible={loading} zIndex={1000} overlayProps={{radius: "lg", blur: 2}}/>
                <Table striped highlightOnHover>
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>Name</Table.Th>
                            <Table.Th>Username</Table.Th>
                            <Table.Th>Role</Table.Th>
                            <Table.Th>Actions</Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {fillUserTable()}
                    </Table.Tbody>
                </Table>
            </Box>

            {/* Add User Modal */}
            <Modal size={"sm"}
                   title={"Add User"}
                   overlayProps={{backgroundOpacity: 0.65, blur: 3,}}
                   opened={isAddModalOpen} onClose={() => setAddModalOpen(false)}>
                <AddUserForm onSuccess={handleAddUser}/>
            </Modal>

            {/* Edit User Modal */}
            {selectedUser && (
                <Modal
                    title={"Edit User"}
                    size={"sm"}
                    overlayProps={{backgroundOpacity: 0.65, blur: 3,}}
                    opened={isEditModalOpen}
                    onClose={() => {
                        setEditModalOpen(false);
                        setSelectedUser(null);
                    }}>
                    <EditUserForm user={selectedUser} onSuccess={handleEditUser}/>
                </Modal>
            )}


            {/* Delete Confirmation Modal */}
            <Modal size={"sm"}
                   overlayProps={{backgroundOpacity: 0.65, blur: 3,}}
                   opened={confirmDeleteModal}
                   onClose={() => setConfirmDeleteModal(false)}
                   title="Confirm Deletion">
                <Text>Are you sure you want to delete this user?</Text>
                <div style={{display: "flex", justifyContent: "flex-end", marginTop: 20}}>
                    <Button variant={"light"} onClick={() => setConfirmDeleteModal(false)}
                            style={{marginRight: 8}}>Cancel</Button>
                    <Button variant={"outline"} color="red"
                            onClick={() => selectedUser && deleteUser(selectedUser)}>Delete</Button>
                </div>
            </Modal>

        </div>

    );
};

export default UserAdminPage;