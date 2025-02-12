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

    public async listUsers(): Promise<User[]> {
        const response: AxiosResponse<User[]> = await this.api.get(this.baseUrl);
        return response.data;
    }

    public async getUser(userId: bigint): Promise<User> {
        const response: AxiosResponse<User> = await this.api.get(`${this.baseUrl}/${userId}`);
        return response.data;
    }

    public async getCurrentUser(): Promise<User> {
        const response: AxiosResponse<User> = await this.api.get(`${this.baseUrl}/me`);
        return response.data;
    }

    public async addUser(userData: User): Promise<User> {
        const response: AxiosResponse<User> = await this.api.post(this.baseUrl, userData);
        return response.data;
    }

    public async updateUser(userId: string, userData: User): Promise<User> {
        const response: AxiosResponse<User> = await this.api.patch(`${this.baseUrl}/${userId}`, userData);
        return response.data;
    }

    public async updateUserPassword(username: string, passwordData: UpdatePasswordRequest): Promise<UserServiceResponse> {
        const response: AxiosResponse<UserServiceResponse> = await this.api.patch(`${this.baseUrl}/${username}/password`, passwordData);
        return response.data;
    }

    public async updateCurrentUserPassword(passwordData: UpdatePasswordRequest): Promise<UserServiceResponse> {
        const currentUser = await this.getCurrentUser();
        const username = currentUser.username;
        const response: AxiosResponse<UserServiceResponse> = await this.api.patch(`${this.baseUrl}/${username}/password`, passwordData);
        return response.data;
    }

    public async deleteUser(id: string): Promise<UserServiceResponse> {
        const response: AxiosResponse<UserServiceResponse> = await this.api.del(`${this.baseUrl}/${id}`);
        return response.data;
    }
}

const instance = new UserService();
export default instance;
