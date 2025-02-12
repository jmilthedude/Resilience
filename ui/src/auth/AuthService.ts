import useApi from "../api/axiosInstance";

interface AuthStatusResponse {
    isAuthenticated: boolean;
}

class AuthService {
    private readonly api = useApi();
    private readonly baseUrl: string = '/auth';

    public async login(username: string, password: string): Promise<void> {
        await this.api.post(`${this.baseUrl}/login`, {username, password});
    }

    public async logout(): Promise<void> {
        await this.api.post(`${this.baseUrl}/logout`, {});
    }

    public async checkAuthStatus(): Promise<AuthStatusResponse> {
        const response = await this.api.get(`${this.baseUrl}/status`);
        return response.data;
    }
}

const instance = new AuthService();
export default instance;