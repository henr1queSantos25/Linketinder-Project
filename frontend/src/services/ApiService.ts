const BASE_URL = 'http://localhost:8080/api';

export class ApiService {
    
    static async post(endpoint: string, data: any): Promise<any> {
        const response = await fetch(`${BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.erro || 'Erro na requisição');
        }
        return response.json();
    }

    static async get(endpoint: string): Promise<any> {
        const response = await fetch(`${BASE_URL}${endpoint}`);
        
        if (!response.ok) {
            throw new Error('Erro na requisição GET');
        }
        return response.json();
    }
}