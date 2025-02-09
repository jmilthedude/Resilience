import { AxiosResponse } from 'axios';
import useApi from "../axiosInstance";
import {User} from "../../types/user"; // Ensure the correct path to the axios instance

interface UpdatePasswordRequest {
    currentPassword: string;
    newPassword: string;
}

interface UserServiceResponse {
    message: string;
    data?: any;
}

class UserService {
    private readonly api = useApi();
    private readonly baseUrl: string = '/users';

    /**
     * Get a list of all users.
     */
    public async listUsers(): Promise<User[]> {
        const response: AxiosResponse<User[]> = await this.api.get(this.baseUrl);
        return response.data;
    }

    /**
     * Fetch a specific user by username.
     * @param username Target user's username
     */
    public async getUser(username: string): Promise<User> {
        const response: AxiosResponse<User> = await this.api.get(`${this.baseUrl}/${username}`);
        return response.data;
    }

    /**
     * Fetch the currently authenticated user.
     */
    public async getCurrentUser(): Promise<User> {
        const response: AxiosResponse<User> = await this.api.get(`${this.baseUrl}/me`);
        return response.data;
    }

    /**
     * Add a new user.
     * @param userData Object containing new user data
     */
    public async addUser(userData: User): Promise<User> {
        const response: AxiosResponse<User> = await this.api.post(this.baseUrl, userData);
        return response.data;
    }

    /**
     * Update a user's password.
     * @param username Target username
     * @param passwordData Object containing current and new password
     */
    public async updateUserPassword(username: string, passwordData: UpdatePasswordRequest): Promise<UserServiceResponse> {
        const response: AxiosResponse<UserServiceResponse> = await this.api.patch(`${this.baseUrl}/${username}/password`, passwordData);
        return response.data;
    }

    /**
     * Update a user's password.
     * @param passwordData Object containing current and new password
     */
    public async updateCurrentUserPassword(passwordData: UpdatePasswordRequest): Promise<UserServiceResponse> {
        const currentUser = await this.getCurrentUser();
        const username = currentUser.username;
        const response: AxiosResponse<UserServiceResponse> = await this.api.patch(`${this.baseUrl}/${username}/password`, passwordData);
        return response.data;
    }

    /**
     * Delete a user by username.
     * @param username Target user's username
     */
    public async deleteUser(username: string): Promise<UserServiceResponse> {
        const response: AxiosResponse<UserServiceResponse> = await this.api.del(`${this.baseUrl}/${username}`);
        return response.data;
    }
}

const instance = new UserService();
export default instance;
