import { apiFetch } from './client';

export function getPortfolio() {
    return apiFetch('/portfolio');
}