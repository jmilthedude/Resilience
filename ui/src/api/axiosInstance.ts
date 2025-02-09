import axios, {AxiosError, AxiosInstance, AxiosResponse} from 'axios';

interface ApiErrorResponse {
    message?: string;
}

class Api {
    private readonly api: AxiosInstance;

    constructor() {
        this.api = axios.create({
            baseURL: 'http://localhost:8081/api/v1',
            timeout: 10000,
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            },
            withCredentials: true
        });

        this.init();
    }

    private init() {
        this.api.interceptors.response.use(
            (response: AxiosResponse) => response,
            (error: AxiosError<ApiErrorResponse>) => {
                if (error.response) {
                    const apiError = error.response.data as ApiErrorResponse;
                    const errorMessage =
                        apiError.message || `An error occurred with status code ${error.response.status}`;

                    console.error(
                        `Server Error [${error.response.status}]: ${errorMessage}`
                    );
                    return Promise.reject(new Error(errorMessage));
                } else if (error.request) {
                    console.error('Network Error:', error.message);
                    return Promise.reject(new Error('No response from the server.'));
                } else {
                    console.error('Error:', error.message);
                    return Promise.reject(new Error('Something went wrong.'));
                }
            }
        );
    }


    public async get(url: string, params?: Record<string, any>): Promise<AxiosResponse> {
        try {
            return await this.api.get(url, {
                params: params,
            });
        } catch (error) {
            console.error(`Error while making GET request to ${url}:`, error);
            throw error;
        }
    };

    public async post(url: string, data: any): Promise<AxiosResponse> {
        try {
            return await this.api.post(url, data);
        } catch (error) {
            console.error(`Error while making POST request to ${url}:`, error);
            throw error;
        }
    };

    public async postForm(url: string, data: Record<string, string>) {
        return axios.post(
            url,
            new URLSearchParams(data).toString(),
            {
                headers: {"Content-Type": "application/x-www-form-urlencoded"},
            }
        );
    };


    public async del(url: string): Promise<AxiosResponse> {
        try {
            return await this.api.delete(url);
        } catch (error) {
            console.error(`Error while making DELETE request to ${url}:`, error);
            throw error;
        }
    };

    public async patch(url: string, data: any): Promise<AxiosResponse> {
        try {
            return await this.api.patch(url, data);
        } catch (error) {
            console.error(`Error while making PATCH request to ${url}:`, error);
            throw error;
        }
    };
}

const useApi = (): Api => {
    return new Api();
};

export default useApi;

