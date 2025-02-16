import { AxiosResponse } from 'axios';
import useApi from "../axiosInstance";
import {Account} from "../../types/account";

interface AccountServiceResponse {
    message: string;
    data?: any;
}

class AccountService {
    private readonly api = useApi();
    private readonly baseUrl: string = '/accounts';

    public async listAccounts(): Promise<Account[]> {
        const response: AxiosResponse<Account[]> = await this.api.get(this.baseUrl);
        return response.data;
    }

    public async getAccount(accountId: bigint): Promise<Account> {
        const response: AxiosResponse<Account> = await this.api.get(`${this.baseUrl}/${accountId}`);
        return response.data;
    }

    public async addAccount(accountData: Account): Promise<Account> {
        const response: AxiosResponse<Account> = await this.api.post(this.baseUrl, accountData);
        return response.data;
    }

    public async updateAccount(accountId: string, accountData: Account): Promise<Account> {
        const response: AxiosResponse<Account> = await this.api.patch(`${this.baseUrl}/${accountId}`, accountData);
        return response.data;
    }

    public async deleteAccount(id: string): Promise<AccountServiceResponse> {
        const response: AxiosResponse<AccountServiceResponse> = await this.api.del(`${this.baseUrl}/${id}`);
        return response.data;
    }
}

const instance = new AccountService();
export default instance;
